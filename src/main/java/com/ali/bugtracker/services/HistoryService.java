package com.ali.bugtracker.services;


import com.ali.bugtracker.entities.History;
import com.ali.bugtracker.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepo;

    public History findHistoryByHistoryId(Long historyId){
        return historyRepo.findHistoryByHistoryId(historyId);
    }
    public List<History> findAll(){return historyRepo.findAll();}

    public void save(History history){historyRepo.save(history);}
    public void delete(History history){historyRepo.delete(history);}
    // getting current date variable for the history table
    public String currentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
