package com.hma.admin.repository;

import com.hma.admin.entity.DailyAccessStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAccessStatRepository extends JpaRepository<DailyAccessStat, Long> {

    Optional<DailyAccessStat> findByStatDateAndModelCode(LocalDate statDate, String modelCode);

    List<DailyAccessStat> findByStatDate(LocalDate statDate);

    @Query("SELECT s FROM DailyAccessStat s WHERE s.statDate = :statDate AND s.failureCount >= :threshold ORDER BY s.failureCount DESC")
    List<DailyAccessStat> findAbnormalByStatDate(@Param("statDate") LocalDate statDate,
                                                  @Param("threshold") Long threshold);

    @Query("SELECT s FROM DailyAccessStat s WHERE s.statDate = :statDate ORDER BY s.totalCount DESC")
    List<DailyAccessStat> findByStatDateOrderByTotalCountDesc(@Param("statDate") LocalDate statDate);

    @Modifying
    @Query("UPDATE DailyAccessStat s SET s.alerted = true WHERE s.id = :id")
    void markAlerted(@Param("id") Long id);
}
