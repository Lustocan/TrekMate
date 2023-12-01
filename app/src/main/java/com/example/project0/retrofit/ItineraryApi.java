package com.example.project0.retrofit;

import com.example.project0.model.AuthModel;
import com.example.project0.model.ItineraryModel;
import com.example.project0.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ItineraryApi {
    @GET("/itineraries/{province}")
    Call<List<ItineraryModel>> getItinerariesByProvince(@Header("Authorization")String token, @Header("Cookie")String cookie, @Path("province")String province);
    @GET("/itineraries/{province}/{pathname}")
    Call<ItineraryModel> getItineraryByPathName(@Header("Authorization")String token,@Header("Cookie")String cookie,@Path("province")String province, @Path("pathname")String pathname);
}
