package com.example.project0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project0.adapter.PathAdapter;
import com.example.project0.adapter.WeatherRVAdapter;
import com.example.project0.databinding.ActivityPathDetBinding;
import com.example.project0.databinding.ActivitySelectedPathBinding;
import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.model.ItineraryModel;
import com.example.project0.model.WeatherRVModel;
import com.example.project0.retrofit.ItineraryApi;
import com.example.project0.retrofit.RetrofitService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StyleSpan;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    private RecyclerView weatherRV              ;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList ;
    private WeatherRVAdapter weatherRVAdapter ;
    private GoogleMap mMap ;
    private RecyclerView recyclerView ;
    private String pointNames[]       ;
    private float  latitude[], longitude[]    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_selected_path);
        weatherRV = findViewById(R.id.idRvWeather);
        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter (this, weatherRVModelArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        recyclerView = findViewById(R.id.path_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));


        //binding = ActivitySelectedPathBinding.inflate(getLayoutInflater());

        String pathname = getIntent().getStringExtra("pathname");
        String province   = getIntent().getStringExtra("province");
        String start   = getIntent().getStringExtra("start");
        this.pointNames = getIntent().getStringArrayExtra("pointNames");
        this.longitude  = getIntent().getFloatArrayExtra("longitude");
        this.latitude  = getIntent().getFloatArrayExtra("latitude");

        //setContentView(binding.getRoot());

        //binding.pathName.setText(pathname);
        //binding.detailStart.setText(start);
        //binding.detailEnd.setText(end);
        //binding.detailLength.setText(length);

        loadItinerary(pathname, province);
        getWeatherInfo(start);
        // Obtain the SupportMapFragment and get notified when the map ise ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                            ArrayList<ItineraryModel> l = new ArrayList<>();
                            l.add(response.body());
                            populatePathView(l);
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
                    //binding.idRvWeather.setAdapter(weatherRVAdapter);


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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers on lines, add listeners or move the camera. In this case,
     * near Sydney Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //loadItinerary(getIntent().getStringExtra("pathname"),getIntent().getStringExtra("province"));
        mMap = googleMap ;
        //System.out.println(currentIt.name);

        // Add a marker in Sydney and move the camera
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Instantiates a new Polyline object and adds points to define a rectangle
        //PolylineOptions polylineOptions = new PolylineOptions();
                /*.add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0)); // Closes the polyline.
        mMap.addPolyline(polylineOptions);*/
        PolylineOptions polylineOptions = new PolylineOptions();
        for(int i=0; i<this.latitude.length; i++){
            mMap.addMarker(new MarkerOptions().position(new LatLng(this.latitude[i], this.longitude[i])).title(this.pointNames[i]));
            polylineOptions.add(new LatLng(this.latitude[i], this.longitude[i]));
        }
        polylineOptions.add(new LatLng(this.latitude[0], this.longitude[0]));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(this.latitude[0], this.longitude[0])));
        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(new LatLng(this.latitude[0], this.longitude[0]))
                .tilt(90)
                .zoom(13)
                .build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition1));
        mMap.addPolyline(polylineOptions);
    }
    private void populatePathView(List<ItineraryModel> itineraryModelList){
        PathAdapter pathAdapter = new PathAdapter(itineraryModelList);
        recyclerView.setAdapter(pathAdapter);
    }
}