package com.sulochan.bugtracker.customValidators;

import org.springframework.beans.factory.annotation.Autowired;

import com.sulochan.bugtracker.entities.Employee;
import com.sulochan.bugtracker.repositories.EmployeeRepository;

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
