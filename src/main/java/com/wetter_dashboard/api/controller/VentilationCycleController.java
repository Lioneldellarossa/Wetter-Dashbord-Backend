package com.wetter_dashboard.api.controller;

import com.wetter_dashboard.api.dto.VentilationCycleDto;
import com.wetter_dashboard.api.service.VentilationCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventilation-cycles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentilationCycleController {

    private final VentilationCycleService ventilationCycleService;

    @GetMapping
    public ResponseEntity<List<VentilationCycleDto>> getAllVentilationCycles() {
        return ResponseEntity.ok(ventilationCycleService.getAllVentilationCycles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentilationCycleDto> getVentilationCycleById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventilationCycleService.getVentilationCycleById(id));
    }

    @PostMapping
    public ResponseEntity<VentilationCycleDto> createVentilationCycle(@RequestBody VentilationCycleDto dto) {
        return new ResponseEntity<>(ventilationCycleService.createVentilationCycle(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VentilationCycleDto> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("systemStatus");
        return ResponseEntity.ok(ventilationCycleService.updateVentilationCycleStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVentilationCycle(@PathVariable Integer id) {
        ventilationCycleService.deleteVentilationCycle(id);
        return ResponseEntity.noContent().build();
    }
}
