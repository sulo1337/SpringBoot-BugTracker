package com.ali.bugtracker.customValidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatValidator implements ConstraintValidator<TimeFormat, String> {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try
        {
            sdf.setLenient(false);
            Date d = sdf.parse(String.valueOf(date));
            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }
}
