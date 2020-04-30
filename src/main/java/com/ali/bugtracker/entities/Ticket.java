package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ticket_id_seq")
    private long ticketId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name="owner")
    private Employee owner;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project projectId;

    @NotBlank(message = "name is empty")
    private String name;
    @NotBlank(message = "description is empty")
    private String description;
    private String creationDate;

    @NotBlank(message = "Choose a stage")
    private String status; // not started, in progress, submitted for testing, completed
    @NotBlank(message = "Choose a priority")
    private String priority; // high, average, low

    @OneToMany(mappedBy = "ticketId")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticketId")
    private List<History> histories;

    public Ticket(Employee employeeId, Project projectId, String name, String description, String creationDate, String status, String priority) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.status = status;
        this.priority=priority;
    }
}
