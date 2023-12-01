package com.example.project0.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project0.R;
import com.example.project0.model.CityModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<CityModel> {
    public CityAdapter(@NonNull Context context, ArrayList<CityModel> dataAL){
        super(context, R.layout.city_list, dataAL);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        CityModel cityModel = getItem(position);

        if( view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.city_list, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);

        listName.setText(cityModel.name);
        listImage.setImageResource(cityModel.image);

        return view;
    }
}
