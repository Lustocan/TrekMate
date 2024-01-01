package com.example.project0.retrofit;
import com.example.project0.model.TrainingModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TrainingApi {
    @POST("/training")
    Call<TrainingModel> sv_training(@Header("Authorization")String token, @Header("Cookie")String cookie, @Body TrainingModel trainingModel);
}
