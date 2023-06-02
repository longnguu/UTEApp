package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uteapp.Adapter.AddPicVideoAdapter;
import com.example.uteapp.Adapter.TabLayoutAdapter;
import com.example.uteapp.Fragment.Tab1Fragment;
import com.example.uteapp.Fragment.Tab1ShopFragment;
import com.example.uteapp.Fragment.Tab2Fragment;
import com.example.uteapp.Fragment.Tab2ShopFragment;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailsActivity extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    ImageView avt,bgr;
    TextView name,follow;
    TabLayoutAdapter tabLayoutAdapter;
    TabLayout tabLayout;
    CardView btn_choose;
    String UID;
    ViewPager2 viewPager;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    ProgressDialog progressDialog;
    TextView cP,cFer,cFing;
    List<PicVideos> data = new ArrayList<PicVideos>();
    int kt=0;


    private List<Uri> mediaUris = new ArrayList<>();
    AddPicVideoAdapter addPicVideoAdapter = new AddPicVideoAdapter(mediaUris,ShopDetailsActivity.this);
    final int GALLERY_REQUEST_CODE=124;
    private int[] tabIcons = {
            R.drawable.icon_view_module,
            R.drawable.icon_info
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        Anhxa();
        UID=getIntent().getStringExtra("uid");
        progressDialog = new ProgressDialog(ShopDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avtt = (String) snapshot.child("imgUS").getValue();
                String bgrr = String.valueOf(snapshot.child("anhnen").getValue());
                String namee = String.valueOf(snapshot.child("tenUser").getValue());
                Picasso.get().load(avtt).into(avt);
                Picasso.get().load(bgrr).into(bgr);
                name.setText(namee);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("users").child(UID).child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cFer.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("users").child(UID).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cFing.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Media").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cP.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("users").child(Data.dataPhone).child("following").addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(UID)){
                    follow.setText("Following");
                }else{
                    follow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(follow.getText().toString()=="Following"){
                    databaseReference.child("users").child(Data.dataPhone).child("following").child(UID).removeValue();
                    databaseReference.child("users").child(UID).child("follower").child(Data.dataPhone).removeValue();
                }else{
                    databaseReference.child("users").child(Data.dataPhone).child("following").child(UID).setValue(1);
                    databaseReference.child("users").child(UID).child("follower").child(Data.dataPhone).setValue(1);
                }
            }
        });
        String[] tabTitles = {"", ""};
        Fragment[] fragments = {new Tab1ShopFragment(), new Tab2ShopFragment()};
        TabLayoutAdapter adapter = new TabLayoutAdapter(ShopDetailsActivity.this, tabTitles, fragments);
        viewPager.setAdapter(adapter);
        adapter.setupWithTabLayout(tabLayout, viewPager);



        setupTabIcons();

    }
    public void Anhxa(){
        tabLayout = findViewById(R.id.shop_frhome_tab_layout);
        viewPager = findViewById(R.id.shop_frhome_view_pager);
        avt=findViewById(R.id.avtHome);
        bgr=findViewById(R.id.backgroundHome);
        name=findViewById(R.id.fr_home_name);
        btn_choose=findViewById(R.id.fthome_btn_choose);
        cP=findViewById(R.id.countPosts);
        cFer=findViewById(R.id.countFollower);
        cFing=findViewById(R.id.countFollowing);
        follow = findViewById(R.id.follow);
    }
    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}