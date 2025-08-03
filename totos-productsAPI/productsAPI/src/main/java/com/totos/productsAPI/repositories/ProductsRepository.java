package com.totos.productsAPI.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totos.productsAPI.models.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByName(String name);
    Optional<Products> findByNameAndDescription(String name, String description);
}
