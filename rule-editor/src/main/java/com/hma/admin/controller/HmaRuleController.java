package com.hma.admin.controller;

import com.hma.admin.dto.ApiResponse;
import com.hma.admin.dto.HmaRuleCreateDTO;
import com.hma.admin.dto.HmaRuleDTO;
import com.hma.admin.service.HmaRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rules")
public class HmaRuleController {

    private final HmaRuleService hmaRuleService;

    public HmaRuleController(HmaRuleService hmaRuleService) {
        this.hmaRuleService = hmaRuleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HmaRuleDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(hmaRuleService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HmaRuleDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(hmaRuleService.findById(id)));
    }

    @GetMapping("/phone-model/{phoneModelId}")
    public ResponseEntity<ApiResponse<List<HmaRuleDTO>>> findByPhoneModelId(@PathVariable Long phoneModelId) {
        return ResponseEntity.ok(ApiResponse.success(hmaRuleService.findByPhoneModelId(phoneModelId)));
    }

    @GetMapping("/model-code/{modelCode}")
    public ResponseEntity<ApiResponse<List<HmaRuleDTO>>> findByModelCode(@PathVariable String modelCode) {
        return ResponseEntity.ok(ApiResponse.success(hmaRuleService.findByModelCode(modelCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HmaRuleDTO>> create(@Valid @RequestBody HmaRuleCreateDTO dto) {
        return new ResponseEntity<>(ApiResponse.success("创建成功", hmaRuleService.create(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HmaRuleDTO>> update(@PathVariable Long id, @Valid @RequestBody HmaRuleCreateDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("更新成功", hmaRuleService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        hmaRuleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @DeleteMapping("/phone/{phoneModelId}/app/{appInfoId}")
    public ResponseEntity<ApiResponse<Void>> deleteByPhoneAndApp(
            @PathVariable Long phoneModelId,
            @PathVariable Long appInfoId) {
        hmaRuleService.deleteByPhoneAndApp(phoneModelId, appInfoId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}
