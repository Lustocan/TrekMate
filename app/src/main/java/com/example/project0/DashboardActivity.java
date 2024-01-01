package com.example.project0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.helpers.UserHelper;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    JwtHelper jwtHelper = new JwtHelper() ;
    CookieHelper cookieHelper = new CookieHelper();
    UserHelper userHelper = new UserHelper();
    Toolbar toolbar ;

    public void openWeather(View view){ startActivity(new Intent(this, WeatherActivity.class)); }
    public void openTraining(View view){ startActivity(new Intent(this, StepCounterActivity.class))   ;}
    public void openTrekking(View view){
        startActivity(new Intent(this, TrekkingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_logout){
                    jwtHelper.deleteToken(DashboardActivity.this);
                    cookieHelper.deleteCookie(DashboardActivity.this);
                    userHelper.deleteUsername(DashboardActivity.this);
                    Intent intent=new Intent(DashboardActivity.this,MainActivity.class); // redirecting to DashboardActivity
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.nav_home){
                    Intent intent=new Intent(DashboardActivity.this,DashboardActivity.class); // redirecting to DashboardActivity
                    startActivity(intent);
                }
                return true ;
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        return true ;
    }

}