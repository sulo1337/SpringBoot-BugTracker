package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BugRepository extends CrudRepository<Bug,Long> {

    List<Bug> findBugsByTicketId(Ticket ticket);
}
