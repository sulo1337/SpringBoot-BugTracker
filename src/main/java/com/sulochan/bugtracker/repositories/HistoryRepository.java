package com.sulochan.bugtracker.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sulochan.bugtracker.entities.History;
import com.sulochan.bugtracker.entities.Ticket;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History,Long> {

     List<History> findAll();
     History findHistoryByEventAndTicketId(String event, Ticket ticket);
     History findHistoryByHistoryId(Long historyId);
}
