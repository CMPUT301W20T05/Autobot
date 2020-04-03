package com.example.autobot;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.autobot.Common.GsonDeserializeExclusion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Offline {
    /*
    *@param u_references the where we save item
    *@param user the user of the app
    * this function is for saving the login user
     */
    public static void UploadUser(SharedPreferences u_references,User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.d("saveuser",user.getUserType()+"hi");
        SharedPreferences.Editor editor = u_references.edit();
        editor.putString("User",json);
        editor.commit();

    }
    /*
     *@param u_references the where we save item
     *@param request the unfinished
     * this function is for saving the unfinished request
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
    /*
     *@param u_references the where we save item
     * @return user the user who login last time.
     * this function is for rebuild saved user
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
    /*
     *@param u_references the where we save item
     * @return request the unfinished request
     * this function is for srebuild the saved user
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
    /*
     *@param u_references the where we save item
     * this function is for cleaning the saved user
     */
    public static void clear_user(SharedPreferences u_references){
        SharedPreferences.Editor editor = u_references.edit();
        editor.remove("User");
        editor.commit();
    }
    /*
     *@param u_references the where we save item
     * this function is for cleaning the saved request
     */
    public static void clear_request(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Request");
        editor.commit();
    }

}
