package com.example.project0.retrofit;

import com.example.project0.model.AuthModel;
import com.example.project0.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/sign_up")
    Call<UserModel> sign_up(@Body UserModel userModel);

    @POST("/auth")
    Call<String> auth(@Body AuthModel authModel);
}
