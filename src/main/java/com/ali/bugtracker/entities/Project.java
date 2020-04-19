package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "project_id_seq")
    private long id;

    @ManyToOne()
    @JoinColumn(name="owner")
    private Employee owner;

    private String name;
    private String description;
    private LocalDateTime creationDate;
    private String status;// not started, in progress , completed

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},
            fetch=FetchType.LAZY)
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;

    @OneToMany(mappedBy = "projectId")
    private List<Ticket> tickets;

    public Project(Employee owner, String name, String description, LocalDateTime creationDate, String status) {
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }

 */