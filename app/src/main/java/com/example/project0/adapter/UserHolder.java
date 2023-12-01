package com.example.project0.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project0.R;

public class UserHolder extends RecyclerView.ViewHolder {

    TextView username, email, role ;
    public UserHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.userListItem_usn);
        email    = itemView.findViewById(R.id.userListItem_email);
        role     = itemView.findViewById(R.id.userListItem_role);
    }
}
