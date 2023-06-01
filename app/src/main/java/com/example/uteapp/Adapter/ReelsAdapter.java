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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.uteapp.Model.Data;
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

import java.io.File;
import java.util.ArrayList;

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
        holder.setData(videosModels.get(position));

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


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable currentDrawable = holder.like.getDrawable();
                Drawable targetDrawable = holder.itemView.getContext().getResources().getDrawable(R.drawable.icon_favourite);
                if (currentDrawable.getConstantState().equals(targetDrawable.getConstantState())) {
                    holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println(snapshot.getRef());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    holder.like.setImageResource(R.drawable.icon_favourite);
                }
            }
        });
        holder.databaseReference.child("LikeCommentMedia").child(data.getParentKey()).child(data.getKey()).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
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
                final Dialog dialogAddPicVideo = new Dialog(context);
                dialogAddPicVideo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAddPicVideo.setContentView(R.layout.dialog_upload_picvideos_1);
                dialogAddPicVideo.show();
                dialogAddPicVideo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogAddPicVideo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAddPicVideo.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogAddPicVideo.getWindow().setGravity(Gravity.BOTTOM);
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
        VideoView videoView;
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
        void setData(PicVideos data){

//            videoView.setVideoURI(Uri.parse(data.getUrl()));
//            title.setText(data.getTitle());
//            des.setText(data.getDes());


//            videoView.setVisibility(View.GONE);
//            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
//            Uri uri = Uri.parse(data.getUrl());
//            MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context)).createMediaSource(uri);
//
//            player.setVideoSurfaceView(surfaceView);
//            player.setMediaSource(mediaSource);
//            player.prepare();
//            player.setPlayWhenReady(true);
//            player.addListener(new Player.EventListener() {
//                @Override
//                public void onLoadingChanged(boolean isLoading) {
//                    if (isLoading) {
//                        progressBar.setVisibility(View.VISIBLE);
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }
//            });
//            Glide.with(context)
//                    .load(data.getUrl())
//                    .downloadOnly(new SimpleTarget<File>() {
//                        @Override
//                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//                            // Lưu tệp vào bộ nhớ cache
//                        }
//                    });


//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    progressBar.setVisibility(View.GONE);
//                    mediaPlayer.start();
//                }
//            });
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    progressBar.setVisibility(View.GONE);
//                    mediaPlayer.start();
//                }
//            });
        }
    }
}
