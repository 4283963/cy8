package com.hma.distribution.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.hma.distribution.dto.CacheStatsDTO;
import com.hma.distribution.dto.DailyAccessStatDTO;
import com.hma.distribution.dto.RuleDownloadDTO;
import com.hma.distribution.service.AccessMonitorService;
import com.hma.distribution.service.RuleDistributionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/distribution")
public class RuleDistributionController {

    private final RuleDistributionService ruleDistributionService;
    private final AccessMonitorService accessMonitorService;

    public RuleDistributionController(RuleDistributionService ruleDistributionService,
                                       AccessMonitorService accessMonitorService) {
        this.ruleDistributionService = ruleDistributionService;
        this.accessMonitorService = accessMonitorService;
    }

    @GetMapping("/rules/{modelCode}")
    public ResponseEntity<RuleDownloadDTO> getRules(@PathVariable String modelCode) {
        return ResponseEntity.ok(ruleDistributionService.getRulesForModel(modelCode));
    }

    @GetMapping("/rules/{modelCode}/json")
    public ResponseEntity<String> getRulesAsJson(@PathVariable String modelCode) {
        String config = ruleDistributionService.getHmaConfigFile(modelCode);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"hma_config_" + modelCode + ".json\"")
                .body(config);
    }

    @GetMapping("/rules/{modelCode}/list")
    public ResponseEntity<String> getRulesAsList(@PathVariable String modelCode) {
        String list = ruleDistributionService.getSimplePackageList(modelCode);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"packages_" + modelCode + ".txt\"")
                .body(list);
    }

    @GetMapping("/rules/{modelCode}/download")
    public ResponseEntity<byte[]> downloadRules(
            @PathVariable String modelCode,
            @RequestParam(defaultValue = "json") String format) {
        byte[] data = ruleDistributionService.getRulesAsFile(modelCode, format);
        MediaType mediaType = "json".equals(format) ? MediaType.APPLICATION_JSON : MediaType.TEXT_PLAIN;
        String extension = "json".equals(format) ? "json" : "txt";

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"hma_rules_" + modelCode + "." + extension + "\"")
                .body(data);
    }

    @DeleteMapping("/cache/{modelCode}")
    public ResponseEntity<Map<String, Object>> evictCache(@PathVariable String modelCode) {
        ruleDistributionService.evictCache(modelCode);
        return ResponseEntity.ok(Map.of(
                "evicted", modelCode,
                "status", "ok"
        ));
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, Object>> evictAllCache() {
        ruleDistributionService.evictAllCache();
        return ResponseEntity.ok(Map.of(
                "evicted", "all",
                "status", "ok"
        ));
    }

    @GetMapping("/cache/stats")
    public ResponseEntity<CacheStatsDTO> cacheStats() {
        CacheStats stats = ruleDistributionService.cacheStats();
        CacheStatsDTO dto = new CacheStatsDTO();
        dto.setEstimatedSize(ruleDistributionService.cacheSize());
        dto.setHitCount(stats.hitCount());
        dto.setMissCount(stats.missCount());
        dto.setHitRate(stats.hitRate());
        dto.setRequestCount(stats.requestCount());
        dto.setLoadSuccessCount(stats.loadSuccessCount());
        dto.setLoadFailureCount(stats.loadFailureCount());
        dto.setEvictionCount(stats.evictionCount());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/monitor/stats/today")
    public ResponseEntity<List<DailyAccessStatDTO>> getTodayStats() {
        return ResponseEntity.ok(accessMonitorService.getTodayStats());
    }

    @GetMapping("/monitor/stats")
    public ResponseEntity<List<DailyAccessStatDTO>> getStatsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(accessMonitorService.getStatsByDate(date));
    }

    @GetMapping("/monitor/stats/{modelCode}")
    public ResponseEntity<DailyAccessStatDTO> getTodayStatByModel(@PathVariable String modelCode) {
        DailyAccessStatDTO dto = accessMonitorService.getTodayStatByModel(modelCode);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/monitor/abnormal/today")
    public ResponseEntity<List<DailyAccessStatDTO>> getTodayAbnormalList() {
        return ResponseEntity.ok(accessMonitorService.getTodayAbnormalList());
    }

    @GetMapping("/monitor/abnormal")
    public ResponseEntity<List<DailyAccessStatDTO>> getAbnormalListByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(accessMonitorService.getAbnormalListByDate(date));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("HMA Rule Distribution Service is running");
    }
}
