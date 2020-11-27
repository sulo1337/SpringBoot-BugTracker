package com.ali.bugtracker.dto;

public interface ChartData {
    // property names should begin with get in order to be found by spring data
     String getLabel();
     int getValue();
}
