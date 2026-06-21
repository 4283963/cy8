package com.hma.admin.repository;

import com.hma.admin.entity.HmaRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HmaRuleRepository extends JpaRepository<HmaRule, Long> {

    @Query("SELECT r FROM HmaRule r JOIN FETCH r.phoneModel JOIN FETCH r.appInfo WHERE r.phoneModel.id = :phoneModelId AND r.isEnabled = true")
    List<HmaRule> findEnabledRulesByPhoneModelId(@Param("phoneModelId") Long phoneModelId);

    @Query("SELECT r FROM HmaRule r JOIN FETCH r.phoneModel pm JOIN FETCH r.appInfo WHERE pm.modelCode = :modelCode AND r.isEnabled = true AND pm.isActive = true")
    List<HmaRule> findEnabledRulesByModelCode(@Param("modelCode") String modelCode);

    List<HmaRule> findByPhoneModelId(Long phoneModelId);

    List<HmaRule> findByAppInfoId(Long appInfoId);

    boolean existsByPhoneModelIdAndAppInfoId(Long phoneModelId, Long appInfoId);

    void deleteByPhoneModelIdAndAppInfoId(Long phoneModelId, Long appInfoId);
}
