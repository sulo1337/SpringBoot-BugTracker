package com.ali.bugtracker.repositories;

import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.entities.Bug;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BugRepository extends CrudRepository<Bug,Long> {

    List<Bug> findBugsByTicketId(Ticket ticket);
    Bug findBugByBugId(Long bugId);
    @Query(nativeQuery = true,value = "select severity as Label, count(*) as Value " +
            "from bug where employee_id=:theEmployee and fixed=0 group by severity")
    List<ChartData> getBugSeverity(@Param("theEmployee") Long employeeId);
}
