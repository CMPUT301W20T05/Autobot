package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class DriverhomeActivity extends HomePageActivity implements ActiverequestsFragment.OnBackPressed {
    private User user;
    String phone_num;
    FragmentManager active_request_fm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load_user();
        setTitle(phone_num);
        //testing
        user = new User();
        user.setFirstName("jc");
        user.setLastName("lyu");
        update_navigation_view(user);
        Fragment fragment = new ActiverequestsFragment();
        active_request_fm = getSupportFragmentManager();
        active_request_fm.beginTransaction()
                .replace(R.id.myMap,fragment).addToBackStack(null).commit();
    }

    public void load_user(){
        Intent intent = getIntent();
        phone_num = intent.getExtras().getString("phone number");
    }

    public void update_navigation_view(User user){
        NavigationView navigationView = getWindow().getDecorView().getRootView().findViewById(R.id.nav_view);
        //find the layout of header layout
        View header = navigationView.getHeaderView(0);

        //find the text view of user name
        TextView user_name = header.findViewById(R.id.driver_name);
        ImageView profile = header.findViewById(R.id.profile_photo);

        //edit
        user_name.setText(user.getFirstName()+user.getLastName());
        Bitmap pic = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
        profile.setImageBitmap(pic);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.my_request_history:
                Log.d("check","1");
                break;
            case R.id.settings:
                Log.d("check","2");
                break;
            case R.id.payment_information:
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, new PaymentInformationFragment()).commit();
                break;
            case R.id.log_out:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);startActivity(intent);
                break;
            case R.id.edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.myMap, new EditProfilePage()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void hide() {
        active_request_fm.popBackStack();
    }

}
