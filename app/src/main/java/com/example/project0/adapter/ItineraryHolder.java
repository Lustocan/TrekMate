package com.example.project0.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project0.R;

public class ItineraryHolder extends RecyclerView.ViewHolder {
    TextView name, length ;

    public ItineraryHolder(@NonNull View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.listName);
        length = itemView.findViewById(R.id.listLength) ;
    }
}
