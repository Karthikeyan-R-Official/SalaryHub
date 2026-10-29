package com.salaryhub.controller;

import com.salaryhub.dto.SalaryRequest;
import com.salaryhub.dto.SalaryResponse;
import com.salaryhub.service.SalaryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/{employeeId}/salaries")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaryResponse createSalary(
            @PathVariable Long employeeId,
            @Valid @RequestBody SalaryRequest request
    ) {
        return salaryService.createSalary(employeeId, request);
    }

    @GetMapping
    public List<SalaryResponse> getSalaryHistory(
            @PathVariable Long employeeId
    ) {
        return salaryService.getSalaryHistory(employeeId);
    }
}