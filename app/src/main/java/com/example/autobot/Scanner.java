package com.example.autobot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    //public Result result;
    //final int MY_PERMISSION_REQUEST_CAMERA = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

    }

    @Override
    public void handleResult(Result result) {
        //textUsername.setText(rawResult.getText());
        //result = rawResult;

        //Toast.makeText(ScanActivity.this,"The rider done.",Toast.LENGTH_SHORT ).show();
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        /*super.onPostResume();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();*/
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

}
