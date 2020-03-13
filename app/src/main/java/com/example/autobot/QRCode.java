package com.example.autobot;

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

public class QRCode extends BaseActivity {

    String TAG = "Generate QRCode";
    TextView fare;
    ImageView qrimg;
    Button generate;
    String inputvalue;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.qrcodd_scanner, frameLayout);

        qrimg = (ImageView) findViewById(R.id.qrcodeScanner);
        fare = (TextView)findViewById(R.id.textView4);
        generate = (Button) findViewById(R.id.button2);

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
    }
}
