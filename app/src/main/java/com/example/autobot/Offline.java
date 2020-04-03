package com.example.autobot;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.autobot.Common.GsonDeserializeExclusion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * This class is the static class to help other upload and extract data from shared preferences
 */

public class Offline {
    /**
     * This function helps use upload user to shared preferences
     * @param u_references the preferences
     * @param user upload user
     */
    public static void UploadUser(SharedPreferences u_references,User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.d("saveuser",user.getUserType()+"hi");
        SharedPreferences.Editor editor = u_references.edit();
        editor.putString("User",json);
        editor.commit();

    }
    /**
     * This function helps use upload request to shared preferences
     * @param r_references the preferences
     * @param request upload request
     */
    public static void UploadRequest(SharedPreferences r_references,Request request){
        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                .create();
        String json = gson.toJson(request);
        Log.d("saverequest",json+"hi");
        SharedPreferences.Editor editor = r_references.edit();
        editor.putString("Request",json);
        editor.commit();
    }

    /**
     * This function helps us extract user from shared preferences
     * @param u_references shared preferences
     * @return new user
     */
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
    /**
     * This function helps us extract request from shared preferences
     * @param u_references shared preferences
     * @return new request
     */
    public static Request ExtractRequest(SharedPreferences u_references){
        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new GsonDeserializeExclusion())
                .create();
        String request_string = u_references.getString("Request","");
        Type type = new TypeToken<Request>() {
        }.getType();
        Log.d("getrequest",request_string+"hhhhhhhh");
        return gson.fromJson(request_string, type);
    }

    /**
     * clear user shared preferences
     * @param u_references
     */
    public static void clear_user(SharedPreferences u_references){
        SharedPreferences.Editor editor = u_references.edit();
        editor.remove("User");
        editor.commit();
    }

    /**
     * clear request shared preferences
     * @param sharedPreferences
     */

    public static void clear_request(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Request");
        editor.commit();
    }

}
