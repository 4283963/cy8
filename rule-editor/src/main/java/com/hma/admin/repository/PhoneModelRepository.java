package com.hma.admin.repository;

import com.hma.admin.entity.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, Long> {

    Optional<PhoneModel> findByModelCode(String modelCode);

    List<PhoneModel> findByIsActiveTrue();

    List<PhoneModel> findByBrandIgnoreCase(String brand);

    boolean existsByModelCode(String modelCode);
}
