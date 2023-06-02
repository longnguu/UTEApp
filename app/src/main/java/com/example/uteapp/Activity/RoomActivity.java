package com.example.uteapp.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uteapp.Adapter.AddPicVideoAdapter;
import com.example.uteapp.Adapter.NguoiDungRoomAdapter;
import com.example.uteapp.Adapter.RoomPVAdapter;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.Model.UserListRoom;
import com.example.uteapp.R;
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

public class RoomActivity extends AppCompatActivity {
    String roomKey;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    List<String> listKeys = new ArrayList<>();
    List<UserListRoom> userListRooms = new ArrayList<>();
    NguoiDungRoomAdapter nguoiDungRoomAdapter ;
    RecyclerView userRecyclerView,picVideoRecyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    RoomPVAdapter adapter;
    List<PicVideos> data = new ArrayList<PicVideos>();
    ProgressDialog progressDialog;
    TextView roomName;
    ImageView bgrr;
    CardView btn_choose;
    TextView RID;
    Dialog dialog;
    final int GALLERY_REQUEST_CODE=124213;
    int kt=0;
    private List<Uri> mediaUris = new ArrayList<>();
    AddPicVideoAdapter addPicVideoAdapter = new AddPicVideoAdapter(mediaUris,RoomActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        userRecyclerView = findViewById(R.id.nguoiDungRoom);
        roomName = findViewById(R.id.fr_room_name);
        bgrr = findViewById(R.id.backgroundRoom);
        RID=findViewById(R.id.roomID);
        btn_choose=findViewById(R.id.room_btn_choose);
        picVideoRecyclerView = findViewById(R.id.picVideoRecycler);
        nguoiDungRoomAdapter = new NguoiDungRoomAdapter(userListRooms, RoomActivity.this);
        linearLayoutManager = new GridLayoutManager(RoomActivity.this, 1, RecyclerView.HORIZONTAL, false);
        userRecyclerView.setAdapter(nguoiDungRoomAdapter);
        userRecyclerView.setLayoutManager(linearLayoutManager);

        roomKey = getIntent().getStringExtra("roomid");

        progressDialog = new ProgressDialog(RoomActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        recyclerPicVideos();


        registerForContextMenu(RID); // Đăng ký Context Menu cho TextView

        RID.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Hiển thị Context Menu khi long click vào TextView
                view.showContextMenu();
                return true;
            }
        });


        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogAddPicVideo = new Dialog(RoomActivity.this);
                dialogAddPicVideo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAddPicVideo.setContentView(R.layout.dialog_upload_picvideos_1);
                dialogAddPicVideo.show();
                dialogAddPicVideo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogAddPicVideo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAddPicVideo.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogAddPicVideo.getWindow().setGravity(Gravity.BOTTOM);
                Button btn_chonAnh = dialogAddPicVideo.findViewById(R.id.edt_btn_openGal);
                btn_chonAnh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddPicVideo.dismiss();
                        selectIMG();
                    }
                });
            }
        });

        databaseReference.child("Room").child(roomKey).child("thanhvien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listKeys.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    listKeys.add(key);
                }
                retrieveUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu có)
            }
        });

        databaseReference.child("Room").child(roomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomName.setText(snapshot.child("tenNhom").getValue(String.class));
                Picasso.get().load(snapshot.child("avt").getValue(String.class)).into(bgrr);
                RID.setText(snapshot.child("key").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu có)
            }
        });

    }
        private void retrieveUserData() {
            userListRooms.clear();
            for (String key : listKeys) {
                DatabaseReference nodeRef = databaseReference.child("users").child(key);
                nodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String tenUser = dataSnapshot.child("tenUser").getValue(String.class);
                            String imgUS = dataSnapshot.child("imgUS").getValue(String.class);
                            UserListRoom userListRoom = new UserListRoom(tenUser, imgUS, key);
                            userListRooms.add(userListRoom);
                            nguoiDungRoomAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi (nếu có)
                    }
                });
            }
        }
    private void selectIMG() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*"); // allow both images and videos
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RoomActivity.this.RESULT_OK && data != null) {
            Uri selectedMediaUri = data.getData();
            String mimeType = RoomActivity.this.getContentResolver().getType(selectedMediaUri);
            if (mimeType.startsWith("image")) {
                // This is an image
                Uri imageUri = data.getData();
                mediaUris.add(imageUri);
            }if (mimeType.startsWith("video")) {
                // This is a video
                Uri videoUri = data.getData();
                mediaUris.add(videoUri);
            }
            addPicVideoAdapter.update(mediaUris);
            final Dialog dialogAddPicVideo1 = new Dialog(RoomActivity.this);
            dialogAddPicVideo1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogAddPicVideo1.setContentView(R.layout.dialog_upload_picvideos_2);
            RecyclerView recyclerView = dialogAddPicVideo1.findViewById(R.id.addpicvideo_recyclerView);
            addPicVideoAdapter = new AddPicVideoAdapter(mediaUris,RoomActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(RoomActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(addPicVideoAdapter);
            if (!dialogAddPicVideo1.isShowing()) {
                dialogAddPicVideo1.show();
            }

            TextView btn_add = dialogAddPicVideo1.findViewById(R.id.addpicvideos_2_add);
            TextView btnSave=dialogAddPicVideo1.findViewById(R.id.addpicvideo_btn_save);
            EditText edtdes= dialogAddPicVideo1.findViewById(R.id.addpicvideo_edt_des);
            EditText edttit= dialogAddPicVideo1.findViewById(R.id.addpicvideo_edt_title);
            TextView btnCancel = dialogAddPicVideo1.findViewById(R.id.addpicvideo_btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaUris.clear();
                    addPicVideoAdapter.update(mediaUris);
                    dialogAddPicVideo1.dismiss();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    if (edttit.getText().equals("") || edtdes.getText().equals("")){
                        Toast.makeText(RoomActivity.this,"Vui lòng nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                    }else{
                        List<String> linkList = new ArrayList<>();
                        String time= String.valueOf(System.currentTimeMillis());
                        kt=0;
                        for (int i = 0; i < mediaUris.size(); i++) {
                            Uri mediaUri = mediaUris.get(i);
                            int j=i;
                            String fileName = "media_" + System.currentTimeMillis() + "_" + i;
                            StorageReference fileRef = storageReference.child(fileName);
                            fileRef.putFile(mediaUri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        // File uploaded successfully
                                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            // Get download URL
                                            String downloadUrl = uri.toString();
                                            String mimeType = RoomActivity.this.getContentResolver().getType(mediaUri);
                                            if (mimeType.startsWith("image")) {
                                                databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("l").setValue("img");
                                                databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("link").setValue(downloadUrl);
                                            }else{
                                                databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("l").setValue("video");
                                                databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("link").setValue(downloadUrl);
                                            }
                                            databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("title").setValue(edttit.getText().toString());
                                            databaseReference.child("RoomMedia").child(roomKey).child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("des").setValue(edtdes.getText().toString());
                                            kt++;
                                            linkList.add(downloadUrl);
                                        });
                                    })
                                    .addOnFailureListener(exception -> {
                                        // File upload failed
                                        Log.e(TAG, "File upload failed: " + exception.getMessage());
                                        kt=-1;
                                        Toast.makeText(RoomActivity.this,"Đã xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }).addOnCompleteListener(task -> {
                                        // File upload completed (regardless of success or failure)
                                        if (kt==mediaUris.size()-1) {
                                            Toast.makeText(RoomActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            dialogAddPicVideo1.dismiss();
                                        }
                                    });
                        }
                    }

                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIMG();
                }
            });

            dialogAddPicVideo1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogAddPicVideo1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogAddPicVideo1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogAddPicVideo1.getWindow().setGravity(Gravity.BOTTOM);
            dialogAddPicVideo1.setCancelable(false);

        }
    }
    public void recyclerPicVideos(){
        databaseReference.child("RoomMedia").child(roomKey).child("Media").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot2:snapshot.getChildren()) {
                    int i = 0;
                    data.clear();
                    for (DataSnapshot dataSnapshot : dataSnapshot2.getChildren()) {
                        i++;
                        PicVideos picVideos = new PicVideos();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            picVideos.setLink(dataSnapshot1.child("link").getValue(String.class));
                            picVideos.setLoai(dataSnapshot1.child("l").getValue(String.class));
                            picVideos.setTitle(dataSnapshot1.child("title").getValue(String.class));
                            picVideos.setDes(dataSnapshot1.child("des").getValue(String.class));
                        }
                        data.add(picVideos);
                    }
                }
                adapter.update(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(RoomActivity.this, 3, LinearLayoutManager.VERTICAL, false);

        picVideoRecyclerView.setLayoutManager(layoutManager);
        adapter = new RoomPVAdapter(data,RoomActivity.this);
        adapter.update(data);
        picVideoRecyclerView.setLayoutManager(layoutManager);
        picVideoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_copy) {
            // Sao chép nội dung của TextView vào Clipboard
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", RID.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    }