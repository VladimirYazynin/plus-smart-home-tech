package ru.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.telemetry.analyzer.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
}
