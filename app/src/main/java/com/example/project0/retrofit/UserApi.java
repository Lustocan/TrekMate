package com.example.project0.retrofit;

import com.example.project0.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {
    @GET("/users")
    Call<List<UserModel>> getAllUsers(@Header("Authorization")String token, @Header("Cookie")String cookie);

    @GET("/welcome")
    Call<String> welcomeMsg(@Header("Authorization")String token, @Header("Cookie")String cookie);
}
