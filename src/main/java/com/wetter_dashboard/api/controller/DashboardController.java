package com.wetter_dashboard.api.controller;

import com.wetter_dashboard.api.dto.DashboardDto;
import com.wetter_dashboard.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows calls from any frontend
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{locationId}")
    public ResponseEntity<DashboardDto> getDashboardData(@PathVariable Integer locationId) {
        return ResponseEntity.ok(dashboardService.getDashboardDataForLocation(locationId));
    }
}
