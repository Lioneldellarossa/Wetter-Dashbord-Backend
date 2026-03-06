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
public class WeatherMetricDto {
    private Integer id;
    private Integer locationId;
    private LocalDateTime recordedAt;
    private BigDecimal temperature;
    private String weatherCondition;
    private Integer humidityPercent;
    private Integer windSpeedKmh;
    private Integer pressureHpa;
    private Integer cloudCoverPercent;
    private BigDecimal precipitationMm;
    private String airQualityStatus;
}
