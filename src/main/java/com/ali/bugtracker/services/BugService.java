package com.ali.bugtracker.services;

import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.repositories.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugService {

    @Autowired
    BugRepository bugRepo;

   public List<Bug> findBugsByTicketId(Ticket ticket){ return bugRepo.findBugsByTicketId(ticket); }
   public void save(Bug bug){ bugRepo.save(bug);}
}
