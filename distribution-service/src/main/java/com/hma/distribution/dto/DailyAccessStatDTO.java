package com.hma.distribution.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyAccessStatDTO {

    private Long id;
    private LocalDate statDate;
    private String modelCode;
    private String modelName;
    private Long totalCount;
    private Long successCount;
    private Long failureCount;
    private Double failureRate;
    private String lastError;
    private LocalDateTime lastAccessTime;
    private Boolean alerted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DailyAccessStatDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getStatDate() { return statDate; }
    public void setStatDate(LocalDate statDate) { this.statDate = statDate; }
    public String getModelCode() { return modelCode; }
    public void setModelCode(String modelCode) { this.modelCode = modelCode; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
    public Long getSuccessCount() { return successCount; }
    public void setSuccessCount(Long successCount) { this.successCount = successCount; }
    public Long getFailureCount() { return failureCount; }
    public void setFailureCount(Long failureCount) { this.failureCount = failureCount; }
    public Double getFailureRate() { return failureRate; }
    public void setFailureRate(Double failureRate) { this.failureRate = failureRate; }
    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }
    public LocalDateTime getLastAccessTime() { return lastAccessTime; }
    public void setLastAccessTime(LocalDateTime lastAccessTime) { this.lastAccessTime = lastAccessTime; }
    public Boolean getAlerted() { return alerted; }
    public void setAlerted(Boolean alerted) { this.alerted = alerted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
