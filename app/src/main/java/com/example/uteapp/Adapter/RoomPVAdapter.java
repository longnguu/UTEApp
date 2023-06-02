package com.example.uteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uteapp.Activity.ReelActivity;
import com.example.uteapp.Activity.RoomActivity;
import com.example.uteapp.Model.PicVideos;
import com.example.uteapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomPVAdapter extends RecyclerView.Adapter<RoomPVAdapter.ViewHolder> {
    private List<PicVideos> mData;
    private Context context;

    public RoomPVAdapter(List<PicVideos> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomPVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fr_home_lv, parent, false);
        return new RoomPVAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomPVAdapter.ViewHolder holder, int position) {
        PicVideos data= mData.get(position);
        holder.tabTitle.setText(data.getTitle().get(0));
        holder.a1.setVisibility(View.GONE);
        holder.a3.setVisibility(View.GONE);
        holder.a2.setVisibility(View.GONE);
        holder.a4.setVisibility(View.GONE);
        if (data.getSize()==1){
            holder.linearLayout.setVisibility(View.GONE);
            holder.c2.setVisibility(View.GONE);
        }

        if (data.getSize()>=1){
            holder.c1.setVisibility(View.VISIBLE);
            String dataVideo=data.getLink().get(0);
            if (data.getLoai().get(0).equals("video")){
                holder.v1.setVisibility(View.GONE);
                holder.a1.setVisibility(View.VISIBLE);
                holder.a1.setImageResource(R.drawable.icon_play);
            }else{
                holder.v1.setVisibility(View.GONE);
                holder.a1.setVisibility(View.VISIBLE);
                Picasso.get().load(dataVideo).into(holder.a1);
            }
        }
        if (data.getSize()>=2){
            holder.c2.setVisibility(View.VISIBLE);
            String dataVideo=data.getLink().get(1);
            if (data.getLoai().get(1).equals("video")){
                holder.v2.setVisibility(View.GONE);
                holder.a2.setVisibility(View.VISIBLE);
                holder.a2.setImageResource(R.drawable.icon_play);
            }else{
                holder.v2.setVisibility(View.GONE);
                holder.a2.setVisibility(View.VISIBLE);
                Picasso.get().load(dataVideo).into(holder.a2);
            }
        }
        if (data.getSize()>=3){
            holder.c3.setVisibility(View.VISIBLE);
            String dataVideo=data.getLink().get(2);
            if (data.getLoai().get(2).equals("video")){
                holder.v3.setVisibility(View.GONE);
                holder.a3.setVisibility(View.VISIBLE);
                holder.a3.setImageResource(R.drawable.icon_play);
            }else {
                holder.v3.setVisibility(View.GONE);
                holder.a3.setVisibility(View.VISIBLE);
                Picasso.get().load(dataVideo).into(holder.a3);
            }
        }
        if (data.getSize()>=4){
            holder.c4.setVisibility(View.VISIBLE);
            String dataVideo=data.getLink().get(3);
            if (data.getLoai().get(3).equals("video")){
                holder.v4.setVisibility(View.GONE);
                holder.a4.setVisibility(View.VISIBLE);
                holder.a4.setImageResource(R.drawable.icon_play);
            }else {
                holder.v4.setVisibility(View.GONE);
                holder.a4.setVisibility(View.VISIBLE);
                Picasso.get().load(dataVideo).into(holder.a4);
            }
        }
        holder.toreels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, ReelActivity.class);
                intent.putExtra("data",data.getLink().get(0));
                intent.putExtra("d","3");
                Activity activity= (Activity) context;
                intent.putExtra("roomKey", activity.getIntent().getStringExtra("roomid"));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout c1,c2,c3,c4;
        ImageView a1,a2,a3,a4;
        VideoView v1,v2,v3,v4;
        LinearLayout linearLayout,toreels;
        TextView tabTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tabTitle = itemView.findViewById(R.id.title_tab1fr);
            c1=itemView.findViewById(R.id.view1);
            c2=itemView.findViewById(R.id.view2);
            c3=itemView.findViewById(R.id.view3);
            c4=itemView.findViewById(R.id.view4);

            a1=itemView.findViewById(R.id.a1);
            a2=itemView.findViewById(R.id.a2);
            a3=itemView.findViewById(R.id.a3);
            a4=itemView.findViewById(R.id.a4);

            v1=itemView.findViewById(R.id.v1);
            v2=itemView.findViewById(R.id.v2);
            v3=itemView.findViewById(R.id.v3);
            v4=itemView.findViewById(R.id.v4);

            linearLayout=itemView.findViewById(R.id.liner_gone);
            toreels = itemView.findViewById(R.id.to_reels);
        }
    }
    public void update(List<PicVideos> mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
}

