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
 * Use the {@link Tab1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1Fragment extends Fragment {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    Tab1Adapter adapter;
    List<PicVideos> data = new ArrayList<PicVideos>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Tab1Fragment() {
        // Required empty public constructor
    }
    public Tab1Fragment(List<PicVideos> data) {
        // Required empty public constructor
        this.data=data;
        adapter=new Tab1Adapter(data,getContext());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1Fragment newInstance(String param1, String param2) {
        Tab1Fragment fragment = new Tab1Fragment();
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
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference.child("Media").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                data.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    i++;
                    PicVideos picVideos = new PicVideos();
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        picVideos.setLink(dataSnapshot1.child("link").getValue(String.class));
                        picVideos.setLoai(dataSnapshot1.child("l").getValue(String.class));
                        picVideos.setTitle(dataSnapshot1.child("title").getValue(String.class));
                        picVideos.setDes(dataSnapshot1.child("des").getValue(String.class));
                    }
                    data.add(picVideos);
                }
                adapter.update(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        System.out.println(data.size());
        RecyclerView recyclerView = view.findViewById(R.id.recycler_tab1);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 1;
//            }
//        });

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Tab1Adapter(data,getContext());
        adapter.update(data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}