package com.example.frappe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frappe.api.ApiClient;
import com.example.frappe.api.ApiInterface;
import com.example.frappe.image_picker.ImagePicker;
import com.example.frappe.model.UserModel;
import com.google.gson.Gson;


import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.frappe.image_picker.ImagePicker;
public class DispImageActivity extends AppCompatActivity {
    ImageView imageView;
    private File fileTemp ;
    private ApiInterface apiService;
    ProgressBar progress_bar;
    TextView textView,t1,t2,t3,t4,t5,t6,t7;

    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_image);
        imageView=(ImageView)findViewById(R.id.imageView2);

        t1=(TextView)findViewById(R.id.textView10);
        t2=(TextView)findViewById(R.id.textView11);
        t3=(TextView)findViewById(R.id.textView12);
        t4=(TextView)findViewById(R.id.textView13);
        t5=(TextView)findViewById(R.id.textView14);
        t6=(TextView)findViewById(R.id.textView15);
        t7=(TextView)findViewById(R.id.textView16);



        Bundle ex = getIntent().getExtras();
        Bitmap image = ex.getParcelable("image");
        imageView.setImageBitmap(image);


        SharedPreferences prefs = getSharedPreferences("attendance", MODE_PRIVATE);
        String id = prefs.getString("id","");
        String ename = prefs.getString("Employee Name","");
        String lstatus = prefs.getString("Login status","");
        String Time = prefs.getString("Time","");
        String deleted_at = prefs.getString("deleted_at","");
        String image1 = prefs.getString("image","");
        String created_at = prefs.getString("created_at","");
        String updated_at = prefs.getString("updated_at","");

        t1.setText(id);
        t2.setText(ename);
        t3.setText(lstatus);
        t4.setText(Time);
        t5.setText(deleted_at);
        t6.setText(created_at);
        t7.setText(updated_at);


        displayresponse();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(DispImageActivity.this, MainActivity.class);
                i.putExtra("result",1);
                startActivity(i);
                finish();
            }
        }, 20000);
    }

    private void displayresponse() {

    }
}