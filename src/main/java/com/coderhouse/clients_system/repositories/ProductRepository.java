package com.coderhouse.clients_system.repositories;

import com.coderhouse.clients_system.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
