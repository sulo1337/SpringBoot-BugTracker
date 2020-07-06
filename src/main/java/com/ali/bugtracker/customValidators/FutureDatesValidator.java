package com.ali.bugtracker.customValidators;

import com.ali.bugtracker.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FutureDatesValidator implements ConstraintValidator<FutureDate,String> {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    @Autowired
    HistoryService historyService;
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try
        {
            sdf.setLenient(false);
            Date date1 = sdf.parse(String.valueOf(date));
            Date date2=sdf.parse(String.valueOf(historyService.currentDate()));
            if (date1.compareTo(date2)>0){
                return true;
            }
            else return false;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
