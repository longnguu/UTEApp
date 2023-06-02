package com.example.uteapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.uteapp.Activity.ChatActivity;
import com.example.uteapp.Model.CommentList;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.MemoryData;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.Model.VideosModel;
import com.example.uteapp.R;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.MyViewHolder> {

    ArrayList<PicVideos> videosModels;
    Context context;

    public ReelsAdapter(ArrayList<PicVideos> videosModels, Context context) {
        this.videosModels = videosModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reels_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.progressBar.setVisibility(View.GONE);
        PicVideos data= videosModels.get(position);
        holder.des.setText(data.getDes().get(0));
        holder.title.setText(data.getTitle().get(0));
        Picasso.get().load(data.getAvt()).into(holder.avt);
        System.out.println("kkk"+data.getAvt());
        if (data.getTitle().get(0)!="")
            System.out.println(data.getTitle());
        holder.photoAdapter= new PhotoAdapter(context,data.getLoai(),data.getLink());
        holder.viewPager.setAdapter(holder.photoAdapter);
        holder.circleIndicator.setViewPager(holder.viewPager);

        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").child(Data.dataPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    System.out.println(dataSnapshot.getRef());
                    holder.like.setImageResource(R.drawable.icon_favourite_red);
                } else {
                    holder.like.setImageResource(R.drawable.icon_favourite);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long likeValue = Long.valueOf(String.valueOf(snapshot.getChildrenCount()));
                // Do something with the retrieved value
                if (likeValue!=null){
                    holder.tlike.setText(likeValue.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Update1(holder,data);



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable currentDrawable = holder.like.getDrawable();
                Drawable targetDrawable = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_favourite);
                if (currentDrawable.getConstantState().equals(targetDrawable.getConstantState())) {
                    holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.databaseReference1.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").child(Data.dataPhone).setValue("1");
                            Long likeValue = Long.valueOf(String.valueOf(snapshot.getChildrenCount()));
                            if (likeValue!=null){
                                holder.tlike.setText(likeValue.toString());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference sourceRef = firebaseDatabase.getReference("Media/"+data.getParentKey()+"/"+data.getKey());

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey(); // Lấy key của phần tử con
                                    String des = snapshot.child("des").getValue(String.class);
                                    String l = snapshot.child("l").getValue(String.class);
                                    String link = snapshot.child("link").getValue(String.class);
                                    String title = snapshot.child("title").getValue(String.class);

                                    // Tạo địa chỉ đích dựa trên key của phần tử con
                                    DatabaseReference destinationRef = firebaseDatabase.getReference("LikeInfor/"+Data.dataPhone+"/"+data.getParentKey()+"/"+data.getKey()+"/" + key);
                                    destinationRef.child("des").setValue(des);
                                    destinationRef.child("l").setValue(l);
                                    destinationRef.child("link").setValue(link);
                                    destinationRef.child("title").setValue(title);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu cần thiết
                        }
                    };
                    sourceRef.addValueEventListener(valueEventListener);

                    holder.like.setImageResource(R.drawable.icon_favourite_red);
                } else {
                    holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.databaseReference1.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").child(Data.dataPhone).removeValue();
                            Long likeValue = Long.valueOf(String.valueOf(snapshot.getChildrenCount()));
                            if (likeValue!=null){
                                holder.tlike.setText(likeValue.toString());
                            }
                            holder.databaseReference.child("LikeInfor").child(Data.dataPhone).child(data.getParentKey()).child(data.getKey()).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    holder.like.setImageResource(R.drawable.icon_favourite);
                }
            }
        });
        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getRef());
                Long likeValue = Long.valueOf(String.valueOf(snapshot.getChildrenCount()));
                // Do something with the retrieved value
                if (likeValue!=null){
                    holder.tcmt.setText(likeValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogCMT = new Dialog(context);
                dialogCMT.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCMT.setContentView(R.layout.dialog_comment);
                dialogCMT.show();
                dialogCMT.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogCMT.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCMT.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogCMT.getWindow().setGravity(Gravity.BOTTOM);
                RecyclerView dialogRecyclerView;

                dialogRecyclerView=dialogCMT.findViewById(R.id.dialog_recyclerView);
                dialogRecyclerView.setHasFixedSize(true);
                dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                dialogRecyclerView.scrollToPosition(holder.commentLists.size()-1);


                holder.commentAdapter = new CommentAdapter(holder.commentLists,context);
                dialogRecyclerView.setAdapter(holder.commentAdapter);


                ImageView btnSend=dialogCMT.findViewById(R.id.cmt_sendBtn);
                EditText edt=dialogCMT.findViewById(R.id.cmt_edtCMT);
                Update1(holder,data);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String getTxtMessenger = edt.getText().toString();
                        final String currentTimeStamp= String.valueOf(System.currentTimeMillis());
                        CommentList commentList1=new CommentList();
                        commentList1.setKeyy(Data.dataPhone);
                        commentList1.setCmt(getTxtMessenger);
                        commentList1.setTime(currentTimeStamp);
                        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("comment").child(currentTimeStamp).setValue(commentList1);
                        edt.setText("");
                        dialogRecyclerView.scrollToPosition(holder.commentLists.size()-1);
                        Update1(holder,data);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return videosModels.size();
    }
    public void updateAdapter(ArrayList<PicVideos> videosModel){
        this.videosModels = videosModel;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        List<CommentList> commentLists=new ArrayList<>();
        CommentAdapter commentAdapter=new CommentAdapter(commentLists,context);
        VideoView videoView;
        Long cmtValue;
        TextView title,des,tlike,tcmt;
        ProgressBar progressBar;
        SurfaceView surfaceView;
        ViewPager viewPager;
        PhotoAdapter photoAdapter;
        CircleIndicator circleIndicator;
        ImageView avt,like,cmt;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.reesl_row_videoView);
            title = itemView.findViewById(R.id.reesl_row_textVideoTitle);
            des=itemView.findViewById(R.id.reesl_row_textVideoDescription);
            progressBar = itemView.findViewById(R.id.reesl_row_videoProgressBar);
            surfaceView = itemView.findViewById(R.id.surface_view);
            viewPager=itemView.findViewById(R.id.reels_row_ViewPage);
            circleIndicator = itemView.findViewById(R.id.circleindicator);
            avt=itemView.findViewById(R.id.avtReel);
            like=itemView.findViewById(R.id.reesl_row_favorites);
            tlike=itemView.findViewById(R.id.reesl_row_favorites_txt);
            cmt=itemView.findViewById(R.id.reesl_row_cmt);
            tcmt=itemView.findViewById(R.id.reesl_row_cmt_txt);


        }
    }
    void Update(MyViewHolder holder,PicVideos data){
        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long cmtValue=snapshot.getChildrenCount();
                List<CommentList> commentListsss = new ArrayList<>();
                holder.commentLists.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CommentList commentList = new CommentList();
                    commentList.setCmt(dataSnapshot.child("cmt").getValue().toString());
                    commentList.setKeyy(dataSnapshot.child("keyy").getValue().toString());
                    Timestamp timestamp = new Timestamp(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
                    commentList.setCmt(dataSnapshot.child("cmt").getValue().toString());
                    commentList.setKeyy(dataSnapshot.child("keyy").getValue().toString());
                    commentList.setTime(simpleTimeFormat.format(timestamp));
                    commentList.setDate(simpleDateFormat.format(timestamp));
                    holder.databaseReference1.child("users").child(commentList.getKeyy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            commentList.setName(snapshot.child("tenUser").getValue().toString());
                            commentList.setAvt(snapshot.child("imgUS").getValue().toString());
//                            if(holder.commentLists.size()>cmtValue) {
//                                System.out.println(cmtValue+"cmtV");
//                                holder.commentLists.clear();
//                                holder.commentLists.add(commentListsss.get(0));
//                                System.out.println(commentListsss.get(0).getCmt()+"  cmt0");
//                            }
                            commentListsss.add(commentList);
                            System.out.println(commentListsss.size()+"  "+holder.commentLists.size()+"  "+cmtValue);
                            holder.commentLists.add(commentList);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                holder.commentAdapter.updateComment(holder.commentLists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void Update1(MyViewHolder holder,PicVideos data) {
        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long cmtValue = snapshot.getChildrenCount();
                List<CommentList> commentLists = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CommentList commentList = new CommentList();
                    commentList.setCmt(dataSnapshot.child("cmt").getValue().toString());
                    commentList.setKeyy(dataSnapshot.child("keyy").getValue().toString());
                    Timestamp timestamp = new Timestamp(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
                    commentList.setTime(simpleTimeFormat.format(timestamp));
                    commentList.setDate(simpleDateFormat.format(timestamp));
                    holder.databaseReference1.child("users").child(commentList.getKeyy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            CommentList updatedCommentList = new CommentList();
                            updatedCommentList.setCmt(commentList.getCmt());
                            updatedCommentList.setKeyy(commentList.getKeyy());
                            updatedCommentList.setTime(commentList.getTime());
                            updatedCommentList.setDate(commentList.getDate());
                            updatedCommentList.setName(snapshot.child("tenUser").getValue().toString());
                            updatedCommentList.setAvt(snapshot.child("imgUS").getValue().toString());
                            commentLists.add(updatedCommentList);

                            if (commentLists.size() == cmtValue) {
                                holder.commentLists.clear();
                                holder.commentLists.addAll(commentLists);
                                holder.commentAdapter.updateComment(holder.commentLists);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }
