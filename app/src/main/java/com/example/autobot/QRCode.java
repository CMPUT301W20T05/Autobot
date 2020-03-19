package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This is a class for generating qrcode based on the price of the trip
 */
public class QRCode extends BaseActivity {

    String TAG = "Generate QRCode";
    TextView fare;
    ImageView qrimg;
    Button generate, button3;
    String inputvalue;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    //ZXingScannerView ScannerView;
    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.qrcodd_scanner, frameLayout);

        db = LoginActivity.db;

        Intent intent = getIntent();
        //username = intent.getStringExtra("Username");
        //reID = intent.getStringExtra("reid");

        //get user from firebase
        //user = db.rebuildUser(username);
        user = HomePageActivity.user;
        username = user.getUsername();
        //get request from firebase
        //request = db.rebuildRequest(reID, user);
        request = HomePageActivity.request;
        reID = request.getRequestID();

        setProfile(username,db); // set profile

        qrimg = (ImageView) findViewById(R.id.qrcodeScanner);
        fare = (TextView)findViewById(R.id.textView4);
        generate = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        //ScannerView = findViewById(R.id.qrCodeScanner);

        fare.setText("10");//price of the trip

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputvalue = fare.toString();
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerdimension = width<height ? width:height;
                smallerdimension = smallerdimension*3/4;
                qrgEncoder = new QRGEncoder(inputvalue, null, QRGContents.Type.TEXT,smallerdimension);
                try{
                    bitmap = qrgEncoder.encodeAsBitmap();
                    qrimg.setImageBitmap(bitmap);
                }
                catch (WriterException e){
                    Log.v(TAG, e.toString());
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateDriver = new Intent(QRCode.this, RateDriver.class);
//                intentRateDriver.putExtra("Username",user.getUsername());
//                intentRateDriver.putExtra("reid",request.getRequestID());
                startActivity(intentRateDriver);
            }
        });
    }

    /*@Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),"Pay Successful", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onPause(){
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    public void onResume(){
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }*/

}
