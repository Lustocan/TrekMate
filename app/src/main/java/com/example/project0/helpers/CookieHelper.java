package com.example.project0.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class CookieHelper {
    public void saveCookie(Context context, String cookie) {
        SharedPreferences sharedPref = context.getSharedPreferences("Auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Cookie", cookie);
        editor.apply();
    }

    public String getCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Auth", Context.MODE_PRIVATE);
        if(sp.contains("Cookie")) return new String(sp.getString("Cookie", null));
        return null ;
    }

    public void deleteCookie(Context context){
        context.deleteSharedPreferences("Auth");
    }
}
