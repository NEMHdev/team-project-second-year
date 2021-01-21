package com.example.andy.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static android.R.attr.bitmap;

/**
 * Created by Andy on 11/2/2017.
 */

public class GeneratorActivity extends AppCompatActivity {
    EditText text;
    Button gen_btn;
    Button conf_btn;
    Button canc_btn;
    ImageView image;
    String text2Qr;
    Bitmap bitmap1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        text = (EditText) findViewById(R.id.text);
        gen_btn = (Button) findViewById(R.id.gen_btn);
        conf_btn = (Button) findViewById(R.id.conf_btn);
        canc_btn = (Button) findViewById(R.id.canc_btn);
        image = (ImageView) findViewById(R.id.image);
        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = text.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                    bitmap1 = bitmap;
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });
        conf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Bitmap" , bitmap1);
                intent.setClass(GeneratorActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });
        canc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GeneratorActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });
    }
}