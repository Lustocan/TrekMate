package com.example.project0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import retrofit2.Call;


import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.UserModel;
import com.example.project0.retrofit.RetrofitService;
import com.example.project0.retrofit.UserApi;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authBranch();
    }


    private void authBranch(){
        RetrofitService retrofitService = new RetrofitService();
        UserApi userapi = retrofitService.getRetrofit().create(UserApi.class);
        String jwt = jh.getToken(MainActivity.this);
        String cookie = ch.getCookie(MainActivity.this);

        if(jwt!=null&&cookie!=null) {

            userapi.getAllUsers("Bearer " + jwt, cookie)
                    .enqueue(new Callback<List<UserModel>>() {
                        @Override
                        public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                            if(response.isSuccessful()){
                                Intent intent=new Intent(MainActivity.this,DashboardActivity.class); // redirecting to DashboardActivity
                                startActivity(intent);
                            }
                            else{
                                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserModel>> call, Throwable t) {}
                    });
        }
        else{
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

}

