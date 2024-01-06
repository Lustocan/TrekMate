package com.example.project0.model;

import java.util.Date;

public class TrainingModel {
    public String id        ;
    public String username  ;
    public String time ;
    public String kilometers ;
    public String kilocalories ;
    public String steps ;
    public String date    ;

    public TrainingModel(String id , String username, String time, String kilometers,
                         String kilocalories, String steps, String date){
        this.id = id ;
        this.username = username ;
        this.time = time ;
        this.kilocalories = kilocalories ;
        this.kilometers = kilometers ;
        this.steps = steps ;
        this.date = date ;
    }
}
