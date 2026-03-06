package com.wetter_dashboard.api.service;

import com.wetter_dashboard.api.dto.DashboardDto;
import com.wetter_dashboard.api.exception.ResourceNotFoundException;
import com.wetter_dashboard.api.model.Location;
import com.wetter_dashboard.api.model.VentilationCycle;
import com.wetter_dashboard.api.model.WeatherMetric;
import com.wetter_dashboard.api.repository.LocationRepository;
import com.wetter_dashboard.api.repository.VentilationCycleRepository;
import com.wetter_dashboard.api.repository.WeatherMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final LocationRepository locationRepository;
    private final WeatherMetricRepository weatherMetricRepository;
    private final VentilationCycleRepository ventilationCycleRepository;

    public DashboardDto getDashboardDataForLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));

        Optional<WeatherMetric> latestWeather = weatherMetricRepository.findFirstByLocationIdOrderByRecordedAtDesc(locationId);
        Optional<VentilationCycle> activeCycle = ventilationCycleRepository.findFirstByLocationIdAndIsActiveTrueOrderByCalculatedAtDesc(locationId);

        DashboardDto.DashboardDtoBuilder builder = DashboardDto.builder()
                .locationId(location.getId())
                .locationName(location.getName());

        latestWeather.ifPresent(weather -> {
            builder.temperature(weather.getTemperature())
                    .weatherCondition(weather.getWeatherCondition())
                    .humidityPercent(weather.getHumidityPercent())
                    .windSpeedKmh(weather.getWindSpeedKmh())
                    .pressureHpa(weather.getPressureHpa())
                    .cloudCoverPercent(weather.getCloudCoverPercent())
                    .precipitationMm(weather.getPrecipitationMm())
                    .airQualityStatus(weather.getAirQualityStatus());
        });

        activeCycle.ifPresent(cycle -> {
            builder.ventilationDurationMinutes(cycle.getVentilationDurationMinutes())
                    .closedDurationHours(cycle.getClosedDurationHours())
                    .nextCycleTarget(cycle.getNextCycleTarget())
                    .systemStatus(cycle.getSystemStatus());
        });

        return builder.build();
    }
}
