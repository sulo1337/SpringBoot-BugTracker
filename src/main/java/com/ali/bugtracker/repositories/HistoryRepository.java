package com.ali.bugtracker.repositories;

import com.ali.bugtracker.entities.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<History,Long> {
}
