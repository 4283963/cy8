package com.hma.admin.controller;

import com.hma.admin.dto.ApiResponse;
import com.hma.admin.dto.PhoneModelDTO;
import com.hma.admin.service.PhoneModelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/phone-models")
public class PhoneModelController {

    private final PhoneModelService phoneModelService;

    public PhoneModelController(PhoneModelService phoneModelService) {
        this.phoneModelService = phoneModelService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhoneModelDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(phoneModelService.findAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PhoneModelDTO>>> findActive() {
        return ResponseEntity.ok(ApiResponse.success(phoneModelService.findActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhoneModelDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(phoneModelService.findById(id)));
    }

    @GetMapping("/code/{modelCode}")
    public ResponseEntity<ApiResponse<PhoneModelDTO>> findByModelCode(@PathVariable String modelCode) {
        return ResponseEntity.ok(ApiResponse.success(phoneModelService.findByModelCode(modelCode)));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse<List<PhoneModelDTO>>> findByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(ApiResponse.success(phoneModelService.findByBrand(brand)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhoneModelDTO>> create(@Valid @RequestBody PhoneModelDTO dto) {
        return new ResponseEntity<>(ApiResponse.success("创建成功", phoneModelService.create(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhoneModelDTO>> update(@PathVariable Long id, @Valid @RequestBody PhoneModelDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("更新成功", phoneModelService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        phoneModelService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}
