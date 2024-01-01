package com.example.project0.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project0.R;
import com.example.project0.model.ItineraryModel;

import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathHolder> {
    private List<ItineraryModel> pathModelList ;
    public PathAdapter(List<ItineraryModel> itineraryModelList){ this.pathModelList = itineraryModelList ;}
    @NonNull
    @Override
    public PathHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.path_item, parent, false);
        return new PathHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathHolder holder, int position) {
        ItineraryModel itineraryModel = pathModelList.get(position);
        holder.routename.setText(itineraryModel.name);
        holder.startname.setText(itineraryModel.start);
        holder.endname.setText(itineraryModel.end);
        holder.length.setText(itineraryModel.length);
    }

    @Override
    public int getItemCount() {
        return this.pathModelList.size();
    }
}
