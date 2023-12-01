package com.example.project0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project0.adapter.WeatherRVAdapter;
import com.example.project0.databinding.ActivityPathDetBinding;
import com.example.project0.databinding.ActivitySelectedPathBinding;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.ItineraryModel;
import com.example.project0.model.WeatherRVModel;
import com.example.project0.retrofit.ItineraryApi;
import com.example.project0.retrofit.RetrofitService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedPathActivity extends AppCompatActivity {

    private ActivitySelectedPathBinding binding ;
    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    private RecyclerView weatherRV              ;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList ;
    private WeatherRVAdapter weatherRVAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_selected_path);
        weatherRV = findViewById(R.id.idRvWeather);
        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModelArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        binding = ActivitySelectedPathBinding.inflate(getLayoutInflater());

        String pathname = getIntent().getStringExtra("pathname");
        String length   = getIntent().getStringExtra("length");
        String start   = getIntent().getStringExtra("start");
        String end   = getIntent().getStringExtra("end");

        setContentView(binding.getRoot());

        binding.pathName.setText(pathname);
        binding.detailStart.setText(start);
        binding.detailEnd.setText(end);
        binding.detailLength.setText(length);

        getWeatherInfo(start);

    }

    private void loadItinerary(String pathname, String province){
        RetrofitService retrofitService = new RetrofitService();
        ItineraryApi itineraryapi = retrofitService.getRetrofit().create(ItineraryApi.class);
        String jwt = jh.getToken(SelectedPathActivity.this);
        String cookie = ch.getCookie(SelectedPathActivity.this);

        if(jwt!=null&&cookie!=null) {

            itineraryapi.getItineraryByPathName("Bearer " + jwt, cookie, province,pathname)
                    .enqueue(new Callback<ItineraryModel>() {
                        @Override
                        public void onResponse(Call<ItineraryModel> call, Response<ItineraryModel> response) {
                            binding = ActivitySelectedPathBinding.inflate(getLayoutInflater());
                            setContentView(binding.getRoot());

                            binding.pathName.setText(response.body().name);
                            binding.detailStart.setText(response.body().start);
                            binding.detailEnd.setText(response.body().end);
                            binding.detailLength.setText(response.body().length);

                            getWeatherInfo("Mestre");

                        }

                        @Override
                        public void onFailure(Call<ItineraryModel> call, Throwable t) {
                            Toast.makeText(SelectedPathActivity.this, "Failed to load users", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }
    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=e4e3455d97d1465ca24170503231111&q="+cityName+"&days=1&aqi=no&alerts=no";
        RequestQueue requestQueue = Volley.newRequestQueue(SelectedPathActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherRVModelArrayList.clear();

                try {
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray  = forecastO.getJSONArray("hour");

                    for(int i=0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherRVModelArrayList.add(new WeatherRVModel(time, temper, img, wind));
                    }

                    weatherRVAdapter.notifyDataSetChanged();
                    binding.idRvWeather.setAdapter(weatherRVAdapter);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectedPathActivity.this, "Please enter a valid city name..", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}