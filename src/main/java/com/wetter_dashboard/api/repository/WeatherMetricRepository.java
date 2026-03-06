package com.wetter_dashboard.api.repository;

import com.wetter_dashboard.api.model.WeatherMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherMetricRepository extends JpaRepository<WeatherMetric, Integer> {
    Optional<WeatherMetric> findFirstByLocationIdOrderByRecordedAtDesc(Integer locationId);
}
