package com.ali.bugtracker.services;

import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Ticket;
import com.ali.bugtracker.repositories.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugService {

    @Autowired
    BugRepository bugRepo;

    public List<Bug> findAll() {
        return bugRepo.findAll();
    }

    public List<Bug> findBugsByTicketId(Ticket ticket) {
        return bugRepo.findBugsByTicketId(ticket);
    }

    public void save(Bug bug) {
        bugRepo.save(bug);
    }
    public void delete(Bug bug){
        bugRepo.delete(bug);
    }
    public Bug findBugByBugId(Long bugId) {
        return bugRepo.findBugByBugId(bugId);
    }

    public List<ChartData> getBugSeverity(Long employeeId) {
        return bugRepo.getBugSeverity(employeeId);
    }

}
