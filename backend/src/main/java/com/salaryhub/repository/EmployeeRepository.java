package com.salaryhub.repository;

import com.salaryhub.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Employee> findById(Long id);

    @Query("""
        SELECT e FROM Employee e
        WHERE (
            :search IS NULL OR :search = ''
            OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (
            :country IS NULL OR :country = ''
            OR LOWER(e.country) = LOWER(:country)
        )
        AND (
            :department IS NULL OR :department = ''
            OR LOWER(e.department) = LOWER(:department)
        )
        ORDER BY e.lastName ASC, e.id ASC
        """)
    Page<Employee> searchEmployees(
            @Param("search") String search,
            @Param("country") String country,
            @Param("department") String department,
            Pageable pageable
    );
}