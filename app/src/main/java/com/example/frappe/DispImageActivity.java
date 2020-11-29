package com.example.frappe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frappe.api.ApiClient;
import com.example.frappe.api.ApiInterface;
import com.example.frappe.image_picker.ImagePicker;
import com.example.frappe.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.frappe.image_picker.ImagePicker;
public class DispImageActivity extends AppCompatActivity {
    ImageView imageView;
    private File fileTemp ;
    private ApiInterface apiService;
    ProgressBar progress_bar;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_image);
        imageView=(ImageView)findViewById(R.id.imageView2);

        Bundle ex = getIntent().getExtras();
        Bitmap image = ex.getParcelable("image");
        imageView.setImageBitmap(image);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(DispImageActivity.this,
                        MainActivity.class);
                i.putExtra("result",1);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

}