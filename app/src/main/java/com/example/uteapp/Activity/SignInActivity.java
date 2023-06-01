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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressDialog progressDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Data.dataPhone=FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


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
                String email = phone.getText().toString();
                String password = pass.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng không để trống!",
                            Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Data.dataPhone= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                    databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Toast.makeText(getApplicationContext(), "Đăng nhập thành công",
                                                                        Toast.LENGTH_SHORT).show();
                                                                String email= snapshot.child("email").getValue(String.class);
                                                                String imgUS= snapshot.child("imgUS").getValue(String.class);
                                                                String anhnen= snapshot.child("anhnen").getValue(String.class);
                                                                Data.dataPhone= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                Data.dataAVT=String.valueOf(imgUS);
                                                                Data.dataBGR=String.valueOf(anhnen);
                                                                progressDialog.dismiss();
                                                                if (checkBox_rememberUP.isChecked()) {
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("PHONE", email);
                                                                    editor.putString("PASSWORD", password);
                                                                    editor.putBoolean("REMEMBER", true);
                                                                    editor.commit();
                                                                } else {
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("PHONE", email);
                                                                    editor.putString("PASSWORD", password);
                                                                    editor.putBoolean("REMEMBER", false);
                                                                    editor.commit();
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
//                                        Toast.makeText(SignInActivity.this, "Login successful.",
//                                                Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignInActivity.this, "Failed to login! Please check your credentials!",
                                                Toast.LENGTH_LONG).show();
                                    }
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