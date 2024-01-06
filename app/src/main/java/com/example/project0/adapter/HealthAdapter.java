package com.example.project0.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project0.R;
import com.example.project0.model.TrainingModel;

import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthHolder> {
    private List<TrainingModel> trainingModelList ;
    public HealthAdapter(List<TrainingModel> trainingModelList){ this.trainingModelList = trainingModelList ; }

    @NonNull
    @Override
    public HealthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.training_list_item, parent, false );
        return new HealthHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthHolder holder, int position) {
        TrainingModel trainingModel = trainingModelList.get(position);
        holder.date.setText(trainingModel.date);
        holder.time.setText(trainingModel.time);
        holder.km.setText(trainingModel.kilometers);
        holder.kcal.setText(trainingModel.kilocalories);
        holder.id.setText(trainingModel.id);
        holder.id.onVisibilityAggregated(false);
    }

    @Override
    public int getItemCount() {
        return this.trainingModelList.size();
    }
}
