package com.hma.distribution.service;

import com.hma.admin.entity.DailyAccessStat;
import com.hma.admin.entity.PhoneModel;
import com.hma.admin.repository.DailyAccessStatRepository;
import com.hma.admin.repository.PhoneModelRepository;
import com.hma.distribution.config.MonitorProperties;
import com.hma.distribution.dto.DailyAccessStatDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccessMonitorService {

    private static final Logger log = LoggerFactory.getLogger(AccessMonitorService.class);

    private final DailyAccessStatRepository statRepository;
    private final PhoneModelRepository phoneModelRepository;
    private final MonitorProperties monitorProperties;
    private final AsyncTaskExecutor accessStatExecutor;

    public AccessMonitorService(DailyAccessStatRepository statRepository,
                                PhoneModelRepository phoneModelRepository,
                                MonitorProperties monitorProperties,
                                @Qualifier("accessStatExecutor") AsyncTaskExecutor accessStatExecutor) {
        this.statRepository = statRepository;
        this.phoneModelRepository = phoneModelRepository;
        this.monitorProperties = monitorProperties;
        this.accessStatExecutor = accessStatExecutor;
    }

    public void recordSuccessAsync(String modelCode) {
        accessStatExecutor.execute(() -> {
            try {
                incrementAndSave(modelCode, true, null);
            } catch (Exception ex) {
                log.warn("Failed to record success stat for model {}", modelCode, ex);
            }
        });
    }

    public void recordFailureAsync(String modelCode, String errorMessage) {
        accessStatExecutor.execute(() -> {
            try {
                incrementAndSave(modelCode, false, errorMessage);
            } catch (Exception ex) {
                log.warn("Failed to record failure stat for model {}", modelCode, ex);
            }
        });
    }

    @Transactional
    public void incrementAndSave(String modelCode, boolean success, String errorMessage) {
        LocalDate today = LocalDate.now();
        DailyAccessStat stat = statRepository.findByStatDateAndModelCode(today, modelCode)
                .orElseGet(() -> {
                    DailyAccessStat s = new DailyAccessStat();
                    s.setStatDate(today);
                    s.setModelCode(modelCode);
                    resolveModelName(s, modelCode);
                    s.setTotalCount(0L);
                    s.setSuccessCount(0L);
                    s.setFailureCount(0L);
                    s.setAlerted(false);
                    return s;
                });

        stat.setTotalCount(stat.getTotalCount() + 1);
        if (success) {
            stat.setSuccessCount(stat.getSuccessCount() + 1);
        } else {
            stat.setFailureCount(stat.getFailureCount() + 1);
            stat.setLastError(truncate(errorMessage, 1000));
        }
        stat.setLastAccessTime(LocalDateTime.now());
        if (stat.getModelName() == null || stat.getModelName().isEmpty()) {
            resolveModelName(stat, modelCode);
        }

        statRepository.save(stat);

        if (!stat.getAlerted()
                && stat.getFailureCount() >= monitorProperties.getFailureAlertThreshold()) {
            printAlert(stat);
            stat.setAlerted(true);
            statRepository.markAlerted(stat.getId());
        }
    }

    private void resolveModelName(DailyAccessStat stat, String modelCode) {
        Optional<PhoneModel> pm = phoneModelRepository.findByModelCode(modelCode);
        pm.ifPresent(phoneModel -> stat.setModelName(phoneModel.getModelName()));
    }

    private void printAlert(DailyAccessStat stat) {
        String displayName = (stat.getModelName() != null && !stat.getModelName().isEmpty())
                ? String.format("[%s(%s)]", stat.getModelName(), stat.getModelCode())
                : String.format("[%s]", stat.getModelCode());

        String banner =
            "\n╔══════════════════════════════════════════════════════════════════╗\n" +
            "║  ⚠  ROOT 权限异常告警                                           ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║  手机型号: " + padRight(displayName, 50) + "║\n" +
            "║  今日失败次数: " + padRight(String.valueOf(stat.getFailureCount()), 45) + "║\n" +
            "║  阈值: " + padRight(String.valueOf(monitorProperties.getFailureAlertThreshold()), 53) + "║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║  ⚠⚠⚠  「" + padRight(displayName + " 可能 root 掉权限了，快去看看」", 48) + "║\n" +
            "╚══════════════════════════════════════════════════════════════════╝";

        log.warn(banner);
    }

    private static String padRight(String s, int n) {
        if (s == null) s = "";
        int len = s.codePointCount(0, s.length());
        if (len >= n) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.codePointCount(0, sb.length()) < n) sb.append(' ');
        return sb.toString();
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }

    public List<DailyAccessStatDTO> getTodayStats() {
        return toDTOList(statRepository.findByStatDateOrderByTotalCountDesc(LocalDate.now()));
    }

    public List<DailyAccessStatDTO> getStatsByDate(LocalDate date) {
        return toDTOList(statRepository.findByStatDateOrderByTotalCountDesc(date));
    }

    public List<DailyAccessStatDTO> getTodayAbnormalList() {
        return toDTOList(statRepository.findAbnormalByStatDate(LocalDate.now(),
                monitorProperties.getFailureAlertThreshold()));
    }

    public List<DailyAccessStatDTO> getAbnormalListByDate(LocalDate date) {
        return toDTOList(statRepository.findAbnormalByStatDate(date,
                monitorProperties.getFailureAlertThreshold()));
    }

    public DailyAccessStatDTO getTodayStatByModel(String modelCode) {
        return statRepository.findByStatDateAndModelCode(LocalDate.now(), modelCode)
                .map(this::toDTO).orElse(null);
    }

    private List<DailyAccessStatDTO> toDTOList(List<DailyAccessStat> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DailyAccessStatDTO toDTO(DailyAccessStat s) {
        DailyAccessStatDTO dto = new DailyAccessStatDTO();
        BeanUtils.copyProperties(s, dto);
        if (s.getTotalCount() != null && s.getTotalCount() > 0) {
            dto.setFailureRate(Math.round(s.getFailureCount() * 10000.0 / s.getTotalCount()) / 100.0);
        } else {
            dto.setFailureRate(0.0);
        }
        return dto;
    }
}
