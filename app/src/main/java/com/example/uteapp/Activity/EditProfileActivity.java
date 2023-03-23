package com.example.uteapp.Activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uteapp.R;
import com.soundcloud.android.crop.CropImageView;


import java.io.IOException;


public class EditProfileActivity extends AppCompatActivity {

    Button btn_openCam,btn_openGall;
    private final int CAMERA_REQEST_CODE=9213,GALLEYRY_REQUESR_CODE=2134,GALLEYRY_REQUESR_CODE_AVT=213483;
    ImageView img_media,img_edt_avt;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Window window = EditProfileActivity.this.getWindow();
        window.setStatusBarColor(EditProfileActivity.this.getResources().getColor(R.color.black));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        btn_openCam=findViewById(R.id.edt_btn_openCam);
        btn_openGall=findViewById(R.id.edt_btn_openGal);
        img_media=findViewById(R.id.edt_pr_img);
        img_edt_avt=findViewById(R.id.imgedtProfile);

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
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_REQEST_CODE){
                bitmap = (Bitmap) (data.getExtras().get("data"));
                img_media.setImageBitmap(bitmap);
            }

            if (requestCode==GALLEYRY_REQUESR_CODE && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_media.setImageBitmap(bitmap);
            }

            if (requestCode==GALLEYRY_REQUESR_CODE_AVT && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_edt_avt.setImageBitmap(bitmap);
            }
        }

    }
    private void selectIMG() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLEYRY_REQUESR_CODE);
    }
    private void selectIMGAVT() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLEYRY_REQUESR_CODE_AVT);
    }
}