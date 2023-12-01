package com.example.project0.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project0.R;
import com.example.project0.model.CityModel;
import com.example.project0.model.ItineraryModel;

import java.util.ArrayList;
import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryHolder> {
    private List<ItineraryModel> itineraryModelList ;

    public ItineraryAdapter(List<ItineraryModel> itineraryModelList){ this.itineraryModelList = itineraryModelList ;}

    @NonNull
    @Override
    public ItineraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itinerary_list, parent, false);
        return new ItineraryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryHolder holder, int position) {
        ItineraryModel itineraryModel = itineraryModelList.get(position);
        holder.name.setText(itineraryModel.name);
        holder.length.setText(itineraryModel.length);
    }

    @Override
    public int getItemCount() {
        return this.itineraryModelList.size();
    }

}
