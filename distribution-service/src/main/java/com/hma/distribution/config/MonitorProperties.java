package com.hma.distribution.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hma.monitor")
public class MonitorProperties {

    private long failureAlertThreshold = 5;

    public long getFailureAlertThreshold() { return failureAlertThreshold; }
    public void setFailureAlertThreshold(long failureAlertThreshold) { this.failureAlertThreshold = failureAlertThreshold; }
}
