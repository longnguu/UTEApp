package com.example.uteapp.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.Model.VideosModel;
import com.example.uteapp.R;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.ArrayList;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.MyViewHolder> {

    ArrayList<VideosModel> videosModels;
    Context context;

    public ReelsAdapter(ArrayList<VideosModel> videosModels, Context context) {
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
    }

    @Override
    public int getItemCount() {
        return videosModels.size();
    }
    public void updateAdapter(ArrayList<VideosModel> videosModel){
        this.videosModels = videosModel;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title;
        ProgressBar progressBar;
        SurfaceView surfaceView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.reesl_row_videoView);
            title = itemView.findViewById(R.id.reesl_row_textVideoTitle);
            progressBar = itemView.findViewById(R.id.reesl_row_videoProgressBar);
            surfaceView = itemView.findViewById(R.id.surface_view);
        }
        void setData(VideosModel data){
//            videoView.setVideoURI(Uri.parse(data.getUrl()));
            videoView.setVisibility(View.GONE);
            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
            Uri uri = Uri.parse(data.getUrl());
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context)).createMediaSource(uri);

            player.setVideoSurfaceView(surfaceView);
            player.setMediaSource(mediaSource);
            player.prepare();
            player.setPlayWhenReady(true);
            player.addListener(new Player.EventListener() {
                @Override
                public void onLoadingChanged(boolean isLoading) {
                    if (isLoading) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
//            Glide.with(context)
//                    .load(data.getUrl())
//                    .downloadOnly(new SimpleTarget<File>() {
//                        @Override
//                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//                            // Lưu tệp vào bộ nhớ cache
//                        }
//                    });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
        }
    }
}
