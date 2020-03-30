package com.example.autobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.text.DecimalFormat;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * This is a class for generating qrcode based on the price of the trip
 */
public class QRCode extends BaseActivity {

    String TAG = "Generate QRCode";
    //TextView fare;
    ImageView qrimg;
    Button generate, confirm;
    String inputvalue;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private Database db;
    private String username;
    private User user;
    private Request request;
    private String reID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.qrcode_generator, frameLayout);
        findViewById(R.id.myMap).setVisibility(View.GONE);

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
        User Driver = DriverIsOnTheWayActivity.driver;
        User rider = request.getRider();
        setProfile(username,db); // set profile
        qrimg = (ImageView) findViewById(R.id.qrcodeScanner);
        generate = (Button) findViewById(R.id.generate);
        confirm = (Button) findViewById(R.id.confirmButton);
        //ScannerView = findViewById(R.id.qrCodeScanner);

        //double estimateFare = request.getEstimateCost();
        //String textDiplay = "The cost of this trip is: " + String.valueOf(estimateFare);
        //fare.setText(textDiplay);//price of the trip

        DecimalFormat df = new DecimalFormat("0.00");
        double totalFare = request.getCost();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputvalue = df.format(totalFare);
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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QRCode.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                Intent intentRateDriver = new Intent(QRCode.this, RateDriver.class);
                finish();
                startActivity(intentRateDriver);
            }
        });
    }
    @Override
    public void onBackPressed(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();  // setup fragmentTransaction

        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu(); // get the menu
        MenuItem emItem = menu.findItem(R.id.edit_profile); // item edit profile
        MenuItem mhItem = menu.findItem(R.id.my_request_history); // item my request history
        MenuItem mnItem = menu.findItem(R.id.my_notification); // item my notification
        MenuItem piItem = menu.findItem(R.id.payment_information); // item payment information
        MenuItem sItem = menu.findItem(R.id.settings); // item settings
        MenuItem lItem = menu.findItem(R.id.log_out); // item log out

        //  store the menu to var when creating options menu

        if (drawer.isDrawerOpen(GravityCompat.START)) {  // if the drawer is opened, when a item is clicked, close the drawer
            drawer.closeDrawer(GravityCompat.START);
        }else if (fragment == null){}
        else {
            ft.remove(fragment).commit();
            fragment = null;
            setTitle("Rider Mode");
            onResume();
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.invalidate();
        }

    }

}
