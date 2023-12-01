package com.example.project0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.project0.adapter.CityAdapter;
import com.example.project0.databinding.ActivityTrekkingBinding;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.CityModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class TrekkingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    JwtHelper jwtHelper = new JwtHelper() ;
    CookieHelper cookieHelper = new CookieHelper();
    Toolbar toolbar ;
    ActivityTrekkingBinding binding ;
    CityAdapter cityAdapter ;
    ArrayList<CityModel> dataAL = new ArrayList<>();
    CityModel cityModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrekkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        int[] imageList = {R.drawable.vicenza,R.drawable.verona,R.drawable.padova,R.drawable.belluno,R.drawable.treviso,R.drawable.rovigo,R.drawable.venezia};
        String[] nameList = {"Vicenza", "Verona", "Padova", "Belluno", "Treviso", "Rovigo", "Venezia"};

        for(int i=0; i<imageList.length; i++){
            cityModel = new CityModel(nameList[i], imageList[i]);
            dataAL.add(cityModel);
        }
        cityAdapter = new CityAdapter(TrekkingActivity.this, dataAL);
        binding.listview.setAdapter(cityAdapter);
        binding.listview.setClickable(true);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Qui è il punto dove dovresti passare i dati sulle camminate
                Intent intent = new Intent(TrekkingActivity.this, PathDetActivity.class);
                intent.putExtra("provImg", imageList[i]);
                intent.putExtra("province", nameList[i]);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_logout){
                    jwtHelper.deleteToken(TrekkingActivity.this);
                    cookieHelper.deleteCookie(TrekkingActivity.this);
                    Intent intent=new Intent(TrekkingActivity.this,MainActivity.class); // redirecting to DashboardActivity
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.nav_home){
                    Intent intent=new Intent(TrekkingActivity.this,DashboardActivity.class); // redirecting to DashboardActivity
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