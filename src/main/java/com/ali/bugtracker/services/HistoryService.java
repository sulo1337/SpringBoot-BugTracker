package com.ali.bugtracker.services;


import com.ali.bugtracker.entities.History;
import com.ali.bugtracker.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepo;

    public void save(History history){historyRepo.save(history);}
    public void delete(History history){historyRepo.delete(history);}
}
