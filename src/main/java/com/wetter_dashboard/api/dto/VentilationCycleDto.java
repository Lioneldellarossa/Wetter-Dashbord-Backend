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
public class VentilationCycleDto {
    private Integer id;
    private Integer locationId;
    private LocalDateTime calculatedAt;
    private Integer ventilationDurationMinutes;
    private BigDecimal closedDurationHours;
    private LocalDateTime nextCycleTarget;
    private String systemStatus;
    private Boolean isActive;
}
