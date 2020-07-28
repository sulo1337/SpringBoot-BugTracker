package com.ali.bugtracker.entities;

import com.ali.bugtracker.customValidators.FutureDate;
import com.ali.bugtracker.customValidators.TimeFormat;
import com.ali.bugtracker.customValidators.UniqueProject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor
public class Project extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;

    @ManyToOne()
    @JoinColumn(name="owner")
    private Employee owner;

    @NotBlank(message = "name is empty")
    private String name;
    @NotBlank(message = "description is empty")
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

    public Project(Employee owner, String name, String description,String deadline, String status) {
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