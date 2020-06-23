package com.ali.bugtracker.services;


import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepo;

    public Ticket findTicketByTicketId(Long id){
        return ticketRepo.findTicketByTicketId(id);
    }
    public Long countTicketsByProjectIdAndEmployeeId(Project project, Employee employee){
        return ticketRepo.countTicketsByProjectIdAndEmployeeId(project,employee);
    }
    public void save(Ticket ticket){ticketRepo.save(ticket);}
    public void delete(Ticket ticket){ticketRepo.delete(ticket);}
    public List<Ticket> findTicketsByProjectIdAndStatus(Project projectId, String status){
        return ticketRepo.findTicketsByProjectIdAndStatus(projectId,status);
    }
}
