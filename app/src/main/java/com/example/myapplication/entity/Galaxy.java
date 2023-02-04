package com.example.myapplication.entity;

import com.example.myapplication.enumTypes.PeriodType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Galaxy {
    private int id;
    private int userNumber;
    private int maximalUserNumber;
    private String galaxyName;
    private PeriodType periodType;


}
