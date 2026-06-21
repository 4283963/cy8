package com.hma.admin.service;

import com.hma.admin.dto.AppInfoDTO;
import com.hma.admin.entity.AppInfo;
import com.hma.admin.repository.AppInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppInfoService {

    private final AppInfoRepository appInfoRepository;

    public AppInfoService(AppInfoRepository appInfoRepository) {
        this.appInfoRepository = appInfoRepository;
    }

    public List<AppInfoDTO> findAll() {
        return appInfoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AppInfoDTO findById(Long id) {
        return appInfoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("App info not found with id: " + id));
    }

    public AppInfoDTO findByPackageName(String packageName) {
        return appInfoRepository.findByPackageName(packageName)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("App info not found with package: " + packageName));
    }

    public List<AppInfoDTO> findRootRelatedApps() {
        return appInfoRepository.findByIsRootRelatedTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppInfoDTO> findDetectionApps() {
        return appInfoRepository.findByIsDetectionAppTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppInfoDTO> findByAppType(String appType) {
        return appInfoRepository.findByAppTypeIgnoreCase(appType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppInfoDTO create(AppInfoDTO dto) {
        if (appInfoRepository.existsByPackageName(dto.getPackageName())) {
            throw new IllegalArgumentException("Package name already exists: " + dto.getPackageName());
        }
        AppInfo entity = convertToEntity(dto);
        entity.setId(null);
        return convertToDTO(appInfoRepository.save(entity));
    }

    @Transactional
    public AppInfoDTO update(Long id, AppInfoDTO dto) {
        AppInfo existing = appInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App info not found with id: " + id));
        if (!existing.getPackageName().equals(dto.getPackageName())
                && appInfoRepository.existsByPackageName(dto.getPackageName())) {
            throw new IllegalArgumentException("Package name already exists: " + dto.getPackageName());
        }
        BeanUtils.copyProperties(dto, existing, "id", "createdAt", "updatedAt");
        return convertToDTO(appInfoRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!appInfoRepository.existsById(id)) {
            throw new EntityNotFoundException("App info not found with id: " + id);
        }
        appInfoRepository.deleteById(id);
    }

    private AppInfoDTO convertToDTO(AppInfo entity) {
        AppInfoDTO dto = new AppInfoDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private AppInfo convertToEntity(AppInfoDTO dto) {
        AppInfo entity = new AppInfo();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
