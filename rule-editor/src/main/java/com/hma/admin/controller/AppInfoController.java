package com.hma.admin.controller;

import com.hma.admin.dto.ApiResponse;
import com.hma.admin.dto.AppInfoDTO;
import com.hma.admin.service.AppInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/apps")
public class AppInfoController {

    private final AppInfoService appInfoService;

    public AppInfoController(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppInfoDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppInfoDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findById(id)));
    }

    @GetMapping("/package/{packageName}")
    public ResponseEntity<ApiResponse<AppInfoDTO>> findByPackageName(@PathVariable String packageName) {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findByPackageName(packageName)));
    }

    @GetMapping("/root-related")
    public ResponseEntity<ApiResponse<List<AppInfoDTO>>> findRootRelatedApps() {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findRootRelatedApps()));
    }

    @GetMapping("/detection")
    public ResponseEntity<ApiResponse<List<AppInfoDTO>>> findDetectionApps() {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findDetectionApps()));
    }

    @GetMapping("/type/{appType}")
    public ResponseEntity<ApiResponse<List<AppInfoDTO>>> findByAppType(@PathVariable String appType) {
        return ResponseEntity.ok(ApiResponse.success(appInfoService.findByAppType(appType)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppInfoDTO>> create(@Valid @RequestBody AppInfoDTO dto) {
        return new ResponseEntity<>(ApiResponse.success("创建成功", appInfoService.create(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppInfoDTO>> update(@PathVariable Long id, @Valid @RequestBody AppInfoDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("更新成功", appInfoService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        appInfoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}
