package com.hma.distribution.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hma.admin.entity.HmaRule;
import com.hma.admin.entity.PhoneModel;
import com.hma.admin.repository.HmaRuleRepository;
import com.hma.admin.repository.PhoneModelRepository;
import com.hma.distribution.dto.HmaConfigDTO;
import com.hma.distribution.dto.RuleDownloadDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleDistributionService {

    private static final Logger log = LoggerFactory.getLogger(RuleDistributionService.class);

    private final HmaRuleRepository hmaRuleRepository;
    private final PhoneModelRepository phoneModelRepository;
    private final ObjectMapper objectMapper;

    public RuleDistributionService(HmaRuleRepository hmaRuleRepository,
                                   PhoneModelRepository phoneModelRepository) {
        this.hmaRuleRepository = hmaRuleRepository;
        this.phoneModelRepository = phoneModelRepository;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public RuleDownloadDTO getRulesForModel(String modelCode) {
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

        log.info("Downloaded {} rules for model {}", rules.size(), modelCode);
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
