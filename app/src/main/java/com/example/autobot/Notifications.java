package com.example.autobot;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
/**
 * this is a fragment show the option of notifications, people can choose their option here
 */
public class Notifications extends Fragment {
    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification, container,false);
        Switch notification = view.findViewById(R.id.switch1);
        Switch sound = view.findViewById(R.id.switch2);
        Switch vibrate = view.findViewById(R.id.switch3);

        boolean value1 = true; // default value if no value was found
        boolean value2 = true; // default value if no value was found
        boolean value3 = true; // default value if no value was found

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("isChecked", 0);

        value1 = sharedPreferences.getBoolean("isChecked1", value1); // retrieve the value of your key
        value2 = sharedPreferences.getBoolean("isChecked2", value2); // retrieve the value of your key
        value3 = sharedPreferences.getBoolean("isChecked3", value3); // retrieve the value of your key

        notification.setChecked(value1);
        sound.setChecked(value2);
        vibrate.setChecked(value3);

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit().putBoolean("isChecked1", true).apply();
                }else {
                    sharedPreferences.edit().putBoolean("isChecked1", false).apply();
                }
            }
        });
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit().putBoolean("isChecked2", true).apply();
                    unMute();
                }else {
                    sharedPreferences.edit().putBoolean("isChecked2", false).apply();
                    mute();
                }
            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit().putBoolean("isChecked3", true).apply();
                    enableVibration(new long[]{500, 500, 500, 500});
                }else {
                    sharedPreferences.edit().putBoolean("isChecked3", false).apply();
                    disableVibration();
                }
            }
        });

        return view;
    }
    /**
     * this function can mute the app when using.
     */
    private void mute() {
        //mute audio
        AudioManager amanager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    }
    /**
     * this function can unMute the app when using.
     */
    public void unMute() {
        //unMute audio
        AudioManager amanager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
    }
    /**
     * this function can enable vibration the app when using.
     */
    public void enableVibration(long[] customPattern){
        notificationBuilder.setVibrate(customPattern);
    }/**
     * this function can disable vibration the app when using.
     */
    public void disableVibration(){
        notificationBuilder.setVibrate(new long[]{0L});
    }
}
