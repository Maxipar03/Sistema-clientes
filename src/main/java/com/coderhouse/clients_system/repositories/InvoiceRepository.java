package com.coderhouse.clients_system.repositories;

import com.coderhouse.clients_system.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
