package com.example.uteapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.User;
import com.example.uteapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.CropImageView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class EditProfileActivity extends AppCompatActivity {

    Button btn_openCam,btn_openGall,btn_save;
    private final int CAMERA_REQEST_CODE=9213,GALLEYRY_REQUESR_CODE=2134,GALLEYRY_REQUESR_CODE_AVT=213483;
    ImageView img_media,img_edt_avt;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    String imageUrl,imageUrl1;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    User us;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        Window window = EditProfileActivity.this.getWindow();
        window.setStatusBarColor(EditProfileActivity.this.getResources().getColor(R.color.black));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        btn_openCam=findViewById(R.id.edt_btn_openCam);
        btn_openGall=findViewById(R.id.edt_btn_openGal);
        img_media=findViewById(R.id.edt_pr_img);
        img_edt_avt=findViewById(R.id.imgedtProfile);
        btn_save=findViewById(R.id.btn_Saveedt);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Calendar calendar = Calendar.getInstance();
                storageReference.child("image" + calendar.getTimeInMillis() + ".png");
                img_edt_avt.setDrawingCacheEnabled(true);
                img_edt_avt.buildDrawingCache();
                Bitmap bitmap = img_edt_avt.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = storageReference.child("image" + calendar.getTimeInMillis() + ".png").putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        System.out.println(imageUrl);
                                        if (imageUrl.isEmpty()) {
                                            imageUrl = "https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/error-image-generic.png?alt=media&token=dbfe9456-ba97-458f-8abf-dfd6e644dd25";
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        storageReference.child("image1" + calendar.getTimeInMillis() + ".png");
                                        img_media.setDrawingCacheEnabled(true);
                                        img_media.buildDrawingCache();
                                        Bitmap bitmap = img_media.getDrawingCache();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        byte[] data = baos.toByteArray();
                                        UploadTask uploadTask = storageReference.child("image1" + calendar.getTimeInMillis() + ".png").putBytes(data);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                if (taskSnapshot.getMetadata() != null) {
                                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                imageUrl1 = uri.toString();
                                                                if (imageUrl1.isEmpty()) {
                                                                    imageUrl1 = "https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/error-image-generic.png?alt=media&token=dbfe9456-ba97-458f-8abf-dfd6e644dd25";
                                                                }
                                                                progressDialog.dismiss();
                                                                GoiIntent();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    }
                });


            }
        });

        img_edt_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMGAVT();
            }
        });

        btn_openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCam,CAMERA_REQEST_CODE);
            }
        });
        btn_openGall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMG();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        Bitmap rotareBitmap=null;
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_REQEST_CODE){
                bitmap = (Bitmap) (data.getExtras().get("data"));
                img_media.setImageBitmap(bitmap);
            }

            if (requestCode==GALLEYRY_REQUESR_CODE && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());
                    rotareBitmap = rotateImageIfRequired(EditProfileActivity.this,bitmap,data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_media.setImageBitmap(rotareBitmap);
            }

            if (requestCode==GALLEYRY_REQUESR_CODE_AVT && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());
                    rotareBitmap = rotateImageIfRequired(EditProfileActivity.this,bitmap,data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_edt_avt.setImageBitmap(rotareBitmap);
            }
        }

    }
    private void selectIMG() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // allow both images and videos
        startActivityForResult(intent,GALLEYRY_REQUESR_CODE);
    }
    private void selectIMGAVT() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLEYRY_REQUESR_CODE_AVT);
    }
    public static Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, Uri uri) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(uri.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void GoiIntent() {
        databaseReference.child("users").child(Data.dataPhone).child("imgUS").setValue(imageUrl);
        databaseReference.child("users").child(Data.dataPhone).child("anhnen").setValue(imageUrl1);
    }

}