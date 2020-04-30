package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    public List<Ticket> findAllByOwner(Employee owner);
    public List<Ticket> findAllByProjectId(Project projectId);
}
