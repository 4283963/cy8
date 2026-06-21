package com.hma.distribution.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RuleDownloadDTO {

    private String modelCode;
    private String modelName;
    private String brand;
    private LocalDateTime updateTime;
    private int totalRules;
    private int enabledRules;
    private List<RuleItem> rules;

    public RuleDownloadDTO() {}

    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public int getTotalRules() { return totalRules; }
    public void setTotalRules(int totalRules) { this.totalRules = totalRules; }
    public int getEnabledRules() { return enabledRules; }
    public void setEnabledRules(int enabledRules) { this.enabledRules = enabledRules; }
    public List<RuleItem> getRules() { return rules; }
    public void setRules(List<RuleItem> rules) { this.rules = rules; }

    public static class RuleItem {
        private String packageName;
        private String appName;
        private String ruleType;
        private String scope;
        private int priority;

        public RuleItem() {}

        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
        public String getAppName() { return appName; }
        public void setAppName(String appName) { this.appName = appName; }
        public String getRuleType() { return ruleType; }
        public void setRuleType(String ruleType) { this.ruleType = ruleType; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }
}
