package com.salaryhub.repository;

import com.salaryhub.entity.SalaryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @EntityGraph(attributePaths = "employee")
    @Query("""
        SELECT s
        FROM SalaryRecord s
        JOIN s.employee e
        WHERE (
            :search IS NULL OR :search = ''
            OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(CONCAT(e.firstName, ' ', e.lastName))
                LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (:employeeId IS NULL OR e.id = :employeeId)
        AND (
            :country IS NULL OR :country = ''
            OR LOWER(e.country) = LOWER(:country)
        )
        AND (
            :department IS NULL OR :department = ''
            OR LOWER(e.department) = LOWER(:department)
        )
        AND (
            :currency IS NULL OR :currency = ''
            OR UPPER(s.currency) = UPPER(:currency)
        )
        """)
    Page<SalaryRecord> searchSalaryRecords(
            @Param("search") String search,
            @Param("employeeId") Long employeeId,
            @Param("country") String country,
            @Param("department") String department,
            @Param("currency") String currency,
            Pageable pageable
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