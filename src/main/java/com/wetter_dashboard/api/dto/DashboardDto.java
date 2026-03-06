package com.wetter_dashboard.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private Integer locationId;
    private String locationName;
    private BigDecimal temperature;
    private String weatherCondition;
    private Integer humidityPercent;
    private Integer windSpeedKmh;
    private Integer pressureHpa;
    private Integer cloudCoverPercent;
    private BigDecimal precipitationMm;
    private String airQualityStatus;
    private Integer ventilationDurationMinutes;
    private BigDecimal closedDurationHours;
    private LocalDateTime nextCycleTarget;
    private String systemStatus;
}
