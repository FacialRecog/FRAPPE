package com.example.frappe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;


public  class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    EditText etUsername,etPassword;
    Button login;
    TextView tv_signUp;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_BITMAP = "bitmap";
    Bitmap image;
    private FaceDetector detector;
    private View mProgressView;
    private View mLoginFormView;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_CLASSIFICATIONS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        etUsername=findViewById(R.id.editTextTextPersonName);
        etPassword=findViewById(R.id.editTextTextPassword);
        login=findViewById(R.id.button);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tv_signUp = findViewById(R.id.tv_signUp);

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
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putParcelable(SAVED_INSTANCE_BITMAP, imageUri);
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
        }
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {

            image = (Bitmap) data.getExtras().get("data");


            image =   getResizedBitmap(image,300);
            try {
                File cacheFile = new File(this.getCacheDir(), "upoload" +  ".jpeg");
                if(!cacheFile.exists())
                {
                    File.createTempFile("upoload" +  ".png", null, this.getCacheDir());
                }


                FileOutputStream fOut = new FileOutputStream(cacheFile);

                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            Intent i = new Intent(getApplicationContext(), DispImageActivity.class);
            i.putExtra("image", image);
            startActivity(i);

        }
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
    public void onClick(View v) {

    }
}