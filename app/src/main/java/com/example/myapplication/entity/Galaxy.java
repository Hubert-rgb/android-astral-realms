package com.example.myapplication.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Galaxy {
    @JsonProperty
    private int id;
    @JsonProperty
    private int userNumber/* = 0*/;
    @JsonProperty
    private int maximalUserNumber;
    @JsonProperty
    private String galaxyName;
    //private PeriodType periodType;


}
