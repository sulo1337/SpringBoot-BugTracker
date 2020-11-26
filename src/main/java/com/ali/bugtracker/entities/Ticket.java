package com.ali.bugtracker.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ticket extends Auditable<String> {
    public Ticket() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employeeId;

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @ManyToOne
    @JoinColumn(name="owner")
    private Employee owner;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project projectId;

    @NotBlank(message = "name is empty")
    @Size(min = 1,max = 99)
    private String name;
    @NotBlank(message = "description is empty")
    @Size(min = 1,max = 495)
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
