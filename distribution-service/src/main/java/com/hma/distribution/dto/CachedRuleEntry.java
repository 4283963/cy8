package com.hma.distribution.dto;

import java.time.Instant;
import java.util.List;

public class CachedRuleEntry {

    private RuleDownloadDTO data;
    private Instant createdAt;
    private boolean fresh;

    public CachedRuleEntry() {}

    public CachedRuleEntry(RuleDownloadDTO data, Instant createdAt, boolean fresh) {
        this.data = data;
        this.createdAt = createdAt;
        this.fresh = fresh;
    }

    public RuleDownloadDTO getData() { return data; }
    public void setData(RuleDownloadDTO data) { this.data = data; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isFresh() { return fresh; }
    public void setFresh(boolean fresh) { this.fresh = fresh; }
}
