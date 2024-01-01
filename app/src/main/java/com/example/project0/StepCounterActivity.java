package com.example.project0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project0.helpers.CookieHelper;
import com.example.project0.helpers.JwtHelper;
import com.example.project0.helpers.UserHelper;
import com.example.project0.model.TrainingModel;
import com.example.project0.retrofit.RetrofitService;
import com.example.project0.retrofit.TrainingApi;

import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private UserHelper userHelper = new UserHelper();
    private JwtHelper jh = new JwtHelper();
    private CookieHelper ch = new CookieHelper();
    private TextView stepCountTextView ;
    private TextView distanceTextView  ;
    private TextView timeTextView      ;
    private TextView caloriesTextView  ;
    private Button pauseButton         ;
    private Button stopButton           ;
    private SensorManager sensorManager ;
    private Sensor stepCounterSensor    ;
    private Integer stepCount = 0            ;
    private boolean isPaused = true       ;
    private long timePaused = 0 ;
    private float stepLengthInMeters = 0.762f ;
    private long startTime ;
    private int index = 0 ;
    private boolean otp = true ;
    private boolean otp2 = false;
    private AlertDialog.Builder builder ;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long mils = System.currentTimeMillis() - startTime;
            int seconds = (int)(mils/1000);
            int min = seconds/60;
            int h   = seconds/3600;
            seconds = seconds % 60 ;
            timeTextView.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d", h, min, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        stepCountTextView = findViewById(R.id.stepCount);
        distanceTextView = findViewById(R.id.distance);
        timeTextView = findViewById(R.id.time);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        caloriesTextView = findViewById(R.id.calories);

        builder = new AlertDialog.Builder(this);
        startTime = System.currentTimeMillis();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepCounterSensor==null){
            stepCountTextView.setText("Step counter not available");
        }

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Alert!")
                        .setMessage("Your training will be deleted, do you want to save it?")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(StepCounterActivity.this, StepCounterActivity.class));
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String username = userHelper.getUsername(StepCounterActivity.this);
                                if(username!=null){
                                    TrainingModel trainingModel = new TrainingModel(username, String.valueOf(timeTextView.getText()), String.valueOf(distanceTextView.getText()),
                                                                                    String.valueOf(caloriesTextView.getText()), String.valueOf(stepCount), new Date().toString());
                                    RetrofitService retrofitService = new RetrofitService();
                                    TrainingApi trainingApi = retrofitService.getRetrofit().create(TrainingApi.class);
                                    String jwt = jh.getToken(StepCounterActivity.this);
                                    String cookie = ch.getCookie(StepCounterActivity.this);

                                    if(jwt!=null&&cookie!=null){
                                        trainingApi.sv_training("Bearer "+jwt, cookie, trainingModel)
                                                .enqueue(new Callback<TrainingModel>() {
                                                    @Override
                                                    public void onResponse(Call<TrainingModel> call, Response<TrainingModel> response) {
                                                        Toast.makeText(StepCounterActivity.this, "Activity stored.", Toast.LENGTH_SHORT);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<TrainingModel> call, Throwable t) {
                                                        Toast.makeText(StepCounterActivity.this, "Something gone wrong.", Toast.LENGTH_SHORT);
                                                    }
                                                });
                                    }

                                }
                                startActivity(new Intent(StepCounterActivity.this, StepCounterActivity.class));
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            if(otp){
                index = (int) sensorEvent.values[0];
                otp = false ;
            }
            if(!otp2){
                index += (int) ((sensorEvent.values[0]-index)-stepCount);
            }
            stepCount = (int) sensorEvent.values[0]-index;
            stepCountTextView.setText(stepCount.toString());
            float distanceInKm = (float)((stepCount) * stepLengthInMeters /1000);
            float caloriesKcal = (float) (70*distanceInKm*0.5);
            distanceTextView.setText(String.format(Locale.getDefault(), distanceInKm+" Km"));
            caloriesTextView.setText(String.format(Locale.getDefault(), caloriesKcal+" Kcal"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onPausedButtonClicked(View view){
        if(isPaused){
            isPaused=false ;
            otp2 = true   ;
            pauseButton.setText("Pause");
            startTime = System.currentTimeMillis()-timePaused;
            timerHandler.postDelayed(timerRunnable,0);
        }
        else{
            isPaused = true ;
            otp2 = false ;
            pauseButton.setText("Start");
            timerHandler.removeCallbacks(timerRunnable);
            timePaused = System.currentTimeMillis()-startTime ;
        }
    }

   /* @Override
    protected void onStop(){
        super.onStop();
        if(stepCounterSensor != null){
            sensorManager.unregisterListener(this);
            timerHandler.removeCallbacks(timerRunnable ) ;
        }
    }*/
    @Override
    protected void onResume(){
        super.onResume();

        if(stepCounterSensor != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    public void saveTimer(Context context, long time) {
        SharedPreferences sharedPref = context.getSharedPreferences("Timer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("CurTraining", time);
        editor.apply();
    }

}