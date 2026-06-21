package com.hma.distribution.controller;

import com.hma.distribution.dto.RuleDownloadDTO;
import com.hma.distribution.service.RuleDistributionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distribution")
public class RuleDistributionController {

    private final RuleDistributionService ruleDistributionService;

    public RuleDistributionController(RuleDistributionService ruleDistributionService) {
        this.ruleDistributionService = ruleDistributionService;
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

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("HMA Rule Distribution Service is running");
    }
}
