package com.wetter_dashboard.api.service;

import com.wetter_dashboard.api.dto.WeatherMetricDto;
import com.wetter_dashboard.api.exception.ResourceNotFoundException;
import com.wetter_dashboard.api.model.Location;
import com.wetter_dashboard.api.model.WeatherMetric;
import com.wetter_dashboard.api.repository.LocationRepository;
import com.wetter_dashboard.api.repository.WeatherMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherMetricService {

    private final WeatherMetricRepository weatherMetricRepository;
    private final LocationRepository locationRepository;

    public List<WeatherMetricDto> getAllWeatherMetrics() {
        return weatherMetricRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WeatherMetricDto getWeatherMetricById(Integer id) {
        WeatherMetric weatherMetric = weatherMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WeatherMetric not found with id: " + id));
        return mapToDto(weatherMetric);
    }

    public WeatherMetricDto createWeatherMetric(WeatherMetricDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        WeatherMetric weatherMetric = WeatherMetric.builder()
                .location(location)
                .temperature(dto.getTemperature())
                .weatherCondition(dto.getWeatherCondition())
                .humidityPercent(dto.getHumidityPercent())
                .windSpeedKmh(dto.getWindSpeedKmh())
                .pressureHpa(dto.getPressureHpa())
                .cloudCoverPercent(dto.getCloudCoverPercent())
                .precipitationMm(dto.getPrecipitationMm())
                .airQualityStatus(dto.getAirQualityStatus())
                .build();

        WeatherMetric savedWeatherMetric = weatherMetricRepository.save(weatherMetric);
        return mapToDto(savedWeatherMetric);
    }

    public void deleteWeatherMetric(Integer id) {
        WeatherMetric weatherMetric = weatherMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WeatherMetric not found with id: " + id));
        weatherMetricRepository.delete(weatherMetric);
    }

    private WeatherMetricDto mapToDto(WeatherMetric weatherMetric) {
        return WeatherMetricDto.builder()
                .id(weatherMetric.getId())
                .locationId(weatherMetric.getLocation().getId())
                .recordedAt(weatherMetric.getRecordedAt())
                .temperature(weatherMetric.getTemperature())
                .weatherCondition(weatherMetric.getWeatherCondition())
                .humidityPercent(weatherMetric.getHumidityPercent())
                .windSpeedKmh(weatherMetric.getWindSpeedKmh())
                .pressureHpa(weatherMetric.getPressureHpa())
                .cloudCoverPercent(weatherMetric.getCloudCoverPercent())
                .precipitationMm(weatherMetric.getPrecipitationMm())
                .airQualityStatus(weatherMetric.getAirQualityStatus())
                .build();
    }
}
