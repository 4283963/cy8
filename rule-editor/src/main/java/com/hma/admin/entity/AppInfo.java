package com.hma.admin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_info", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"package_name"})
})
public class AppInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_name", nullable = false, length = 200)
    private String packageName;

    @Column(name = "app_name", nullable = false, length = 200)
    private String appName;

    @Column(name = "app_type", length = 50)
    private String appType;

    @Column(name = "is_root_related", nullable = false)
    private Boolean isRootRelated = false;

    @Column(name = "is_detection_app", nullable = false)
    private Boolean isDetectionApp = false;

    @Column(name = "description", length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AppInfo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getAppType() { return appType; }
    public void setAppType(String appType) { this.appType = appType; }
    public Boolean getIsRootRelated() { return isRootRelated; }
    public void setIsRootRelated(Boolean isRootRelated) { this.isRootRelated = isRootRelated; }
    public Boolean getIsDetectionApp() { return isDetectionApp; }
    public void setIsDetectionApp(Boolean isDetectionApp) { this.isDetectionApp = isDetectionApp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
