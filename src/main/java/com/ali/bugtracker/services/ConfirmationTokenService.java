package com.ali.bugtracker.services;

import com.ali.bugtracker.entities.ConfirmationToken;
import com.ali.bugtracker.repositories.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ConfirmationTokenService {


    @Autowired
    ConfirmationTokenRepository confirmationTokenRepo;
    public ConfirmationToken findByConfirmationToken(String confirmationToken){
        return confirmationTokenRepo.findByConfirmationToken(confirmationToken);
    }
    public void save(ConfirmationToken confirmationToken){
        confirmationTokenRepo.save(confirmationToken);
    }


}
