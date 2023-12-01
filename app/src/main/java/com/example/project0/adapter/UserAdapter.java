package com.example.project0.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project0.R;
import com.example.project0.model.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    private List<UserModel> userModelList;
    public UserAdapter(List<UserModel> userModelList){
        this.userModelList = userModelList;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel userModel = userModelList.get(position);
        holder.username.setText(userModel.getUsername());
        holder.email.setText(userModel.getEmail());
        holder.role.setText(userModel.getRole());
    }

    @Override
    public int getItemCount() {
        return this.userModelList.size();
    }
}
