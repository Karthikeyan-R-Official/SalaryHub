package com.salaryhub.repository;

import com.salaryhub.entity.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = """
    WITH latest_salary AS (
        SELECT DISTINCT ON (employee_id)
            employee_id,
            amount,
            currency
        FROM salary_records
        ORDER BY employee_id, effective_date DESC, id DESC
    )
    SELECT
        e.country AS "groupName",
        ls.currency AS "currency",
        COUNT(*) AS "employeeCount",
        ROUND(AVG(ls.amount), 2) AS "averageSalary"
    FROM employees e
    JOIN latest_salary ls ON ls.employee_id = e.id
    GROUP BY e.country, ls.currency
    ORDER BY e.country, ls.currency
    """, nativeQuery = true)
    List<SalaryBreakdownProjection> findSalaryBreakdownByCountry();

    @Query(value = """
    WITH latest_salary AS (
        SELECT DISTINCT ON (employee_id)
            employee_id,
            amount,
            currency
        FROM salary_records
        ORDER BY employee_id, effective_date DESC, id DESC
    )
    SELECT
        e.department AS "groupName",
        ls.currency AS "currency",
        COUNT(*) AS "employeeCount",
        ROUND(AVG(ls.amount), 2) AS "averageSalary"
    FROM employees e
    JOIN latest_salary ls ON ls.employee_id = e.id
    GROUP BY e.department, ls.currency
    ORDER BY e.department, ls.currency
    """, nativeQuery = true)
    List<SalaryBreakdownProjection> findSalaryBreakdownByDepartment();
}