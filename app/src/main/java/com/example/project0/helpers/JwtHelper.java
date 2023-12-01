package com.example.project0.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class JwtHelper {
    public void saveToken(Context context, String token) {
        SharedPreferences sharedPref = context.getSharedPreferences("Auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Token", token);
        editor.apply();
    }

    public String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Auth", Context.MODE_PRIVATE);
        if(sp.contains("Token")) return new String(sp.getString("Token", null));
        return null ;
    }

    public void deleteToken(Context context){
        context.deleteSharedPreferences("Auth");
    }
}
