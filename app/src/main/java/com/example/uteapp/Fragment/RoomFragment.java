package com.example.uteapp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.uteapp.Activity.AddProductActivity;
import com.example.uteapp.Activity.EditProfileActivity;
import com.example.uteapp.Adapter.RoomAdapter;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.RoomList;
import com.example.uteapp.Model.SanPham;
import com.example.uteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment {
    CardView btnTaoNhom;
    final int GALLERY_REQUEST_CODE=1248214;
    ImageView img;
    CardView cardViewAVT;
    Bitmap rotareBitmap=null;
    LinearLayoutManager linearLayoutManager;
    Dialog dialog;
    List<RoomList> roomLists=new ArrayList<>();
    RoomAdapter roomAdapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    String fileUrl;
    SearchView searchView;
    CardView cardViewRecy;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String param2) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.RoomSearchView);
        btnTaoNhom = view.findViewById(R.id.froom_btn_choose);
        recyclerView=view.findViewById(R.id.RoomRecyclerView);
        cardViewRecy = (CardView) view.findViewById(R.id.cardrecycler);
        roomAdapter = new RoomAdapter(roomLists,getContext());
        linearLayoutManager = new GridLayoutManager(getContext(),1, RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(roomAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                callQuery(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                callQuery(s);
                return false;
            }
        });

        databaseReference.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomLists.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    RoomList roomList = dataSnapshot.getValue(RoomList.class);
                    roomLists.add(roomList);
                }
                roomAdapter.updateSanPham(roomLists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnTaoNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }


    private void showDialog() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_taonhom);

        EditText ten,mota,pass,cfpass;
        Button open,addRoom;
        pass=dialog.findViewById(R.id.txtPassNhom);
        cfpass=dialog.findViewById(R.id.txtcfPassNhom);
        ten=dialog.findViewById(R.id.txtTaoTenNhom);
        mota=dialog.findViewById(R.id.txtTaoMoTa);
        open=dialog.findViewById(R.id.btn_themAnh);
        addRoom=dialog.findViewById(R.id.btnTaoNhom);
        img=dialog.findViewById(R.id.avtNhom);
        cardViewAVT=dialog.findViewById(R.id.cvAVTNhom);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIMG();
            }
        });
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ten.getText().toString() != null && mota.getText().toString() != null && pass.getText().toString() != null && pass.getText().toString().equals(cfpass.getText().toString()) && rotareBitmap!=null){
                    RoomList roomList= new RoomList();
                    roomList.setTenNhom(ten.getText().toString());
                    roomList.setMoTa(mota.getText().toString());
                    roomList.setPass(pass.getText().toString());
                    roomList.setChu(Data.dataPhone);
                    Calendar calendar = Calendar.getInstance();
                    storageReference.child("image" + calendar.getTimeInMillis() + ".png");
                    roomList.setKey(String.valueOf(calendar.getTimeInMillis()));
                    img.setDrawingCacheEnabled(true);
                    img.buildDrawingCache();
                    Bitmap bitmap = img.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageReference.child("image" + calendar.getTimeInMillis() + ".png").putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Lấy đường dẫn của tệp tin đã tải lên
                                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            fileUrl = uri.toString();
                                            roomList.setAvt(fileUrl);
                                            databaseReference.child("Room").child(String.valueOf(calendar.getTimeInMillis())).setValue(roomList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getContext(),"Thành công",Toast.LENGTH_SHORT);
                                                }
                                            });
                                            databaseReference.child("Room").child(String.valueOf(calendar.getTimeInMillis())).child("thanhvien").child(Data.dataPhone).setValue(1);
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý lỗi tải lên tại đây
                                }
                            });
                }else{
                    Toast.makeText(getContext(),"Có lỗi xảy ra. Vui lòng nhập đủ thông tin",Toast.LENGTH_SHORT);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if(resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQUEST_CODE && data !=null && data.getData()!=null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    rotareBitmap = rotateImageIfRequired(getActivity(),bitmap,data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            img.setImageBitmap(rotareBitmap);
            cardViewAVT.setVisibility(View.VISIBLE);
        }
    }

    private void selectIMG() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*"); // allow both images and videos
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);

        // Chỉnh lại kích thước của bitmap nếu nó không đủ rộng hoặc cao
        if (width > height && rotatedBitmap.getHeight() < width) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, width, height, true);
            rotatedBitmap.recycle();
            rotatedBitmap = scaledBitmap;
        } else if (height > width && rotatedBitmap.getWidth() < height) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, width, height, true);
            rotatedBitmap.recycle();
            rotatedBitmap = scaledBitmap;
        }

        return rotatedBitmap;
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
    private void callQuery(String s) {
        databaseReference.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = dataSnapshot.child("tenNhom").getValue(String.class).toUpperCase();
                    String namee = dataSnapshot.child("key").getValue(String.class).toUpperCase();
                    if (s == "" ||s== null|| name.contains(s.toUpperCase()) || namee.contains(s.toUpperCase())) {
                        RoomList roomList = dataSnapshot.getValue(RoomList.class);
                        roomLists.add(roomList);
                    }
                }
                roomAdapter.updateSanPham(roomLists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}