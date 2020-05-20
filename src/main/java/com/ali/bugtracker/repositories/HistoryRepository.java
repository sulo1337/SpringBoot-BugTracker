package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.History;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<History,Long> {

    public History findHistoryByEventAndTicketId(String event, Ticket ticket);
}
