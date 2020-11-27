package com.example.frappe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class DispImageActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_image);
        imageView=(ImageView)findViewById(R.id.imageView2);
        Bundle ex = getIntent().getExtras();
        Bitmap image = ex.getParcelable("image");
        imageView.setImageBitmap(image);
    }
}