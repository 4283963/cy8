package com.hma.admin.service;

import com.hma.admin.dto.HmaRuleCreateDTO;
import com.hma.admin.dto.HmaRuleDTO;
import com.hma.admin.entity.AppInfo;
import com.hma.admin.entity.HmaRule;
import com.hma.admin.entity.PhoneModel;
import com.hma.admin.repository.AppInfoRepository;
import com.hma.admin.repository.HmaRuleRepository;
import com.hma.admin.repository.PhoneModelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HmaRuleService {

    private final HmaRuleRepository hmaRuleRepository;
    private final PhoneModelRepository phoneModelRepository;
    private final AppInfoRepository appInfoRepository;

    public HmaRuleService(HmaRuleRepository hmaRuleRepository,
                          PhoneModelRepository phoneModelRepository,
                          AppInfoRepository appInfoRepository) {
        this.hmaRuleRepository = hmaRuleRepository;
        this.phoneModelRepository = phoneModelRepository;
        this.appInfoRepository = appInfoRepository;
    }

    public List<HmaRuleDTO> findAll() {
        return hmaRuleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public HmaRuleDTO findById(Long id) {
        return hmaRuleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("HMA rule not found with id: " + id));
    }

    public List<HmaRuleDTO> findByPhoneModelId(Long phoneModelId) {
        return hmaRuleRepository.findEnabledRulesByPhoneModelId(phoneModelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HmaRuleDTO> findByModelCode(String modelCode) {
        return hmaRuleRepository.findEnabledRulesByModelCode(modelCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HmaRuleDTO create(HmaRuleCreateDTO dto) {
        if (hmaRuleRepository.existsByPhoneModelIdAndAppInfoId(dto.getPhoneModelId(), dto.getAppInfoId())) {
            throw new IllegalArgumentException("Rule already exists for this phone model and app");
        }
        PhoneModel phoneModel = phoneModelRepository.findById(dto.getPhoneModelId())
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found with id: " + dto.getPhoneModelId()));
        AppInfo appInfo = appInfoRepository.findById(dto.getAppInfoId())
                .orElseThrow(() -> new EntityNotFoundException("App info not found with id: " + dto.getAppInfoId()));

        HmaRule entity = new HmaRule();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(null);
        entity.setPhoneModel(phoneModel);
        entity.setAppInfo(appInfo);
        return convertToDTO(hmaRuleRepository.save(entity));
    }

    @Transactional
    public HmaRuleDTO update(Long id, HmaRuleCreateDTO dto) {
        HmaRule existing = hmaRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("HMA rule not found with id: " + id));

        if (!existing.getPhoneModel().getId().equals(dto.getPhoneModelId())
                || !existing.getAppInfo().getId().equals(dto.getAppInfoId())) {
            if (hmaRuleRepository.existsByPhoneModelIdAndAppInfoId(dto.getPhoneModelId(), dto.getAppInfoId())) {
                throw new IllegalArgumentException("Rule already exists for this phone model and app");
            }
        }

        PhoneModel phoneModel = phoneModelRepository.findById(dto.getPhoneModelId())
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found with id: " + dto.getPhoneModelId()));
        AppInfo appInfo = appInfoRepository.findById(dto.getAppInfoId())
                .orElseThrow(() -> new EntityNotFoundException("App info not found with id: " + dto.getAppInfoId()));

        BeanUtils.copyProperties(dto, existing, "id", "createdAt", "updatedAt");
        existing.setPhoneModel(phoneModel);
        existing.setAppInfo(appInfo);
        return convertToDTO(hmaRuleRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!hmaRuleRepository.existsById(id)) {
            throw new EntityNotFoundException("HMA rule not found with id: " + id);
        }
        hmaRuleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByPhoneAndApp(Long phoneModelId, Long appInfoId) {
        if (!hmaRuleRepository.existsByPhoneModelIdAndAppInfoId(phoneModelId, appInfoId)) {
            throw new EntityNotFoundException("HMA rule not found");
        }
        hmaRuleRepository.deleteByPhoneModelIdAndAppInfoId(phoneModelId, appInfoId);
    }

    private HmaRuleDTO convertToDTO(HmaRule entity) {
        HmaRuleDTO dto = new HmaRuleDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getPhoneModel() != null) {
            dto.setPhoneModelId(entity.getPhoneModel().getId());
            dto.setPhoneModelName(entity.getPhoneModel().getModelName());
            dto.setModelCode(entity.getPhoneModel().getModelCode());
        }
        if (entity.getAppInfo() != null) {
            dto.setAppInfoId(entity.getAppInfo().getId());
            dto.setPackageName(entity.getAppInfo().getPackageName());
            dto.setAppName(entity.getAppInfo().getAppName());
        }
        return dto;
    }
}
