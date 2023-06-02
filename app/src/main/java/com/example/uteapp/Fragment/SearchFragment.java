package com.example.uteapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uteapp.Adapter.SanPhamAdapter;
import com.example.uteapp.Model.SanPham;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    public static ProgressDialog progressDialog;
    public static int check=0,getCheck=6;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static ArrayList<SanPham> sanPhams;
    public static SanPhamAdapter sanPhamAdapter;
    RecyclerView recyclerView,recyclerViewSP, recyclerViewDMSP;
    LinearLayoutManager linearLayoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        recyclerViewSP= view.findViewById(R.id.search_recyclerView);
        sanPhams=new ArrayList<>();
        sanPhamAdapter=new SanPhamAdapter(sanPhams,getContext());
        linearLayoutManager = new GridLayoutManager(getContext(),2, RecyclerView.VERTICAL,false);
        recyclerViewSP.setAdapter(sanPhamAdapter);
        recyclerViewSP.setLayoutManager(linearLayoutManager);

        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    public static void update(int pos){
        progressDialog.show();
        sanPhams=new ArrayList<>();
        databaseReference.child("SanPham1").orderByKey().limitToLast(getCheck).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=getCheck;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    if(Integer.parseInt(dataSnapshot1.child("loai").getValue(String.class))==pos){
                        String ten= dataSnapshot1.child("ten").getValue(String.class);
                        SanPham sanPham = new SanPham(ten);
                        sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                        sanPham.setMaSP(dataSnapshot1.getKey());
                        sanPham.setUID(dataSnapshot1.child("uid").getValue(String.class));
                        sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                        sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                        sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                        sanPham.setSoluongban(dataSnapshot1.child("soluongban").getValue(String.class));
                        sanPhams.add(sanPham);
                    }
                    sanPhamAdapter.updateSanPham(sanPhams);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }
    public static void update(){
        progressDialog.show();
        int i=0;
        sanPhams=new ArrayList<>();
        databaseReference.child("SanPham1").orderByKey().limitToLast(getCheck).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    String ten = dataSnapshot1.child("ten").getValue(String.class);
                    SanPham sanPham = new SanPham(ten);
                    sanPham.setImg(dataSnapshot1.child("img").getValue(String.class));
                    sanPham.setMaSP(dataSnapshot1.getKey());
                    sanPham.setUID(dataSnapshot1.child("uid").getValue(String.class));
                    sanPham.setMota(dataSnapshot1.child("mota").getValue(String.class));
                    sanPham.setGia(dataSnapshot1.child("gia").getValue(String.class));
                    sanPham.setDaBan(dataSnapshot1.child("daBan").getValue(String.class));
                    sanPham.setSoluongban(dataSnapshot1.child("soluongban").getValue(String.class));
                    sanPhams.add(sanPham);
                }
                sanPhamAdapter.updateSanPham(sanPhams);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}