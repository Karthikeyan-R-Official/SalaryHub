package com.salaryhub.repository;

import com.salaryhub.entity.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRecordRepository
        extends JpaRepository<SalaryRecord, Long> {

    List<SalaryRecord> findByEmployee_IdOrderByEffectiveDateDescIdDesc(
            Long employeeId
    );

    boolean existsByEmployee_IdAndEffectiveDate(
            Long employeeId,
            java.time.LocalDate effectiveDate
    );
}