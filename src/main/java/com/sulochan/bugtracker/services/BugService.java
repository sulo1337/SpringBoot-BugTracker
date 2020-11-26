package com.sulochan.bugtracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.entities.Bug;
import com.sulochan.bugtracker.entities.Ticket;
import com.sulochan.bugtracker.repositories.BugRepository;

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
