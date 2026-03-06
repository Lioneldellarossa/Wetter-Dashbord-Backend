package com.wetter_dashboard.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "weather_condition", length = 50)
    private String weatherCondition;

    @Column(name = "humidity_percent")
    private Integer humidityPercent;

    @Column(name = "wind_speed_kmh")
    private Integer windSpeedKmh;

    @Column(name = "pressure_hpa")
    private Integer pressureHpa;

    @Column(name = "cloud_cover_percent")
    private Integer cloudCoverPercent;

    @Column(name = "precipitation_mm", precision = 5, scale = 2)
    private BigDecimal precipitationMm;

    @Column(name = "air_quality_status", length = 100)
    private String airQualityStatus;
}
