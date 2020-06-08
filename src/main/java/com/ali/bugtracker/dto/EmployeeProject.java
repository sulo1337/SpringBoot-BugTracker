package com.ali.bugtracker.dto;

public interface EmployeeProject {
    // property names should begin with get in order to be found by spring data
     String getFirstName();
     String getLastName();
     int getProjectCount();
}
