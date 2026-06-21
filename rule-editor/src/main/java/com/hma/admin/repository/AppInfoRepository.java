package com.hma.admin.repository;

import com.hma.admin.entity.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppInfoRepository extends JpaRepository<AppInfo, Long> {

    Optional<AppInfo> findByPackageName(String packageName);

    List<AppInfo> findByIsRootRelatedTrue();

    List<AppInfo> findByIsDetectionAppTrue();

    List<AppInfo> findByAppTypeIgnoreCase(String appType);

    boolean existsByPackageName(String packageName);
}
