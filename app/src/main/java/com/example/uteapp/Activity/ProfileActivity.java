package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uteapp.Model.Data;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView prProfile,prEmail,prPhone,prAddress,prSignOut;
    ImageView imgProfile,imgBackground;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        AnhXa();
        databaseReference.child("users").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("imgUS").getValue().toString()).into(imgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        prProfile = (TextView)  findViewById(R.id.prEditProfile);
        prProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        prSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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