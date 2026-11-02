package com.salaryhub.controller;

import com.salaryhub.dto.SalaryRecordListResponse;
import com.salaryhub.service.SalaryService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salaries")
public class GlobalSalaryController {

    private final SalaryService salaryService;

    public GlobalSalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping
    public Page<SalaryRecordListResponse> getAllSalaryRecords(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String currency,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return salaryService.getAllSalaryRecords(
                search,
                country,
                department,
                currency,
                page,
                size
        );
    }
}