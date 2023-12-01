package com.example.project0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.project0.adapter.UserAdapter;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.UserModel;
import com.example.project0.retrofit.RetrofitService;
import com.example.project0.retrofit.UserApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {
    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();

    private RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.UserList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        loadUsers();
    }

    private void loadUsers(){
        RetrofitService retrofitService = new RetrofitService();
        UserApi userapi = retrofitService.getRetrofit().create(UserApi.class);
        String jwt = jh.getToken(UserListActivity.this);
        String cookie = ch.getCookie(UserListActivity.this);

        if(jwt!=null&&cookie!=null) {

            userapi.getAllUsers("Bearer " + jwt, cookie)
                    .enqueue(new Callback<List<UserModel>>() {
                        @Override
                        public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                            populateListView(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<UserModel>> call, Throwable t) {
                            Toast.makeText(UserListActivity.this, "Failed to load users", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private void populateListView(List<UserModel> userModelList){
        UserAdapter userAdapter = new UserAdapter(userModelList);
        recyclerView.setAdapter(userAdapter);
    }
}