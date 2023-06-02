package com.example.uteapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uteapp.Model.User;
import com.example.uteapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    User user= new User();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextView name,phone,mail,pass,confirm_pass;
    Button signup,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AnhXa();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(confirm_pass.getText().toString())){
                    user.setTenUser(name.getText().toString());
                    user.setSDT(phone.getText().toString());
                    user.setEmail(mail.getText().toString());
                    user.setMatKhau(pass.getText().toString());
                    databaseReference.child("users").child(phone.getText().toString()).setValue(user);
                    Toast.makeText(SignUpActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Xác nhận mật khẩu sai",Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });

    }
    public void AnhXa(){
        name = findViewById(R.id.su_name);
        phone = findViewById(R.id.su_phone);
        mail = findViewById(R.id.su_email);
        pass = findViewById(R.id.su_password);
        confirm_pass = findViewById(R.id.su_confirm_password);
        signup = (Button) findViewById(R.id.su_btn_register);
        back=findViewById(R.id.su_back_SignIn);
    }
}