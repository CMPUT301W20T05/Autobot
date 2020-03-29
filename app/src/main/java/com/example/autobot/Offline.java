package com.example.autobot;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Offline {
    public static void UploadUser(SharedPreferences u_references,User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.d("saveuser",user.getUserType()+"hi");
        SharedPreferences.Editor editor = u_references.edit();
        editor.putString("User",json);
        editor.commit();

    }
    public static void UploadRequest(SharedPreferences r_references,Request request){
        Gson gson = new Gson();
        String json = gson.toJson(request);
        SharedPreferences.Editor editor = r_references.edit();
        editor.putString("Request",json);
        editor.commit();
    }
    public static User ExtractUser(SharedPreferences u_references) {
        Gson gson = new Gson();
        Log.d("loaduser", "hi");
        String user_string = u_references.getString("User", null);
        if (user_string == null) {
            user_string = "null";
        }
        Log.d("loaduserdata", user_string);
        Type type = new TypeToken<User>() {
        }.getType();
        return gson.fromJson(user_string, type);
    }
    public static Request ExtractRequest(SharedPreferences u_references){
        Gson gson = new Gson();
        String request_string = u_references.getString("Request","");
        return gson.fromJson(request_string, Request.class);
    }
    public static void clear(SharedPreferences u_references,SharedPreferences r_references){
        u_references.edit().clear();
        u_references.edit().commit();
        r_references.edit().clear();
        r_references.edit().commit();
    }



}
