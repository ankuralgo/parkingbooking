package org.ankur.parkinglot.repository;

import org.ankur.parkinglot.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
