package ru.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.telemetry.analyzer.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
