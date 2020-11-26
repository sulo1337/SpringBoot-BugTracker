package com.sulochan.bugtracker.entities;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.sulochan.bugtracker.customValidators.FutureDate;
import com.sulochan.bugtracker.customValidators.TimeFormat;
import com.sulochan.bugtracker.customValidators.UniqueProject;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Entity

public class Project extends Auditable<String> {
    public Project() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;

    @ManyToOne()
    @JoinColumn(name="owner")
    private Employee owner;

    @NotBlank(message = "name is empty")
    @Size(min = 1,max = 99)
    private String name;
    @NotBlank(message = "description is empty")
    @Size(min = 1,max = 495)
    private String description;
    @TimeFormat
    @FutureDate
    @NotBlank(message = "deadline is empty")
    private String deadline;
    @Pattern(regexp = "^(NOT STARTED|IN PROGRESS|COMPLETED)$" ,message = "invalid status")
    private String status;// not started, in progress , completed

    @NotNull
    @NotEmpty
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},
            fetch=FetchType.LAZY)
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;

    @OneToMany(mappedBy = "projectId")
    private List<Ticket> tickets;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Project(Employee owner, String name, String description, String deadline, String status) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    @PrePersist
    void preInsert(){
        if (this.status==null || this.status.equals("")){
            this.status="NOT STARTED";
        }
    }
}