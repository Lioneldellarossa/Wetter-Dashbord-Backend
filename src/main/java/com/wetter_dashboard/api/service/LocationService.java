package com.wetter_dashboard.api.service;

import com.wetter_dashboard.api.dto.LocationDto;
import com.wetter_dashboard.api.exception.ResourceNotFoundException;
import com.wetter_dashboard.api.model.Location;
import com.wetter_dashboard.api.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public LocationDto getLocationById(Integer id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return mapToDto(location);
    }

    public LocationDto createLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setName(locationDto.getName());
        Location savedLocation = locationRepository.save(location);
        return mapToDto(savedLocation);
    }

    public LocationDto updateLocation(Integer id, LocationDto locationDto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        location.setName(locationDto.getName());
        Location updatedLocation = locationRepository.save(location);
        return mapToDto(updatedLocation);
    }

    public void deleteLocation(Integer id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        locationRepository.delete(location);
    }

    private LocationDto mapToDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .createdAt(location.getCreatedAt())
                .build();
    }
}
