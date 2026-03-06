package com.wetter_dashboard.api.controller;

import com.wetter_dashboard.api.dto.LocationDto;
import com.wetter_dashboard.api.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Integer id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        return new ResponseEntity<>(locationService.createLocation(locationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Integer id, @RequestBody LocationDto locationDto) {
        return ResponseEntity.ok(locationService.updateLocation(id, locationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
