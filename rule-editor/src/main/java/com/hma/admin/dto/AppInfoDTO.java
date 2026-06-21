package com.hma.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AppInfoDTO {

    private Long id;

    @NotBlank(message = "包名不能为空")
    @Size(max = 200, message = "包名长度不能超过200")
    private String packageName;

    @NotBlank(message = "应用名称不能为空")
    @Size(max = 200, message = "应用名称长度不能超过200")
    private String appName;

    @Size(max = 50, message = "应用类型长度不能超过50")
    private String appType;

    private Boolean isRootRelated = false;

    private Boolean isDetectionApp = false;

    @Size(max = 500, message = "描述长度不能超过500")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AppInfoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getAppType() { return appType; }
    public void setAppType(String appType) { this.appType = appType; }
    public Boolean getIsRootRelated() { return isRootRelated; }
    public void setIsRootRelated(Boolean isRootRelated) { this.isRootRelated = isRootRelated; }
    public Boolean getIsDetectionApp() { return isDetectionApp; }
    public void setIsDetectionApp(Boolean isDetectionApp) { this.isDetectionApp = isDetectionApp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
