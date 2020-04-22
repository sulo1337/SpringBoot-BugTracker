package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
}
