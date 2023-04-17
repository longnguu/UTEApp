package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.uteapp.Adapter.ReelsAdapter;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.VideosModel;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ReelActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ArrayList<VideosModel> videosModels = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ReelsAdapter reelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2 = findViewById(R.id.vpager);
        reelsAdapter = new ReelsAdapter(videosModels,ReelActivity.this);
        databaseReference.child("Videos_Account").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    videosModels.add(new VideosModel(dataSnapshot.child("url").getValue(String.class),dataSnapshot.child("title").getValue(String.class),dataSnapshot.child("des").getValue(String.class)));
                    reelsAdapter.updateAdapter(videosModels);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewPager2.setAdapter(reelsAdapter);

    }
}