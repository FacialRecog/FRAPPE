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
    TextView textView;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_image);
        imageView=(ImageView)findViewById(R.id.imageView2);
         textView=(TextView)findViewById(R.id.textView3);
        Bundle ex = getIntent().getExtras();
        Bitmap image = ex.getParcelable("image");
        imageView.setImageBitmap(image);

        displayresponse();



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i=new Intent(DispImageActivity.this, MainActivity.class);
//                i.putExtra("result",1);
//                startActivity(i);
//                finish();
//            }
//        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void displayresponse() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.20.10.6:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface ApiInterface = retrofit.create(ApiInterface.class);

        Call<List<UserModel>> listCall = ApiInterface.getPosts();

        listCall.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code " + response.code());
                    return;
                }

                List<UserModel> posts = response.body();

                for (UserModel  post : posts) {
                    String content = "";
                    content += "ID: " + post.getUsername() + "\n";
                    textView.append(content);

                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }

        });
    }
}