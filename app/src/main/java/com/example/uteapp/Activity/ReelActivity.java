package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.uteapp.Adapter.PhotoAdapter;
import com.example.uteapp.Adapter.ReelsAdapter;
import com.example.uteapp.Adapter.Tab1Adapter;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.PicVideos;
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

    ArrayList<PicVideos> picVideos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2 = findViewById(R.id.vpager);
        reelsAdapter = new ReelsAdapter(picVideos,ReelActivity.this);
//        databaseReference.child("Media").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    videosModels.add(new VideosModel(dataSnapshot.child("url").getValue(String.class),dataSnapshot.child("title").getValue(String.class),dataSnapshot.child("des").getValue(String.class)));
//                    reelsAdapter.updateAdapter(videosModels);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        databaseReference.child("Media").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                picVideos.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    i++;
                    PicVideos picVideoss = new PicVideos();
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        picVideoss.setLink(dataSnapshot1.child("link").getValue(String.class));
                        picVideoss.setLoai(dataSnapshot1.child("l").getValue(String.class));
                        picVideoss.setDes(dataSnapshot1.child("des").getValue(String.class));
                        picVideoss.setTitle(dataSnapshot1.child("title").getValue(String.class));
                    }
                    picVideos.add(picVideoss);
                }
                reelsAdapter.updateAdapter(picVideos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewPager2.setAdapter(reelsAdapter);

    }
}