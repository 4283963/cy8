package com.hma.admin.service;

import com.hma.admin.dto.PhoneModelDTO;
import com.hma.admin.entity.PhoneModel;
import com.hma.admin.repository.PhoneModelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneModelService {

    private final PhoneModelRepository phoneModelRepository;

    public PhoneModelService(PhoneModelRepository phoneModelRepository) {
        this.phoneModelRepository = phoneModelRepository;
    }

    public List<PhoneModelDTO> findAll() {
        return phoneModelRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PhoneModelDTO> findActive() {
        return phoneModelRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PhoneModelDTO findById(Long id) {
        return phoneModelRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found with id: " + id));
    }

    public PhoneModelDTO findByModelCode(String modelCode) {
        return phoneModelRepository.findByModelCode(modelCode)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found with code: " + modelCode));
    }

    public List<PhoneModelDTO> findByBrand(String brand) {
        return phoneModelRepository.findByBrandIgnoreCase(brand).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PhoneModelDTO create(PhoneModelDTO dto) {
        if (phoneModelRepository.existsByModelCode(dto.getModelCode())) {
            throw new IllegalArgumentException("Model code already exists: " + dto.getModelCode());
        }
        PhoneModel entity = convertToEntity(dto);
        entity.setId(null);
        return convertToDTO(phoneModelRepository.save(entity));
    }

    @Transactional
    public PhoneModelDTO update(Long id, PhoneModelDTO dto) {
        PhoneModel existing = phoneModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phone model not found with id: " + id));
        if (!existing.getModelCode().equals(dto.getModelCode())
                && phoneModelRepository.existsByModelCode(dto.getModelCode())) {
            throw new IllegalArgumentException("Model code already exists: " + dto.getModelCode());
        }
        BeanUtils.copyProperties(dto, existing, "id", "createdAt", "updatedAt");
        return convertToDTO(phoneModelRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!phoneModelRepository.existsById(id)) {
            throw new EntityNotFoundException("Phone model not found with id: " + id);
        }
        phoneModelRepository.deleteById(id);
    }

    private PhoneModelDTO convertToDTO(PhoneModel entity) {
        PhoneModelDTO dto = new PhoneModelDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private PhoneModel convertToEntity(PhoneModelDTO dto) {
        PhoneModel entity = new PhoneModel();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
