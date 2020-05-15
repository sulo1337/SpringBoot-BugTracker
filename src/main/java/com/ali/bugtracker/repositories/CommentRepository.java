package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Comment;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {

    public List<Comment> findAllByTicketId(Ticket id);

}
