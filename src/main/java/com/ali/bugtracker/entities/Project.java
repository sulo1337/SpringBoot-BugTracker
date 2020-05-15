package com.ali.bugtracker.entities;

import com.ali.bugtracker.customValidators.UniqueProject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long projectId;

    @ManyToOne()
    @JoinColumn(name="owner")
    private Employee owner;

    @NotBlank(message = "name is empty")
    @UniqueProject
    private String name;
    @NotBlank(message = "description is empty")
    private String description;
    private String creationDate;
    @NotBlank(message = "Choose a stage")
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

    public Project(Employee owner, String name, String description, String creationDate, String status) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.status = status;
    }
}

/*
            ///////////////how to do the date for later work ////////
    void test() {

        System.out.println(dtf.format(now));
    }

 */