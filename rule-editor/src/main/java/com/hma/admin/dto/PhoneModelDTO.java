package com.hma.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PhoneModelDTO {

    private Long id;

    @NotBlank(message = "品牌不能为空")
    @Size(max = 50, message = "品牌长度不能超过50")
    private String brand;

    @NotBlank(message = "型号名称不能为空")
    @Size(max = 100, message = "型号名称长度不能超过100")
    private String modelName;

    @NotBlank(message = "型号代码不能为空")
    @Size(max = 50, message = "型号代码长度不能超过50")
    private String modelCode;

    @Size(max = 20, message = "Android版本长度不能超过20")
    private String androidVersion;

    @Size(max = 20, message = "MIUI版本长度不能超过20")
    private String miuiVersion;

    @Size(max = 500, message = "描述长度不能超过500")
    private String description;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PhoneModelDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getAndroidVersion() { return androidVersion; }
    public void setAndroidVersion(String androidVersion) { this.androidVersion = androidVersion; }
    public String getMiuiVersion() { return miuiVersion; }
    public void setMiuiVersion(String miuiVersion) { this.miuiVersion = miuiVersion; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
