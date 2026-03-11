package ru.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.commerce.warehouse.entity.OrderBookingEntity;

import java.util.UUID;

@Repository
public interface OrderBookingRepository extends JpaRepository<OrderBookingEntity, UUID> {
}
