package com.example.project0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project0.adapter.ItineraryAdapter;
import com.example.project0.databinding.ActivityPathDetBinding;
import com.example.project0.databinding.ActivitySelectedPathBinding;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.ItineraryModel;
import com.example.project0.retrofit.ItineraryApi;
import com.example.project0.retrofit.RetrofitService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PathDetActivity extends AppCompatActivity {

    private ActivityPathDetBinding binding ;
    private RecyclerView recyclerView ;
    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    private String province ;

    private ArrayList<ItineraryModel> dataAR ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_det);

        binding = ActivityPathDetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        Intent intent = this.getIntent();
        if(intent != null){
            province = intent.getStringExtra("province");
            int provImg  = intent.getIntExtra("provImg", 0);

            binding.detailName.setText(province);
            binding.detailImage.setImageResource(provImg);

            loadItineraries(province);
        }
    }

    private void loadItineraries(String province){
        RetrofitService retrofitService = new RetrofitService();
        ItineraryApi itineraryapi = retrofitService.getRetrofit().create(ItineraryApi.class);
        String jwt = jh.getToken(PathDetActivity.this);
        String cookie = ch.getCookie(PathDetActivity.this);

        if(jwt!=null&&cookie!=null) {

            itineraryapi.getItinerariesByProvince("Bearer " + jwt, cookie, province)
                    .enqueue(new Callback<List<ItineraryModel>>() {
                        @Override
                        public void onResponse(Call<List<ItineraryModel>> call, Response<List<ItineraryModel>> response) {
                            populateListView(response.body());
                            dataAR = new ArrayList<>(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<ItineraryModel>> call, Throwable t) {
                            Toast.makeText(PathDetActivity.this, "Failed to load users", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private void populateListView(List<ItineraryModel> itineraryModelList){
        ItineraryAdapter itineraryAdapter = new ItineraryAdapter(itineraryModelList);
        recyclerView.setAdapter(itineraryAdapter);
    }

    public void openPathSelected(View view){
        Intent intent = new Intent(PathDetActivity.this, SelectedPathActivity.class);
        TextView name = view.findViewById(R.id.listName);
        String pname = String.valueOf(name.getText());

        for(int i=0; i<dataAR.size();i++){
            if(pname.equals(dataAR.get(i).name)){
                intent.putExtra("province", province);

                intent.putExtra("pathname", dataAR.get(i).name);
                intent.putExtra("start", dataAR.get(i).start);
                intent.putExtra("end", dataAR.get(i).end);
                intent.putExtra("length", dataAR.get(i).length);
            }
        }
        startActivity(intent);
    }
}