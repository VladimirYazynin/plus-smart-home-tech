package ru.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.commerce.warehouse.entity.ProductStorageEntity;

import java.util.UUID;

@Repository
public interface ProductStorageRepository extends JpaRepository<ProductStorageEntity, UUID> {
}
