package com.salaryhub.dto;

public record AnalyticsOverviewResponse(
        long totalEmployees,
        long activeEmployees,
        long inactiveEmployees,
        long totalSalaryRecords
) {
}