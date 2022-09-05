package org.ankur.parkinglot.repository;

import org.ankur.parkinglot.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
