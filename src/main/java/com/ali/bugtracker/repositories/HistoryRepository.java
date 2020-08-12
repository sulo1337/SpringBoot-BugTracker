package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.History;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History,Long> {

     List<History> findAll();
     History findHistoryByEventAndTicketId(String event, Ticket ticket);
     History findHistoryByHistoryId(Long historyId);
}
