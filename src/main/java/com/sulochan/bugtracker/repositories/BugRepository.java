package com.sulochan.bugtracker.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.entities.Bug;
import com.sulochan.bugtracker.entities.Ticket;

import java.util.List;

public interface BugRepository extends CrudRepository<Bug,Long> {

    List<Bug> findAll();
    List<Bug> findBugsByTicketId(Ticket ticket);
    Bug findBugByBugId(Long bugId);
    @Query(nativeQuery = true,value = "select severity as Label, count(*) as Value " +
            "from bug where employee_id=:theEmployee and fixed=0 group by severity")
    List<ChartData> getBugSeverity(@Param("theEmployee") Long employeeId);
}
