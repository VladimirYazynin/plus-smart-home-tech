package ru.practicum.commerce.shoppingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.commerce.shoppingstore.entity.ProductEntity;
import ru.practicum.commerce.shoppingstore.enums.ProductCategory;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Page<ProductEntity> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);
}
