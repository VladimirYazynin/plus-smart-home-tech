package ru.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.model.ScenarioAction;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, Long> {
    List<ScenarioAction> findAllByScenarioIdIn(List<Long> scenarioIds);

    void deleteAllByScenarioId(Long scenarioId);
}