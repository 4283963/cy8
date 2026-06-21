package com.hma.distribution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.hma.admin.entity.HmaRule;
import com.hma.admin.entity.PhoneModel;
import com.hma.admin.repository.HmaRuleRepository;
import com.hma.admin.repository.PhoneModelRepository;
import com.hma.distribution.config.CacheProperties;
import com.hma.distribution.dto.CachedRuleEntry;
import com.hma.distribution.dto.HmaConfigDTO;
import com.hma.distribution.dto.RuleDownloadDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RuleDistributionService {

    private static final Logger log = LoggerFactory.getLogger(RuleDistributionService.class);
    private static final String CACHE_KEY_RULES = "rules:";

    private final HmaRuleRepository hmaRuleRepository;
    private final PhoneModelRepository phoneModelRepository;
    private final ObjectMapper objectMapper;
    private final Cache<String, CachedRuleEntry> ruleDownloadCache;
    private final CacheProperties cacheProperties;
    private final AsyncTaskExecutor refreshExecutor;
    private final AccessMonitorService accessMonitorService;

    public RuleDistributionService(HmaRuleRepository hmaRuleRepository,
                                   PhoneModelRepository phoneModelRepository,
                                   @Qualifier("ruleDownloadCache") Cache<String, CachedRuleEntry> ruleDownloadCache,
                                   CacheProperties cacheProperties,
                                   @Qualifier("cacheRefreshExecutor") AsyncTaskExecutor refreshExecutor,
                                   AccessMonitorService accessMonitorService) {
        this.hmaRuleRepository = hmaRuleRepository;
        this.phoneModelRepository = phoneModelRepository;
        this.ruleDownloadCache = ruleDownloadCache;
        this.cacheProperties = cacheProperties;
        this.refreshExecutor = refreshExecutor;
        this.accessMonitorService = accessMonitorService;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public RuleDownloadDTO getRulesForModel(String modelCode) {
        String key = CACHE_KEY_RULES + modelCode;
        CachedRuleEntry entry = ruleDownloadCache.getIfPresent(key);
        Duration refreshAfter = cacheProperties.getRules().getRefreshAfterWrite();

        if (entry != null) {
            if (isFresh(entry, refreshAfter)) {
                log.debug("Cache hit (fresh) for model {}", modelCode);
                accessMonitorService.recordSuccessAsync(modelCode);
                return entry.getData();
            }
            log.debug("Cache hit (stale) for model {}, returning cached and triggering async refresh", modelCode);
            triggerAsyncRefresh(key, modelCode);
            accessMonitorService.recordSuccessAsync(modelCode);
            return entry.getData();
        }

        try {
            RuleDownloadDTO fresh = loadFromDatabase(modelCode);
            ruleDownloadCache.put(key, new CachedRuleEntry(fresh, Instant.now(), true));
            log.info("Loaded {} rules for model {} from database and cached", fresh.getTotalRules(), modelCode);
            accessMonitorService.recordSuccessAsync(modelCode);
            return fresh;
        } catch (RuntimeException ex) {
            log.error("Database unavailable and no cache fallback for model {}", modelCode, ex);
            accessMonitorService.recordFailureAsync(modelCode, ex.getMessage());
            throw ex;
        }
    }

    private boolean isFresh(CachedRuleEntry entry, Duration refreshAfter) {
        if (entry.getCreatedAt() == null) {
            return false;
        }
        return entry.getCreatedAt().plus(refreshAfter).isAfter(Instant.now());
    }

    private void triggerAsyncRefresh(String key, String modelCode) {
        CompletableFuture.runAsync(() -> {
            try {
                RuleDownloadDTO fresh = loadFromDatabase(modelCode);
                ruleDownloadCache.put(key, new CachedRuleEntry(fresh, Instant.now(), true));
                log.info("Async refresh succeeded for model {}", modelCode);
            } catch (RuntimeException ex) {
                CachedRuleEntry existing = ruleDownloadCache.getIfPresent(key);
                if (existing != null) {
                    ruleDownloadCache.put(key, new CachedRuleEntry(existing.getData(), existing.getCreatedAt(), false));
                    log.warn("Async refresh failed for model {}, kept stale cache: {}", modelCode, ex.getMessage());
                } else {
                    log.warn("Async refresh failed for model {} and no cache to keep: {}", modelCode, ex.getMessage());
                }
            }
        }, refreshExecutor);
    }

    private RuleDownloadDTO loadFromDatabase(String modelCode) {
        PhoneModel phoneModel = phoneModelRepository.findByModelCode(modelCode)
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found: " + modelCode));

        if (!phoneModel.getIsActive()) {
            throw new IllegalArgumentException("Phone model is not active: " + modelCode);
        }

        List<HmaRule> rules = hmaRuleRepository.findEnabledRulesByModelCode(modelCode);

        RuleDownloadDTO dto = new RuleDownloadDTO();
        dto.setModelCode(phoneModel.getModelCode());
        dto.setModelName(phoneModel.getModelName());
        dto.setBrand(phoneModel.getBrand());
        dto.setUpdateTime(LocalDateTime.now());
        dto.setTotalRules(rules.size());
        dto.setEnabledRules((int) rules.stream().filter(HmaRule::getIsEnabled).count());

        List<RuleDownloadDTO.RuleItem> ruleItems = rules.stream()
                .sorted(Comparator.comparing(HmaRule::getPriority).reversed())
                .map(this::convertToRuleItem)
                .collect(Collectors.toList());
        dto.setRules(ruleItems);

        return dto;
    }

    public String getHmaConfigFile(String modelCode) {
        RuleDownloadDTO rules = getRulesForModel(modelCode);

        HmaConfigDTO config = new HmaConfigDTO();
        config.setVersion("1.0");
        config.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        config.setModelCode(rules.getModelCode());
        config.setModelName(rules.getModelName());
        config.setBrand(rules.getBrand());

        HmaConfigDTO.Config hmaConfig = new HmaConfigDTO.Config();
        hmaConfig.setMode("BLACKLIST");

        List<String> blacklist = rules.getRules().stream()
                .filter(r -> "BLACKLIST".equals(r.getRuleType()))
                .map(RuleDownloadDTO.RuleItem::getPackageName)
                .collect(Collectors.toList());
        hmaConfig.setBlacklist(blacklist);

        List<String> whitelist = rules.getRules().stream()
                .filter(r -> "WHITELIST".equals(r.getRuleType()))
                .map(RuleDownloadDTO.RuleItem::getPackageName)
                .collect(Collectors.toList());
        hmaConfig.setWhitelist(whitelist);

        hmaConfig.setTemplateApps(new ArrayList<>());
        hmaConfig.setHideSystem(false);
        hmaConfig.setUseRegex(false);
        hmaConfig.setExtraRules(new ArrayList<>());

        config.setConfig(hmaConfig);

        try {
            return objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            log.error("Failed to generate HMA config JSON", e);
            throw new RuntimeException("Failed to generate config file", e);
        }
    }

    public String getSimplePackageList(String modelCode) {
        RuleDownloadDTO rules = getRulesForModel(modelCode);
        return rules.getRules().stream()
                .map(RuleDownloadDTO.RuleItem::getPackageName)
                .collect(Collectors.joining("\n"));
    }

    public byte[] getRulesAsFile(String modelCode, String format) {
        return switch (format.toLowerCase()) {
            case "json" -> getHmaConfigFile(modelCode).getBytes();
            case "list" -> getSimplePackageList(modelCode).getBytes();
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    public void evictCache(String modelCode) {
        ruleDownloadCache.invalidate(CACHE_KEY_RULES + modelCode);
        log.info("Evicted cache for model {}", modelCode);
    }

    public void evictAllCache() {
        ruleDownloadCache.invalidateAll();
        log.info("Evicted all rule caches");
    }

    public CacheStats cacheStats() {
        return ruleDownloadCache.stats();
    }

    public long cacheSize() {
        return ruleDownloadCache.estimatedSize();
    }

    private RuleDownloadDTO.RuleItem convertToRuleItem(HmaRule rule) {
        RuleDownloadDTO.RuleItem item = new RuleDownloadDTO.RuleItem();
        item.setPackageName(rule.getAppInfo().getPackageName());
        item.setAppName(rule.getAppInfo().getAppName());
        item.setRuleType(rule.getRuleType());
        item.setScope(rule.getScope());
        item.setPriority(rule.getPriority());
        return item;
    }
}
