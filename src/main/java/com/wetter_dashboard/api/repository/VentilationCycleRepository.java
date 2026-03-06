package com.wetter_dashboard.api.repository;

import com.wetter_dashboard.api.model.VentilationCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentilationCycleRepository extends JpaRepository<VentilationCycle, Integer> {
    Optional<VentilationCycle> findFirstByLocationIdAndIsActiveTrueOrderByCalculatedAtDesc(Integer locationId);
}
