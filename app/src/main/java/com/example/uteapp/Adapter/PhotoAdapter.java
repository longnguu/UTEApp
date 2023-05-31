package com.example.uteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.example.uteapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends PagerAdapter {
    private Context mContext;
    List<String> loai = new ArrayList<>();
    List<String> link = new ArrayList<>();
    public PhotoAdapter(Context mContext, List<String> loai, List<String> link) {
        this.mContext = mContext;
        this.loai = loai;
        this.link=link;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.itemphoto,container,false);
        ImageView imgPhoto =  view.findViewById(R.id.img_photo_banner);
        VideoView videoView = view.findViewById(R.id.video_banner);
        ProgressBar progressBar = view.findViewById(R.id.progressPTA);

        String lo= loai.get(position);
        String li = link.get(position);
            if (lo.equals("video")){
                videoView.setVideoURI(Uri.parse(li));
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
                videoView.setVisibility(View.VISIBLE);
                imgPhoto.setVisibility(View.GONE);
            }else {
                Picasso.get().load(li).into(imgPhoto);
                videoView.setVisibility(View.GONE);
                imgPhoto.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (loai!=null)
            return loai.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    public void setVideo(String data,VideoView v){
        MediaController mediaController = new MediaController(mContext);
        mediaController.setAnchorView(v);
        v.setMediaController(mediaController);
        Uri videoUri = Uri.parse(data);
        v.setVideoURI(videoUri);
    }

}
