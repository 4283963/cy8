package com.hma.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class HmaRuleDTO {

    private Long id;

    @NotNull(message = "手机型号ID不能为空")
    private Long phoneModelId;

    private String phoneModelName;

    private String modelCode;

    @NotNull(message = "应用信息ID不能为空")
    private Long appInfoId;

    private String packageName;

    private String appName;

    @NotBlank(message = "规则类型不能为空")
    @Size(max = 30, message = "规则类型长度不能超过30")
    private String ruleType = "BLACKLIST";

    private Boolean isEnabled = true;

    @Size(max = 50, message = "作用域长度不能超过50")
    private String scope = "ALL";

    private Integer priority = 0;

    @Size(max = 500, message = "备注长度不能超过500")
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public HmaRuleDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPhoneModelId() { return phoneModelId; }
    public void setPhoneModelId(Long phoneModelId) { this.phoneModelId = phoneModelId; }
    public String getPhoneModelName() { return phoneModelName; }
    public void setPhoneModelName(String phoneModelName) { this.phoneModelName = phoneModelName; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public Long getAppInfoId() { return appInfoId; }
    public void setAppInfoId(Long appInfoId) { this.appInfoId = appInfoId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
