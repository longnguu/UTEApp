package com.example.uteapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.SanPham;
import com.example.uteapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ImageView imageView;
    EditText tenSP,moTa,gia,soLuong,loaiSP;
    ProgressDialog progressDialog;
    String mobile;
    ArrayAdapter<String> adapter;
    List<String> dmmmm=new ArrayList<>();
    Button pick ,cam;
    Button save;
    LinearLayout linearLayout;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        AnhXa();
        pick = (Button) findViewById(R.id.edt_btn_openGal);
        cam = (Button) findViewById(R.id.edt_btn_openCam);
        save=(Button) findViewById(R.id.saveQLSP);
        linearLayout = findViewById(R.id.addproduct_linearbtn);
        imageView =  findViewById(R.id.addproduct_imgView);
        mobile= Data.dataPhone;

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMG();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    progressDialog.show();
                    Calendar calendar = Calendar.getInstance();
                    storageReference.child("image" + calendar.getTimeInMillis() + ".png");
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageReference.child("image" + calendar.getTimeInMillis() + ".png").putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            if (imageUrl.isEmpty()) {
                                                imageUrl = "https://firebasestorage.googleapis.com/v0/b/demotmdt-26982.appspot.com/o/error-image-generic.png?alt=media&token=dbfe9456-ba97-458f-8abf-dfd6e644dd25";
                                            }
                                            SanPham sanPham = new SanPham(tenSP.getText().toString(), soLuong.getText().toString(), gia.getText().toString(), moTa.getText().toString(), imageUrl, loaiSP.getText().toString().split(" ")[0]);
                                            final String currentTimeStamp = String.valueOf(System.currentTimeMillis());
                                            sanPham.setMaSP(currentTimeStamp);
                                            sanPham.setDaBan("0");
                                            sanPham.setUID(mobile);
                                            databaseReference.child("SanPham").child(mobile).child(currentTimeStamp).setValue(sanPham);
                                            databaseReference.child("SanPham1").child(currentTimeStamp).setValue(sanPham);
//                                            Intent intent = new Intent(ThemSanPhamActivity.this, QuanLySanPhamActivity.class);
//                                            intent.putExtra("email",getIntent().getStringExtra("email"));
//                                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
//                                            intent.putExtra("name",getIntent().getStringExtra("name"));
//                                            startActivity(intent);
                                            Toast.makeText(AddProductActivity.this, "Đã thêm.", Toast.LENGTH_SHORT).show();
                                            tenSP.setText("");
                                            soLuong.setText("");
                                            gia.setText("");
                                            moTa.setText("");
                                            loaiSP.setSelection(0);
                                            imageView.setImageDrawable(null);

                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else
                    Toast.makeText(AddProductActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMG();
            }
        });
    }

    private void AnhXa() {
        tenSP = findViewById(R.id.addSP_Ten);
        gia = findViewById(R.id.addSP_Gia);
        loaiSP = findViewById(R.id.addSP_Category);
        moTa =findViewById(R.id.addSP_Mota);
        soLuong = findViewById(R.id.addSP_SoLuong);
    }
    private boolean check(){
        if (tenSP.getText().toString().isEmpty() || gia.getText().toString().isEmpty()||moTa.getText().toString().isEmpty()||soLuong.getText().toString().isEmpty()){
            return false;
        }else return true;
    }

    private void selectIMG() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;
        Bitmap rotareBitmap=null;
        if(resultCode==RESULT_OK){

            if (requestCode==100 && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(AddProductActivity.this.getContentResolver(), data.getData());
                    rotareBitmap = rotateImageIfRequired(AddProductActivity.this,bitmap,data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(rotareBitmap);
                imageView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }

        }
    }
    public static Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, Uri uri) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(uri.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}