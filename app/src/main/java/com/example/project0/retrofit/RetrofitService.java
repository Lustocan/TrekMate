package com.example.project0.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit ;

    public RetrofitService(){
        initializeRetrofit();
    }

    private void initializeRetrofit(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.107:7777")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //baseUrl("http://10.0.2.2:7777")
    }

    public Retrofit getRetrofit(){
        return this.retrofit ;
    }
}
