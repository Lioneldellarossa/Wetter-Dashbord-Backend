package com.wetter_dashboard.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventilation_cycles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentilationCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @CreationTimestamp
    @Column(name = "calculated_at", updatable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "ventilation_duration_minutes", nullable = false)
    private Integer ventilationDurationMinutes;

    @Column(name = "closed_duration_hours", nullable = false, precision = 4, scale = 1)
    private BigDecimal closedDurationHours;

    @Column(name = "next_cycle_target", nullable = false)
    private LocalDateTime nextCycleTarget;

    @Column(name = "system_status", length = 50)
    @Builder.Default
    private String systemStatus = "Warten";

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
