package com.example.project0.model;

import java.util.Date;

public class TrainingModel {
    private String username ;
    private String time ;
    private String kilometers ;
    private String kilocalories ;
    private String steps ;
    private String date    ;

    public TrainingModel(String username, String time, String kilometers,
                         String kilocalories, String steps, String date){
        this.username = username ;
        this.time = time ;
        this.kilocalories = kilocalories ;
        this.kilometers = kilometers ;
        this.steps = steps ;
        this.date = date ;
    }
}
