package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uteapp.Adapter.ViewPageAdapter;
import com.example.uteapp.Model.CartList;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.MessengerList;
import com.example.uteapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;
    ImageView topnavCart;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    List<CartList> cartLists = new ArrayList<>();
    CardView slgCart;
    TextView textSlgCart;
    ImageView iconMess, iconCart;
    public static CardView unSeenMain;
    public static TextView textUnSeenMain;

    @Override
    public void onStart() {
        super.onStart();
        if(Data.datalog == 0){
            Intent intent = new Intent(HomeActivity.this,MessengerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.black));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        unSeenMain = (CardView) findViewById(R.id.unseenMain);
        textUnSeenMain = (TextView) findViewById(R.id.textUnseenMain);

        iconMess = (ImageView) findViewById(R.id.topnavMess);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navnav);
        viewPager = (ViewPager) findViewById(R.id.viewpg);
        topnavCart = findViewById(R.id.topnavCart);
        slgCart = findViewById(R.id.slCartMain);
        textSlgCart = findViewById(R.id.textSLCart);
        topnavCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });

        iconMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MessengerActivity.class);
                intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navhome:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navsearch:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navroom:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navprofile:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.navqr:
                        viewPager.setCurrentItem(4);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.navhome).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.navsearch).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.navroom).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.navprofile).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.navqr).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        databaseReference.child("GioHang").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartLists.clear();
                int i=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String tenSP=dataSnapshot.child("tenSP").getValue(String.class);
                    String gia=dataSnapshot.child("gia").getValue(String.class);
                    String check=dataSnapshot.child("check").getValue(String.class);
                    String name=dataSnapshot.child("name").getValue(String.class);
                    String maSP=dataSnapshot.child("maSP").getValue(String.class);
                    String uid=dataSnapshot.child("uid").getValue(String.class);
                    String soLuongMua=dataSnapshot.child("soLuongMua").getValue(String.class);
                    String hinhanh=dataSnapshot.child("soLuongMua").getValue(String.class);
                    CartList cartList = new CartList(maSP,tenSP,soLuongMua,check,gia,uid,hinhanh);
                    cartLists.add(cartList);
                    i++;
                }
                if (i > 0) {
                    slgCart.setVisibility(View.VISIBLE);
                    textSlgCart.setText(String.valueOf(i));
                } else {
                    slgCart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageAdapter);
    }

    public static void updateUnSeen(List<MessengerList> messengerLists) {
        int i = 0;
        for (MessengerList messengerList : messengerLists) {
            if (messengerList.getUnseenMessenger() > 0) {
                i++;
            }
        }
        if (i > 0) {
            unSeenMain.setVisibility(View.VISIBLE);
            textUnSeenMain.setText(String.valueOf(i));
        } else {
            unSeenMain.setVisibility(View.GONE);
        }
    }
}