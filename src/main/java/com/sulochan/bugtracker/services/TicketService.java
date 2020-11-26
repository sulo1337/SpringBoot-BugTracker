package com.sulochan.bugtracker.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.entities.Project;
import com.sulochan.bugtracker.entities.Ticket;
import com.sulochan.bugtracker.repositories.TicketRepository;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepo;

    public List<Ticket> findAll(){return ticketRepo.findAll();}
    public Ticket findTicketByTicketId(Long id){
        return ticketRepo.findTicketByTicketId(id);
    }
    public Long countTicketsByProjectIdAndEmployeeId(Project project, Employee employee){
        return ticketRepo.countTicketsByProjectIdAndEmployeeId(project,employee);
    }
    public Long countTicketsByOwner(Employee owner){
        return ticketRepo.countTicketsByOwner(owner);
    }
    public void save(Ticket ticket){ticketRepo.save(ticket);}
    public void delete(Ticket ticket){ticketRepo.delete(ticket);}
    public List<Ticket> findTicketsByProjectIdAndStatus(Project projectId, String status){
        return ticketRepo.findTicketsByProjectIdAndStatus(projectId,status);
    }
    public List<ChartData> getTicketStatus(Long employeeId){
        return ticketRepo.getTicketStatus(employeeId);
    }
    public Long countTicketsByProjectIdAndStatus(Project projectId,String status){
        return ticketRepo.countTicketsByProjectIdAndStatus(projectId,status);
    }

}

