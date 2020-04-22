package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ticket_id_seq")
    private long id;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project projectId;

    private String name;
    private String description;
    private LocalDateTime creationDate;
    private String status; // not started, in progress, submitted for testing, completed

    @OneToMany(mappedBy = "ticketId")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticketId")
    private List<History> histories;

    public Ticket(Employee employeeId, Project projectId, String name, String description, LocalDateTime creationDate, String status) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.status = status;
    }
}
