package com.ali.bugtracker.repositories;


import com.ali.bugtracker.dto.ChartData;
import com.ali.bugtracker.dto.TimelineData;
import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project,Long> {
     List<Project> findAll();
     List<Project> findAllByOwner(Employee manager);
     Project findByName(String name);
     Project findByProjectId(Long id);
    @Query(value="select project_id from employee_project where employee_id=:employeeId",nativeQuery=true)
     List<Long> findAllByEmployeeId(@Param("employeeId") Long employeeId);

    @Query(nativeQuery = true,value = "select status as Label, count(*) as Value " +
            "from project where owner=:theOwner group by status")
     List<ChartData> getProjectStatus(@Param("theOwner") Long ownerId);
    @Query(nativeQuery = true,value = "SELECT name as projectName, created_date as creationDate, deadline as endDate "+
                                        "FROM project where owner=:employeeId")
     List<TimelineData> getTimeLineData(@Param("employeeId") Long employeeId);
}
