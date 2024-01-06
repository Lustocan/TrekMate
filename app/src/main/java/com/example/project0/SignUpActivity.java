package com.example.project0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.UserModel;
import com.example.project0.retrofit.AuthApi;
import com.example.project0.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private AlertDialog.Builder builder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up();
    }


    private void sign_up(){
        EditText inputUsrn  = findViewById(R.id.form_textFieldUsername);
        EditText inputEmail = findViewById(R.id.form_textFieldEmail);
        EditText inputPass  = findViewById(R.id.form_textFiledPassword);
        MaterialButton buttonSign = findViewById(R.id.signup_button);

        RetrofitService retrofitService = new RetrofitService();
        AuthApi authapi = retrofitService.getRetrofit().create(AuthApi.class);
        buttonSign.setOnClickListener(view ->{
            String username = String.valueOf(inputUsrn.getText());
            String email = String.valueOf(inputEmail.getText());
            String password = String.valueOf(inputPass.getText());

            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setRole("USER");
            userModel.setUsername(username);
            userModel.setPassword(password);

            builder = new AlertDialog.Builder(this);

            builder.setTitle("Terms and Conditions")
                   .setMessage("By using this app you agree to our Terms and Conditions Privacy Policy.\n\n" +
                               "With this policy we declare that your data is kept only for the purpose of providing you a better experience through this app.\n\n"+
                               "Your data will never be voluntarily transferred or sold to other companies.")
                   .setCancelable(true)
                   .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           authapi.sign_up(userModel)
                                   .enqueue(new Callback<UserModel>() {
                                       @Override
                                       public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                           if(response.isSuccessful()){
                                               Toast.makeText(SignUpActivity.this, "Signup successfull", Toast.LENGTH_SHORT).show();

                                               Intent intent = new Intent(SignUpActivity.this ,LoginActivity.class);
                                               startActivity(intent);
                                           }
                                           else Toast.makeText(SignUpActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                                       }

                                       @Override
                                       public void onFailure(Call<UserModel> call, Throwable t) {
                                           Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                                           Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                                       }
                                   });
                       }
                   }).show();
        });
    }

    public void openLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
}