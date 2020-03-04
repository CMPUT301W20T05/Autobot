package com.example.autobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.regex.Pattern;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SetPasswordActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity);
        final EditText setPassword = findViewById(R.id.editTextSetPassword);
        final EditText confirmPassord = findViewById(R.id.editTextConfirmPassword);
        Button buttonConfirmPassword = findViewById(R.id.buttonConfirmPassword);

        final Intent intent = getIntent();
        final String Username = intent.getStringExtra("Username");
        buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword=setPassword.getText().toString();
                String confirmPassword = confirmPassord.getText().toString();
                String pattern1 = ".*[a-z].*";
                String pattern2 = ".*[A-Z].*";
                String pattern3 = ".*[0-9].*";
                String pattern4 = ".*\\W.*";
                if (newPassword.compareTo(confirmPassword)==0){
                    boolean isMatch1 = Pattern.matches(pattern1,newPassword);
                    boolean isMatch2 = Pattern.matches(pattern2,newPassword);
                    boolean isMatch3 = Pattern.matches(pattern3,newPassword);
                    boolean isMatch4 = Pattern.matches(pattern4,newPassword);
                    if(!(isMatch1 && isMatch2 && isMatch3 && isMatch4)){
                        Toast toast = Toast.makeText(getApplicationContext(), "Password must contain a-zA-Z0-9 and special characters", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else{
                        HashMap<String,String> user_data = new HashMap<>();
                        user_data.put("Password",newPassword);
                        Database db = new Database();
                        db.collectionReference_user.document(Username).update("Password",newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Data addition successful");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data addition failed" + e.toString());
                                    }
                                });

                        Intent intentHomePage = new Intent(SetPasswordActivity.this, HomePageActivity.class);
                        startActivity(intentHomePage);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Confirm Password is wrong", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });
    }


}
