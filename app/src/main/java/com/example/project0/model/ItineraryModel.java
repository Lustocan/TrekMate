package com.example.project0.model;

public class ItineraryModel {
    public String name     ;
    public String province ;
    public String length    ;
    public String start    ;
    public String end      ;

    public ItineraryModel(String name, String province, String length, String start, String end){
        this.name = name ;
        this.province = province ;
        this.length = length ;
        this.start = start ;
        this.end = end ;
    }
}
