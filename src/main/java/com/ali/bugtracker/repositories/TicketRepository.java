package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
     List<Ticket> findAllByOwner(Employee owner);
     List<Ticket> findAllByProjectId(Project projectId);
     Ticket findTicketByTicketId(Long id);
     List<Ticket> findAllByEmployeeIdAndProjectId(Employee employeeId,Project projectId);
     Ticket findByNameAndProjectId(String name,Project projectId);
     Long countTicketsByProjectIdAndEmployeeId(Project project,Employee employee);
     List<Ticket> findTicketsByProjectIdAndStatus(Project projectId,String status);
}
