package com.hma.distribution.dto;

import java.util.List;

public class HmaConfigDTO {

    private String version;
    private String generatedAt;
    private String modelCode;
    private String modelName;
    private String brand;
    private Config config;

    public HmaConfigDTO() {}

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Config getConfig() { return config; }
    public void setConfig(Config config) { this.config = config; }

    public static class Config {
        private String mode;
        private List<String> blacklist;
        private List<String> whitelist;
        private List<String> templateApps;
        private boolean hideSystem;
        private boolean useRegex;
        private List<String> extraRules;

        public Config() {}

        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public List<String> getBlacklist() { return blacklist; }
        public void setBlacklist(List<String> blacklist) { this.blacklist = blacklist; }
        public List<String> getWhitelist() { return whitelist; }
        public void setWhitelist(List<String> whitelist) { this.whitelist = whitelist; }
        public List<String> getTemplateApps() { return templateApps; }
        public void setTemplateApps(List<String> templateApps) { this.templateApps = templateApps; }
        public boolean isHideSystem() { return hideSystem; }
        public void setHideSystem(boolean hideSystem) { this.hideSystem = hideSystem; }
        public boolean isUseRegex() { return useRegex; }
        public void setUseRegex(boolean useRegex) { this.useRegex = useRegex; }
        public List<String> getExtraRules() { return extraRules; }
        public void setExtraRules(List<String> extraRules) { this.extraRules = extraRules; }
    }
}
