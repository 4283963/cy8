package com.hma.admin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "hma_rule", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"phone_model_id", "app_info_id"})
})
public class HmaRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phone_model_id", nullable = false)
    private PhoneModel phoneModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_info_id", nullable = false)
    private AppInfo appInfo;

    @Column(name = "rule_type", nullable = false, length = 30)
    private String ruleType = "BLACKLIST";

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "scope", length = 50)
    private String scope = "ALL";

    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public HmaRule() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PhoneModel getPhoneModel() { return phoneModel; }
    public void setPhoneModel(PhoneModel phoneModel) { this.phoneModel = phoneModel; }
    public AppInfo getAppInfo() { return appInfo; }
    public void setAppInfo(AppInfo appInfo) { this.appInfo = appInfo; }
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
