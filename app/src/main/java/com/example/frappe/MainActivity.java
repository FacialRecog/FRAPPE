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
import java.io.FileNotFoundException;
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

            processImage(image);
            launchMediaScanIntent();
            try {
                processCameraPicture(image);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(getApplicationContext(), DispImageActivity.class);
            i.putExtra("image", image);
            startActivity(i);

        }
    }

    private void processCameraPicture(Bitmap bitmap) throws Exception  {

        if ( bitmap != null) {

            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
            //txtTakenPicDesc.setText(null);

            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);


                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);

//                txtTakenPicDesc.setText("FACE " + (index + 1) + "\n");
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }


            }

            if (faces.size() == 0) {
//                txtTakenPicDesc.setText("Scan Failed: Found nothing to scan");
            } else {
//                imgTakePicture.setImageBitmap(editedBitmap);
//                txtTakenPicDesc.setText(txtTakenPicDesc.getText() + "No of Faces Detected: " + " " + String.valueOf(faces.size()));
            }
        } else {
//            txtTakenPicDesc.setText("Could not set up the detector!");
        }
    }

    private Bitmap decodeBitmapUri(MainActivity ctx, Uri uri) throws FileNotFoundException {
        int targetW = 300;
        int targetH = 300;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }



    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        this.sendBroadcast(mediaScanIntent);
    }
    private void processImage(Bitmap editedBitmap) {
        if (editedBitmap != null) {
            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);

            Bitmap workingBitmap = Bitmap.createBitmap(editedBitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);

            canvas.drawBitmap(editedBitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);


            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);


                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);

//                txtSampleDesc.setText(txtSampleDesc.getText() + "FACE " + (index + 1) + "\n");
//                txtSampleDesc.setText(txtSampleDesc.getText() + "Smile probability:" + " " + face.getIsSmilingProbability() + "\n");
//                txtSampleDesc.setText(txtSampleDesc.getText() + "Left Eye Is Open Probability: " + " " + face.getIsLeftEyeOpenProbability() + "\n");
//                txtSampleDesc.setText(txtSampleDesc.getText() + "Right Eye Is Open Probability: " + " " + face.getIsRightEyeOpenProbability() + "\n\n");

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }


            }

            if (faces.size() == 0) {
                //txtSampleDesc.setText("Scan Failed: Found nothing to scan");
            } else {
//                imageView.setImageBitmap(editedBitmap);
//                txtSampleDesc.setText(txtSampleDesc.getText() + "No of Faces Detected: " + " " + String.valueOf(faces.size()));
            }
        } else {
//            txtSampleDesc.setText("Could not set up the detector!");
        }


    }

    @Override
    public void onClick(View v) {

    }
}