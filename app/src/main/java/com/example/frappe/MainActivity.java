package com.example.frappe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.frappe.api.ApiClient;
import com.example.frappe.api.ApiInterface;
import com.example.frappe.image_picker.ImagePicker;
import com.example.frappe.model.UserModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public  class MainActivity extends AppCompatActivity{
    EditText etUsername,etPassword;
    Button login;
    TextView tv_signUp;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int NEXT_INTENT=103;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_BITMAP = "bitmap";
    Bitmap image;
    private FaceDetector detector;
    private View mProgressView;
    private View mLoginFormView;
    private Uri imageUri;
    private File fileTemp ;
    private ApiInterface apiService;
    ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagePicker.setMinQuality(600, 600);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        etUsername=findViewById(R.id.editTextTextPersonName);
        etPassword=findViewById(R.id.editTextTextPassword);
        login=findViewById(R.id.button);
        progress_bar = findViewById(R.id.progress_bar1);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tv_signUp = findViewById(R.id.tv_signUp);

        Intent intent = getIntent();
        int intValue = intent.getIntExtra("result", 0);
        if(intValue==1){
            openCamera();
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();

            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this,
                        RegisterActivity.class);
                startActivity(i);

            }
        });

    }


    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }
        else {
            openCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_PERM_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else {
                Toast.makeText(this,"camera permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {

        Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        startActivityForResult(intent1,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            image = (Bitmap) data.getExtras().get("data");

            image =   getResizedBitmap(image,300);

            Intent i = new Intent(getApplicationContext(), DispImageActivity.class);
            i.putExtra("image", image);

            try {
                File cacheFile = new File(getApplicationContext().getCacheDir(), "upoload" +  ".jpeg");
                if(!cacheFile.exists())
                {
                    File.createTempFile("upoload" +  ".png", null, getApplicationContext().getCacheDir());
                }


                FileOutputStream fOut = new FileOutputStream(cacheFile);

                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                fileTemp = cacheFile;
            } catch (IOException e) {
                e.printStackTrace();
            }

            progress_bar.setVisibility(View.VISIBLE);
            attendance();

            startActivity(i);

        }

    }

    private void attendance() {
        MultipartBody.Part profileImage = null;
        if (fileTemp!=null) {

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), fileTemp);
            profileImage = MultipartBody.Part.createFormData("image1", fileTemp.getName(), requestBody);
        }

        Call<JsonObject> call = apiService.scan( profileImage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progress_bar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    if (jsonObject.has("id")) {

                        try {

                            Gson gson= new Gson();
                            UserModel user = gson.fromJson(jsonObject.toString(),UserModel.class);
                            if(user!=null)
                            {
                                Toast.makeText(getApplicationContext(), "user_attendance_marked_successfully",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else
                                Toast.makeText(getApplicationContext(), R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();


                    }

                } else {

                    Toast.makeText(getApplicationContext(), R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}