package com.sulochan.bugtracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.entities.ConfirmationToken;
import com.sulochan.bugtracker.repositories.ConfirmationTokenRepository;

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
