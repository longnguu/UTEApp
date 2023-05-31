package com.example.uteapp.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uteapp.Activity.AddProductActivity;
import com.example.uteapp.Adapter.AddPicVideoAdapter;

import com.example.uteapp.Adapter.TabLayoutAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    ImageView avt,bgr;
    TextView name;
    TabLayoutAdapter tabLayoutAdapter;
    TabLayout tabLayout;
    CardView btn_choose;
    ViewPager2 viewPager;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    ProgressDialog progressDialog;
    List<PicVideos> data = new ArrayList<PicVideos>();
    int kt=0;


    private List<Uri> mediaUris = new ArrayList<>();
    AddPicVideoAdapter addPicVideoAdapter = new AddPicVideoAdapter(mediaUris,getContext());
    final int GALLERY_REQUEST_CODE=124;
    private int[] tabIcons = {
            R.drawable.icon_view_module,
            R.drawable.icon_info
    };


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Anhxa(view);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        databaseReference.child("users").child(Data.dataPhone).addListenerForSingleValueEvent(new ValueEventListener() {
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



        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        String[] tabTitles = {"", ""};
        Fragment[] fragments = {new Tab1Fragment(), new Tab2Fragment()};
        tabLayout = view.findViewById(R.id.frhome_tab_layout);
        viewPager = view.findViewById(R.id.frhome_view_pager);

        TabLayoutAdapter adapter = new TabLayoutAdapter(getActivity(), tabTitles, fragments);
        viewPager.setAdapter(adapter);
        adapter.setupWithTabLayout(tabLayout, viewPager);



        setupTabIcons();

    }
    public void Anhxa(View view){
        avt=view.findViewById(R.id.avtHome);
        bgr=view.findViewById(R.id.backgroundHome);
        name=view.findViewById(R.id.fr_home_name);
        btn_choose=view.findViewById(R.id.fthome_btn_choose);
    }
    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_bottom_sheet);

        LinearLayout choose1 = dialog.findViewById(R.id.choose_1);
        LinearLayout choose2 = dialog.findViewById(R.id.choose_2);
        LinearLayout choose3 = dialog.findViewById(R.id.choose_3);
        LinearLayout choose4 = dialog.findViewById(R.id.choose_4);

        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAddPicVideo = new Dialog(getActivity());
                dialog.dismiss();
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

        choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Share is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        choose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(getActivity(),"Upload is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        choose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), AddProductActivity.class));

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void selectIMG() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*"); // allow both images and videos
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedMediaUri = data.getData();
            String mimeType = getActivity().getContentResolver().getType(selectedMediaUri);
            if (mimeType.startsWith("image")) {
                // This is an image
                Uri imageUri = data.getData();
                mediaUris.add(imageUri);
                System.out.println("cbas");
            }if (mimeType.startsWith("video")) {
                // This is a video
                Uri videoUri = data.getData();
                mediaUris.add(videoUri);
            }

            System.out.println(mediaUris.size());
            addPicVideoAdapter.update(mediaUris);
            final Dialog dialogAddPicVideo1 = new Dialog(getActivity());
            dialogAddPicVideo1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogAddPicVideo1.setContentView(R.layout.dialog_upload_picvideos_2);
            RecyclerView recyclerView = dialogAddPicVideo1.findViewById(R.id.addpicvideo_recyclerView);
            addPicVideoAdapter = new AddPicVideoAdapter(mediaUris,getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(addPicVideoAdapter);
            System.out.println(mediaUris.size());
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
                        Toast.makeText(getContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_SHORT).show();
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
                                            String mimeType = getContext().getContentResolver().getType(mediaUri);
                                            if (mimeType.startsWith("image")) {
                                                databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("l").setValue("img");
                                                databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("link").setValue(downloadUrl);
                                            }else{
                                                databaseReference.child("Videos_Account").child(Data.dataPhone).child(time).child("url").setValue(downloadUrl);
                                                databaseReference.child("Videos_Account").child(Data.dataPhone).child(time).child("title").setValue(edttit.getText().toString());
                                                databaseReference.child("Videos_Account").child(Data.dataPhone).child(time).child("des").setValue(edtdes.getText().toString());
                                                databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("l").setValue("video");
                                                databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("link").setValue(downloadUrl);
                                            }
                                            databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("title").setValue(edttit.getText().toString());
                                            databaseReference.child("Media").child(Data.dataPhone).child(time).child(String.valueOf(j)).child("des").setValue(edtdes.getText().toString());
                                            kt++;
                                            linkList.add(downloadUrl);
                                            System.out.println(downloadUrl);
                                        });
                                    })
                                    .addOnFailureListener(exception -> {
                                        // File upload failed
                                        Log.e(TAG, "File upload failed: " + exception.getMessage());
                                        kt=-1;
                                        Toast.makeText(getContext(),"Đã xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }).addOnCompleteListener(task -> {
                                        // File upload completed (regardless of success or failure)
                                        if (kt==mediaUris.size()-1) {
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
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

}