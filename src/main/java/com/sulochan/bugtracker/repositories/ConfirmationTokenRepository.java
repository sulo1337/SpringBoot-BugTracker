package com.sulochan.bugtracker.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sulochan.bugtracker.entities.ConfirmationToken;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

}
