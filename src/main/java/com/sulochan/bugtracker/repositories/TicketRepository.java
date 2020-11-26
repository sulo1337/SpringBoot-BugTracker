package com.sulochan.bugtracker.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sulochan.bugtracker.dto.ChartData;
import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.entities.Project;
import com.sulochan.bugtracker.entities.Ticket;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
     List<Ticket> findAll();
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
