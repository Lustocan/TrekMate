package com.example.project0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project0.adapter.HealthAdapter;
import com.example.project0.adapter.UserAdapter;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.helpers.UserHelper;
import com.example.project0.model.TrainingModel;
import com.example.project0.model.UserModel;
import com.example.project0.retrofit.AuthApi;
import com.example.project0.retrofit.RetrofitService;
import com.example.project0.retrofit.TrainingApi;
import com.example.project0.retrofit.UserApi;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout ;
    private NavigationView navigationView ;
    private Toolbar toolbar ;
    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper() ;
    private UserHelper uh = new UserHelper();
    private RecyclerView recyclerView ;
    private AlertDialog.Builder builder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        recyclerView = findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_logout){
                    jh.deleteToken(HealthActivity.this);
                    ch.deleteCookie(HealthActivity.this);
                    uh.deleteUsername(HealthActivity.this);
                    Intent intent=new Intent(HealthActivity.this,MainActivity.class); // redirecting to DashboardActivity
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.nav_home){
                    Intent intent=new Intent(HealthActivity.this,DashboardActivity.class); // redirecting to DashboardActivity
                    startActivity(intent);
                }
                return true ;
            }
        });

        loadTrainings();
    }

    private void loadTrainings(){
        RetrofitService retrofitService = new RetrofitService();
        TrainingApi trainingApi = retrofitService.getRetrofit().create(TrainingApi.class);
        String jwt = jh.getToken(HealthActivity.this);
        String cookie = ch.getCookie(HealthActivity.this);
        String username = uh.getUsername(HealthActivity.this);

        if(jwt!=null&&cookie!=null) {

            trainingApi.my_trainings("Bearer " + jwt, cookie, username)
                    .enqueue(new Callback<List<TrainingModel>>() {
                        @Override
                        public void onResponse(Call<List<TrainingModel>> call, Response<List<TrainingModel>> response) {
                            populateListView(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<TrainingModel>> call, Throwable t) {
                            Toast.makeText(HealthActivity.this, "Failed to load users", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    public void deleteTraining(View view){
        TextView el = view.findViewById(R.id.TrList_id);
        String id = String.valueOf(el.getText()) ;

        RetrofitService retrofitService = new RetrofitService();
        TrainingApi trapi = retrofitService.getRetrofit().create(TrainingApi.class);
        builder = new AlertDialog.Builder(this);


        Intent intent=new Intent(HealthActivity.this,HealthActivity.class);

        builder.setTitle("Alert!")
               .setMessage("Are you sure to delete this training?")
               .setCancelable(true)
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       //DO NOTHING
                   }
               })
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       trapi.delete_training("Bearer "+jh.getToken(HealthActivity.this), ch.getCookie(HealthActivity.this), id)
                               .enqueue(new Callback<String>() {
                                   @Override
                                   public void onResponse(Call<String> call, Response<String> response) {}

                                   @Override
                                   public void onFailure(Call<String> call, Throwable t) {}
                               });
                       startActivity(intent);
                   }
               }).show();
    }

    private void populateListView(List<TrainingModel> trainingModelList){
        HealthAdapter healthAdapter = new HealthAdapter(trainingModelList);
        recyclerView.setAdapter(healthAdapter);
    }
}