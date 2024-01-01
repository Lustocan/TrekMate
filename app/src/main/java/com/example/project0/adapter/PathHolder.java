package com.example.project0.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project0.R;

public class PathHolder extends RecyclerView.ViewHolder {
    TextView routename, startname, endname, length ;
    public PathHolder(@NonNull View itemView){
        super(itemView);
        this.routename = itemView.findViewById(R.id.routename);
        this.startname = itemView.findViewById(R.id.startname);
        this.endname = itemView.findViewById(R.id.endname);
        this.length = itemView.findViewById(R.id.length);
    }
}
