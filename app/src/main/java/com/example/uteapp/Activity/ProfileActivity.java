package com.example.uteapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uteapp.R;

public class ProfileActivity extends AppCompatActivity {

    TextView prProfile,prEmail,prPhone,prAddress,prSignOut;
    ImageView imgProfile,imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AnhXa();
        prProfile = (TextView)  findViewById(R.id.prEditProfile);
        prProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    private void AnhXa() {
        prProfile = findViewById(R.id.prEditProfile);
        prEmail=(TextView) findViewById(R.id.prEmailAddress);
        prPhone=(TextView) findViewById(R.id.prPhoneNumber);
        prAddress = (TextView) findViewById(R.id.prResidentialAddress);
        imgProfile=(ImageView) findViewById(R.id.imgProfile);
        prSignOut =(TextView) findViewById(R.id.prSignOut);
    }
}