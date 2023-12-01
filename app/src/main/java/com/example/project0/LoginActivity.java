package com.example.project0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;


import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.AuthModel;
import com.example.project0.retrofit.AuthApi;
import com.example.project0.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login();
    }

    public void openSignUp(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }
    public void openUserList(View view){startActivity(new Intent(this, UserListActivity.class));}

    private void login(){
        EditText inputUsrn = findViewById(R.id.form_textFieldUsername);
        EditText inputPass = findViewById(R.id.form_textFiledPassword);
        MaterialButton buttonLog = findViewById(R.id.login_button);

        RetrofitService retrofitService = new RetrofitService();
        AuthApi authapi = retrofitService.getRetrofit().create(AuthApi.class);
        buttonLog.setOnClickListener(view -> {
            String username = String.valueOf( inputUsrn.getText());
            String password = String.valueOf( inputPass.getText());

            AuthModel authModel = new AuthModel();
            authModel.setUsername(username);
            authModel.setPassword(password);


            authapi.auth(authModel)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                String cookie =  response.headers().get("Set-Cookie") ;
                                Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                                jh.saveToken(LoginActivity.this, response.body());
                                ch.saveCookie(LoginActivity.this, cookie);
                                startActivity(new Intent(LoginActivity.this,DashboardActivity.class)); // redirecting to DashboardActivity
                            }
                            else Toast.makeText(LoginActivity.this, "Wrong password or username", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });

    }

}