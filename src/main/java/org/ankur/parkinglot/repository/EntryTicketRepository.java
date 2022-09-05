package org.ankur.parkinglot.repository;

import org.ankur.parkinglot.domain.EntryTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryTicketRepository extends JpaRepository<EntryTicket, Long> {
}
