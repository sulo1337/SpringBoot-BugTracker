package com.sulochan.bugtracker.customValidators;

import org.springframework.beans.factory.annotation.Autowired;

import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.entities.Project;
import com.sulochan.bugtracker.repositories.ProjectRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueProjectValidator implements ConstraintValidator<UniqueProject,String> {
    @Autowired
    ProjectRepository projectRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Project project= projectRepo.findByName(value);
        if (project!=null)
            return false;
        else
            return true;
    }
}
