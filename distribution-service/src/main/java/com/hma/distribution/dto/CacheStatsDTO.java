package com.hma.distribution.dto;

public class CacheStatsDTO {

    private long estimatedSize;
    private long hitCount;
    private long missCount;
    private double hitRate;
    private long requestCount;
    private long loadSuccessCount;
    private long loadFailureCount;
    private long evictionCount;

    public CacheStatsDTO() {}

    public long getEstimatedSize() { return estimatedSize; }
    public void setEstimatedSize(long estimatedSize) { this.estimatedSize = estimatedSize; }
    public long getHitCount() { return hitCount; }
    public void setHitCount(long hitCount) { this.hitCount = hitCount; }
    public long getMissCount() { return missCount; }
    public void setMissCount(long missCount) { this.missCount = missCount; }
    public double getHitRate() { return hitRate; }
    public void setHitRate(double hitRate) { this.hitRate = hitRate; }
    public long getRequestCount() { return requestCount; }
    public void setRequestCount(long requestCount) { this.requestCount = requestCount; }
    public long getLoadSuccessCount() { return loadSuccessCount; }
    public void setLoadSuccessCount(long loadSuccessCount) { this.loadSuccessCount = loadSuccessCount; }
    public long getLoadFailureCount() { return loadFailureCount; }
    public void setLoadFailureCount(long loadFailureCount) { this.loadFailureCount = loadFailureCount; }
    public long getEvictionCount() { return evictionCount; }
    public void setEvictionCount(long evictionCount) { this.evictionCount = evictionCount; }
}
