package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uteapp.Model.Data;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    Button login,signup;
    TextView phone,pass;
    CheckBox checkBox_rememberUP;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AnhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("REMEMBER", false) == true) {
            phone.setText(sharedPreferences.getString("PHONE", ""));
            pass.setText(sharedPreferences.getString("PASSWORD", ""));
            checkBox_rememberUP.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String Phone = phone.getText().toString();
                String password = pass.getText().toString();
                if (Phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng không để trống!",
                            Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Phone)){
                                databaseReference.child("users").child(Phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.child("matKhau").getValue(String.class).equals(password)){
                                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công",
                                                    Toast.LENGTH_SHORT).show();
                                            String email= snapshot.child("email").getValue(String.class);
                                            String mobile= snapshot.child("sdt").getValue(String.class);
                                            String name= snapshot.child("tenUser").getValue(String.class);
                                            String imgUS= snapshot.child("imgUS").getValue(String.class);
                                            String anhnen= snapshot.child("anhnen").getValue(String.class);
                                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                            intent.putExtra("name",name);
                                            intent.putExtra("email",email);
                                            intent.putExtra("imgUS",imgUS);
                                            intent.putExtra("anhnen",anhnen);
                                            intent.putExtra("mobile",mobile);
                                            intent.putExtra("acti","Login");
                                            Data.dataPhone= String.valueOf(Phone);
                                            Data.dataAVT=String.valueOf(imgUS);
                                            Data.dataBGR=String.valueOf(anhnen);
                                            progressDialog.dismiss();
                                            System.out.println("abc"+mobile);
                                            startActivity(intent);
                                            if (checkBox_rememberUP.isChecked()) {
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("PHONE", Phone);
                                                editor.putString("PASSWORD", password);
                                                editor.putBoolean("REMEMBER", true);
                                                editor.commit();
                                            } else {
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("PHONE", Phone);
                                                editor.putString("PASSWORD", password);
                                                editor.putBoolean("REMEMBER", false);
                                                editor.commit();
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Sai thông tin tài khoản hoặc mật khẩu",
                                                    Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }else{
                                Toast.makeText(SignInActivity.this,"Tài khoản chưa tồn tại", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
    }
    public void AnhXa(){
        phone = findViewById(R.id.li_edt_phone);
        pass=findViewById(R.id.li_edt_password);
        login=findViewById(R.id.li_login_btn);
        signup=findViewById(R.id.li_signUp_btn);
        checkBox_rememberUP = findViewById(R.id.cb_savePassword);
    }
}