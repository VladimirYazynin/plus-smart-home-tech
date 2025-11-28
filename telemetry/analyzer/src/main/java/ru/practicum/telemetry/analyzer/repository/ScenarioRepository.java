package ru.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.model.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
