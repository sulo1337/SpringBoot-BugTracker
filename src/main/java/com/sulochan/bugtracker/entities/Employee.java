package com.sulochan.bugtracker.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sulochan.bugtracker.customValidators.UniqueEmail;

import java.util.List;

@Entity


public class Employee {
    public Employee() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;
    @NotBlank(message = "first name cannot be empty")
    @Size(min = 3,max = 99)
    private String firstName;
    @NotBlank(message = "last name cannot be empty")
    @Size(min = 1,max = 99)
    private String lastName;
    @NotBlank(message = "provide an email")
    @Email
    @UniqueEmail
    private String email;
    @NotBlank(message = "password field is empty")
    @Size(min = 6,max = 99)
    private String password;

    private boolean enabled;

    @NotBlank(message = "Choose a role")
    @Pattern(regexp = "^(ROLE_M|ROLE_P|ROLE_T)$" ,message = "invalid role")
    private String role;
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},
            fetch=FetchType.LAZY)
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;

    @OneToMany(mappedBy = "employeeId")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "employeeId")
    private List<Comment> comments;

    @OneToMany(mappedBy = "employeeId")
    private List<Bug> bugs;


    @OneToMany(mappedBy = "employeeId")
    private List<History> histories;

    public Employee(String firstName, String lastName, String email,String password,String role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password=password;
        this.role=role;
        this.enabled=false;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
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
}
