package com.hma.distribution.config;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "hma.cache")
public class CacheProperties {

    private Rules rules = new Rules();

    public Rules getRules() { return rules; }
    public void setRules(Rules rules) { this.rules = rules; }

    public static class Rules {
        @Min(1)
        private long maxSize = 1000;

        private Duration expireAfterWrite = Duration.ofMinutes(30);

        private Duration refreshAfterWrite = Duration.ofMinutes(5);

        public long getMaxSize() { return maxSize; }
        public void setMaxSize(long maxSize) { this.maxSize = maxSize; }
        public Duration getExpireAfterWrite() { return expireAfterWrite; }
        public void setExpireAfterWrite(Duration expireAfterWrite) { this.expireAfterWrite = expireAfterWrite; }
        public Duration getRefreshAfterWrite() { return refreshAfterWrite; }
        public void setRefreshAfterWrite(Duration refreshAfterWrite) { this.refreshAfterWrite = refreshAfterWrite; }
    }
}
