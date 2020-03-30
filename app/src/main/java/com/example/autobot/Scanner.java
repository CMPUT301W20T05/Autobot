
package com.example.autobot;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.Result;

import java.text.DecimalFormat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Database db;
    private Request request;
    private User user;
    private String username;
    private String reID;
    ZXingScannerView scannerView;
    public Result result;
    final int MY_PERMISSION_REQUEST_CAMERA = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request = DriveIsGoing.request;
        User Driver = request.getDriver();
        User rider = request.getRider();
        String Driver_name = Driver.getUsername();
        String Rider_name = rider.getUsername();
        db = LoginActivity.db;
        upadate(Driver_name,Driver,db);
        upadate(Rider_name,rider,db);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

    }

    @Override
    public void handleResult(Result rawResult) {
        result = rawResult;
        openDialog();
    }

    @Override
    protected void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean success = false;
        while (!success) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            }
            else {
                success = true;
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
        }
    }

    protected void openDialog(){
        //pop out dialog
        final BottomSheetDialog scanCompleteDialog = new BottomSheetDialog(Scanner.this);
        scanCompleteDialog.setContentView(R.layout.scan_complete);
        scanCompleteDialog.setCancelable(false);

        request = DriveIsGoing.request;
        User Driver = request.getDriver();
        User rider = request.getRider();
        db = LoginActivity.db;

        TextView price = scanCompleteDialog.findViewById(R.id.price);
        price.setText(String.valueOf(result.getText()));
        Toast.makeText(Scanner.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
        Driver.resetbalance(String.valueOf(Double.parseDouble(Driver.getBalance()) + Double.parseDouble(result.getText())),db);
        rider.resetbalance(String.valueOf(Double.parseDouble(rider.getBalance()) - Double.parseDouble(result.getText())),db);

        Button buttonReturn = scanCompleteDialog.findViewById(R.id.returnHomepage);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to home page
                Intent intenthome = new Intent(getApplicationContext(), DriverhomeActivity.class);
                //finish current thread
                finish();
                overridePendingTransition(0, 0);
                startActivity(intenthome);
                overridePendingTransition(0, 0);
            }
        });
        scanCompleteDialog.show();
    }
    @Override
    public void onBackPressed(){
        finish();
    }

    public void upadate(String Account,User user,Database db){
        db.getRef(Account).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userName = Account; // set username to username
                            DocumentReference documentReference = db.collectionReference_user.document(userName);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    user.setBalance((String) document.get("Balance"));
                                }
                            });
                        }
                        }
                    });
                }
}