package com.ali.bugtracker.repositories;

import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.entities.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
     List<Ticket> findAllByOwner(Employee owner);
     Long countTicketsByOwner(Employee owner);
     Long countTicketsByProjectIdAndStatus(Project projectId,String status);
     List<Ticket> findAllByProjectId(Project projectId);
     Ticket findTicketByTicketId(Long id);
     List<Ticket> findAllByEmployeeIdAndProjectId(Employee employeeId,Project projectId);
     Ticket findByNameAndProjectId(String name,Project projectId);
     Long countTicketsByProjectIdAndEmployeeId(Project project,Employee employee);
     List<Ticket> findTicketsByProjectIdAndStatus(Project projectId,String status);
     @Query(nativeQuery = true,value = "select status as Label, count(*) as Value " +
             "from ticket where employee_id=:theEmployee group by status")
     List<ChartData> getTicketStatus(@Param("theEmployee") Long employeeId);
}
