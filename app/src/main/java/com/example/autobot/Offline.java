package com.example.autobot;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class Offline {
    public static void UploadUser(SharedPreferences u_references,User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        u_references.edit().putString("User",json);
        u_references.edit().commit();

    }
    public static void UploadRequest(SharedPreferences r_references,Request request){
        Gson gson = new Gson();
        String json = gson.toJson(request);
        r_references.edit().putString("Request",json);
        r_references.edit().commit();
    }
    public static User ExtractUser(SharedPreferences u_references){
        Gson gson = new Gson();
        String user_string = u_references.getString("User","");
        return gson.fromJson(user_string,User.class);
    }
    public static Request ExtractRequest(SharedPreferences u_references){
        Gson gson = new Gson();
        String request_string = u_references.getString("Request","");
        return gson.fromJson(request_string, Request.class);
    }
    public void clear(SharedPreferences u_references,SharedPreferences r_references){
        u_references.edit().clear();
        u_references.edit().commit();
        r_references.edit().clear();
        r_references.edit().commit();
    }

}