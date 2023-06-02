package com.example.uteapp.Fragment;

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

import com.example.uteapp.Adapter.Tab1Adapter;
import com.example.uteapp.Adapter.Tab2Adapter;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2Fragment extends Fragment {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    Tab2Adapter adapter;
    List<PicVideos> data = new ArrayList<PicVideos>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Tab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2Fragment newInstance(String param1, String param2) {
        Tab2Fragment fragment = new Tab2Fragment();
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
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference.child("LikeInfor").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    int i=0;

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        i++;
                        PicVideos picVideos = new PicVideos();
                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            picVideos.setLink(dataSnapshot2.child("link").getValue(String.class));
                            picVideos.setLoai(dataSnapshot2.child("l").getValue(String.class));
                            picVideos.setTitle(dataSnapshot2.child("title").getValue(String.class));
                            picVideos.setDes(dataSnapshot2.child("des").getValue(String.class));
                            System.out.println(dataSnapshot2.getRef());
                        }
                        data.add(picVideos);
                        System.out.println(picVideos.getLink());
                        System.out.println("abc  "+picVideos.getSize());
                        System.out.println("abc  "+data.size());
                    }
                }
                adapter.update(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println(data.size());
        RecyclerView recyclerView = view.findViewById(R.id.recycler_tab2);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Tab2Adapter(data,getContext());
        adapter.update(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}