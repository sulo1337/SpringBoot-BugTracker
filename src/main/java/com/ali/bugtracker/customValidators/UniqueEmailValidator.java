package com.ali.bugtracker.customValidators;

import com.ali.bugtracker.entities.Employee;
import com.ali.bugtracker.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    EmployeeRepository employeeRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
      Employee employee= employeeRepo.findByEmail(value);
      if (employee!=null)
          return false;
      else
        return true;
    }
}
