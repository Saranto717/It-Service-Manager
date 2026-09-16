package com.andreas.itservicemanager.repository;

import com.andreas.itservicemanager.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}