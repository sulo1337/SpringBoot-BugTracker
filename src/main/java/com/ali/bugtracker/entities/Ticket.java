package com.ali.bugtracker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Ticket extends Auditable<Ticket> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Pattern(regexp = "^(NOT STARTED|IN PROGRESS|SUBMITTED FOR TESTING|COMPLETED)$" ,message = "invalid status")
    private String status; // not started, in progress, submitted for testing, completed
    @NotBlank(message = "Choose a priority")
    private String priority; // high, average, low

    @OneToMany(mappedBy = "ticketId")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticketId")
    private List<Bug> bugs;

    @OneToMany(mappedBy = "ticketId")
    private List<History> histories;

    public Ticket(Employee employeeId, Project projectId, String name, String description, String status, String priority) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority=priority;
    }

    @PrePersist
    void preInsert(){
        if (this.status==null || this.status.equals("")){
            this.status="NOT STARTED";
        }
    }
}
