package com.ali.bugtracker.customValidators;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.entities.Project;
import com.ali.bugtracker.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
