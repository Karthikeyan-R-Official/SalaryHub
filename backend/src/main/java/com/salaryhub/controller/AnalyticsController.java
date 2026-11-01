package com.salaryhub.controller;

import com.salaryhub.dto.AnalyticsOverviewResponse;
import com.salaryhub.dto.SalaryBreakdownResponse;
import com.salaryhub.service.AnalyticsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overview")
    public AnalyticsOverviewResponse getOverview() {
        return analyticsService.getOverview();
    }

    @GetMapping("/salaries/by-country")
    public List<SalaryBreakdownResponse> getSalaryBreakdownByCountry() {
        return analyticsService.getSalaryBreakdownByCountry();
    }

    @GetMapping("/salaries/by-department")
    public List<SalaryBreakdownResponse> getSalaryBreakdownByDepartment() {
        return analyticsService.getSalaryBreakdownByDepartment();
    }
}