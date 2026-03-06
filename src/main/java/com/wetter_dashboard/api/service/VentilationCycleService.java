package com.wetter_dashboard.api.service;

import com.wetter_dashboard.api.dto.VentilationCycleDto;
import com.wetter_dashboard.api.exception.ResourceNotFoundException;
import com.wetter_dashboard.api.model.Location;
import com.wetter_dashboard.api.model.VentilationCycle;
import com.wetter_dashboard.api.repository.LocationRepository;
import com.wetter_dashboard.api.repository.VentilationCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentilationCycleService {

    private final VentilationCycleRepository ventilationCycleRepository;
    private final LocationRepository locationRepository;

    public List<VentilationCycleDto> getAllVentilationCycles() {
        return ventilationCycleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public VentilationCycleDto getVentilationCycleById(Integer id) {
        VentilationCycle cycle = ventilationCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VentilationCycle not found with id: " + id));
        return mapToDto(cycle);
    }

    public VentilationCycleDto createVentilationCycle(VentilationCycleDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        // When a new cycle is created, we usually mark previous active ones as inactive
        ventilationCycleRepository.findFirstByLocationIdAndIsActiveTrueOrderByCalculatedAtDesc(location.getId())
                .ifPresent(activeCycle -> {
                    activeCycle.setIsActive(false);
                    ventilationCycleRepository.save(activeCycle);
                });

        VentilationCycle cycle = VentilationCycle.builder()
                .location(location)
                .ventilationDurationMinutes(dto.getVentilationDurationMinutes())
                .closedDurationHours(dto.getClosedDurationHours())
                .nextCycleTarget(dto.getNextCycleTarget())
                .systemStatus(dto.getSystemStatus() != null ? dto.getSystemStatus() : "Warten")
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        VentilationCycle savedCycle = ventilationCycleRepository.save(cycle);
        return mapToDto(savedCycle);
    }

    public VentilationCycleDto updateVentilationCycleStatus(Integer id, String systemStatus) {
        VentilationCycle cycle = ventilationCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VentilationCycle not found with id: " + id));
        cycle.setSystemStatus(systemStatus);
        VentilationCycle updatedCycle = ventilationCycleRepository.save(cycle);
        return mapToDto(updatedCycle);
    }


    public void deleteVentilationCycle(Integer id) {
        VentilationCycle cycle = ventilationCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VentilationCycle not found with id: " + id));
        ventilationCycleRepository.delete(cycle);
    }

    private VentilationCycleDto mapToDto(VentilationCycle cycle) {
        return VentilationCycleDto.builder()
                .id(cycle.getId())
                .locationId(cycle.getLocation().getId())
                .calculatedAt(cycle.getCalculatedAt())
                .ventilationDurationMinutes(cycle.getVentilationDurationMinutes())
                .closedDurationHours(cycle.getClosedDurationHours())
                .nextCycleTarget(cycle.getNextCycleTarget())
                .systemStatus(cycle.getSystemStatus())
                .isActive(cycle.getIsActive())
                .build();
    }
}
