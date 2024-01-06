package com.example.project0.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project0.R;

public class HealthHolder extends RecyclerView.ViewHolder {
    TextView date, time, km, kcal, id ;

    public HealthHolder(@NonNull View itemView){
        super(itemView);
        date = itemView.findViewById(R.id.TrList_date);
        time = itemView.findViewById(R.id.TrList_time);
        km   = itemView.findViewById(R.id.TrList_km);
        kcal   = itemView.findViewById(R.id.TrList_kcal);
        id = itemView.findViewById(R.id.TrList_id);
    }
}
