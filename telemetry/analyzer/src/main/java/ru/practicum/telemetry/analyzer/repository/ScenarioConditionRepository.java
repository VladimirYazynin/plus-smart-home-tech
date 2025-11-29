package ru.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.model.ScenarioCondition;

import java.util.List;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, Long> {
    List<ScenarioCondition> findAllByScenarioIdIn(List<Long> scenarioIds);

    void deleteAllByScenarioId(Long scenarioId);
}