package com.hma.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HmaRuleCreateDTO {

    @NotNull(message = "手机型号ID不能为空")
    private Long phoneModelId;

    @NotNull(message = "应用信息ID不能为空")
    private Long appInfoId;

    @NotBlank(message = "规则类型不能为空")
    @Size(max = 30, message = "规则类型长度不能超过30")
    private String ruleType = "BLACKLIST";

    private Boolean isEnabled = true;

    @Size(max = 50, message = "作用域长度不能超过50")
    private String scope = "ALL";

    private Integer priority = 0;

    @Size(max = 500, message = "备注长度不能超过500")
    private String notes;

    public HmaRuleCreateDTO() {}

    public Long getPhoneModelId() { return phoneModelId; }
    public void setPhoneModelId(Long phoneModelId) { this.phoneModelId = phoneModelId; }
    public Long getAppInfoId() { return appInfoId; }
    public void setAppInfoId(Long appInfoId) { this.appInfoId = appInfoId; }
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
}
