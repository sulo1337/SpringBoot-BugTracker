package com.ali.bugtracker.entities;

import com.ali.bugtracker.customValidators.UniqueEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;
    @NotBlank(message = "first name cannot be empty")
    private String firstName;
    @NotBlank(message = "last name cannot be empty")
    private String lastName;
    @NotBlank(message = "provide an email")
    @Email
    @UniqueEmail
    private String email;
    @NotBlank(message = "password field is empty")
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


}
