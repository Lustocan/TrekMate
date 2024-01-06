package com.example.project0.retrofit;
import com.example.project0.model.TrainingModel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrainingApi {
    @POST("/training")
    Call<TrainingModel> sv_training(@Header("Authorization")String token, @Header("Cookie")String cookie, @Body TrainingModel trainingModel);

    @GET("/training/myList")
    Call<List<TrainingModel>> my_trainings(@Header("Authorization")String token, @Header("Cookie")String cookie, @Header("username") String username);

    @DELETE("/training/{trId}")
    Call<String> delete_training(@Header("Authorization")String token, @Header("Cookie")String cookie, @Path("trId")String trId);
}
