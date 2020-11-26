package com.sulochan.bugtracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sulochan.bugtracker.entities.Comment;
import com.sulochan.bugtracker.repositories.CommentRepository;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepo;

    public void save(Comment comment){commentRepo.save(comment);}
    public void delete(Comment comment){commentRepo.delete(comment);}
}
