package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
}
