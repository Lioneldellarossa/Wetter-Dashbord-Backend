package com.wetter_dashboard.api.controller;

import com.wetter_dashboard.api.dto.WeatherMetricDto;
import com.wetter_dashboard.api.service.WeatherMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather-metrics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WeatherMetricController {

    private final WeatherMetricService weatherMetricService;

    @GetMapping
    public ResponseEntity<List<WeatherMetricDto>> getAllWeatherMetrics() {
        return ResponseEntity.ok(weatherMetricService.getAllWeatherMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherMetricDto> getWeatherMetricById(@PathVariable Integer id) {
        return ResponseEntity.ok(weatherMetricService.getWeatherMetricById(id));
    }

    @PostMapping
    public ResponseEntity<WeatherMetricDto> createWeatherMetric(@RequestBody WeatherMetricDto weatherMetricDto) {
        return new ResponseEntity<>(weatherMetricService.createWeatherMetric(weatherMetricDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeatherMetric(@PathVariable Integer id) {
        weatherMetricService.deleteWeatherMetric(id);
        return ResponseEntity.noContent().build();
    }
}
