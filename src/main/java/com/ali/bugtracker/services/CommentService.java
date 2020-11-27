package com.ali.bugtracker.services;

import com.ali.bugtracker.entities.Comment;
import com.ali.bugtracker.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepo;

    public void save(Comment comment){commentRepo.save(comment);}
    public void delete(Comment comment){commentRepo.delete(comment);}
}
