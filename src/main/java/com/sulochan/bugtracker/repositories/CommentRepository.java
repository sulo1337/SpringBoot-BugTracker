package com.sulochan.bugtracker.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sulochan.bugtracker.entities.Comment;
import com.sulochan.bugtracker.entities.Ticket;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {

     List<Comment> findAllByTicketId(Ticket id);

}
