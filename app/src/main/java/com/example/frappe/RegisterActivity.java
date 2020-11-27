package com.example.frappe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.frappe.api.ApiClient;
import com.example.frappe.api.ApiInterface;
import com.example.frappe.image_picker.ImagePicker;
import com.example.frappe.image_picker.ImageUtils;
import com.example.frappe.model.UserModel;
import com.example.frappe.utils.Commons;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements   PermissionRequestErrorListener {

    private CircleImageView ivProfileImage;
    private TextInputEditText etUsername, etPassword, etConfirmPassword,etAge;
    Button btUpdateProfile;
    private File fileTemp ;
    private ApiInterface apiService;
    Context mContext;
    ProgressBar progress_bar;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext =this;

        apiService = ApiClient.getClient().create(ApiInterface.class);


        initViews();

        registerClickListebers();

        ImagePicker.setMinQuality(600, 600);







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

    private void initViews() {
        ivProfileImage = findViewById(R.id.ivProfileImage);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAge = findViewById(R.id.etAge);
        btUpdateProfile = findViewById(R.id.btUpdateProfile);
        progress_bar = findViewById(R.id.progress_bar);


    }

    private void registerClickListebers() {


        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });

        btUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated())
                {
                    registerUser();
                }
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

    private void openCamera() {

        Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        startActivityForResult(intent1,CAMERA_REQUEST_CODE);
    }
    private void registerUser() {

        Map<String, RequestBody> params = new HashMap<>();
        params.put("username", Commons.createRequestBody(etUsername.getText().toString()));
        params.put("password", Commons.createRequestBody(etPassword.getText().toString()));
        params.put("confirm_password", Commons.createRequestBody(etConfirmPassword.getText().toString()));
        params.put("age", Commons.createRequestBody(etAge.getText().toString()));
        progress_bar.setVisibility(View.VISIBLE);

        MultipartBody.Part profileImage = null;
        if (fileTemp!=null) {

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), fileTemp);
            profileImage = MultipartBody.Part.createFormData("image", fileTemp.getName(), requestBody);
        }

        Call<JsonObject> call = apiService.register(params, profileImage);
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
                                Toast.makeText(mContext, R.string.user_registered_successfully,Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else
                                Toast.makeText(mContext, R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                        }catch (Exception e){
                            Toast.makeText(mContext, R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(mContext, R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();


                    }

                } else {

                    Toast.makeText(mContext, R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.request_cannot_be_processed,Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isValidated() {
        if(etUsername.getText().toString().isEmpty())
        {
            Toast.makeText(this, R.string.please_enter_username,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, R.string.please_enter_password,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
        {
            Toast.makeText(this, R.string.password_mismatch,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etAge.getText().toString().isEmpty())
        {
            Toast.makeText(this, R.string.enetr_age,Toast.LENGTH_SHORT).show();
            return false;
        }

        if(fileTemp==null || !fileTemp.exists())
        {
            Toast.makeText(this, R.string.select_image,Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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






    @Override
    public void onError(DexterError error) {
        Log.e("onError", "onError");
    }

    public boolean checkStoragePermission() {

        String permission = "android.permission.CAMERA";
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && data!=null && data.hasExtra("data")) {

           Bitmap image = (Bitmap) data.getExtras().get("data");


            image =   getResizedBitmap(image,300);
            ivProfileImage.setImageBitmap(image);

            try {
                File cacheFile = new File(mContext.getCacheDir(), "upoload" +  ".jpeg");
                if(!cacheFile.exists())
                {
                    File.createTempFile("upoload" +  ".png", null, mContext.getCacheDir());
                }


                FileOutputStream fOut = new FileOutputStream(cacheFile);

                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                fileTemp = cacheFile;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



}
