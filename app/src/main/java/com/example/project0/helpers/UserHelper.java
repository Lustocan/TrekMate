package com.example.project0.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserHelper {
    public void saveUsername(Context context, String username) {
        SharedPreferences sharePref = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public String getUsername(Context context){
        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        if(sp.contains("username")) return new String(sp.getString("username", null));
        return null ;
    }

    public void deleteUsername(Context context) { context.deleteSharedPreferences("User");}
}
