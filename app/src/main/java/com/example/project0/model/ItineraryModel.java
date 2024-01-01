package com.example.project0.model;

public class ItineraryModel {
    public String name           ;
    public String province        ;
    public String length          ;
    public String start           ;
    public String end             ;
    public float latitude[]       ;
    public float longitude[]      ;
    public String pointNames[]    ;

    public ItineraryModel(String name, String province, String length, String start,
                          String end, float latitude[], float longitude[], String pointNames[]){
        this.name = name         ;
        this.province = province ;
        this.length = length     ;
        this.start = start       ;
        this.end = end           ;
        this.latitude = latitude ;
        this.longitude = longitude ;
        this.pointNames = pointNames   ;
    }
}
