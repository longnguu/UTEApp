package com.example.uteapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uteapp.R;

import java.util.List;

public class AddPicVideoAdapter extends RecyclerView.Adapter<AddPicVideoAdapter.ViewHolder> {

    private List<Uri> mediaUris;
    Context context;
    public AddPicVideoAdapter(List<Uri> mediaUris, Context context) {
        this.mediaUris = mediaUris;
        this.context = context;
    }


    @NonNull
    @Override
    public AddPicVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_picvideos, parent, false);
        return new AddPicVideoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPicVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri mediaUri = mediaUris.get(position);
        String mimeType = context.getContentResolver().getType(mediaUri);
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoView.isPlaying()) {
                    // Pause video
                    holder.videoView.pause();
                } else {
                    // Start video
                    holder.videoView.start();
                }
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Tạo một hộp thoại xác nhận có yes/no
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn tiếp tục không?");

// Nút Yes
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi nhấn nút Yes
                        mediaUris.remove(position);
                        update(mediaUris);
                    }
                });

// Nút No
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi nhấn nút No
                    }
                });

// Hiển thị hộp thoại
                builder.show();

                return true;
            }
        });
        holder.videoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Tạo một hộp thoại xác nhận có yes/no
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn tiếp tục không?");

// Nút Yes
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi nhấn nút Yes
                        mediaUris.remove(position);
                        update(mediaUris);
                    }
                });

// Nút No
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi nhấn nút No
                    }
                });

// Hiển thị hộp thoại
                builder.show();
                return true;
            }
        });
        if (mimeType.startsWith("image")) {
            // Image type
            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setImageURI(mediaUri);
        } else {
            // Video type
            holder.videoView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVideoURI(mediaUri);
        }
    }

    public void update(List<Uri> uris){
        this.mediaUris=uris;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mediaUris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        VideoView videoView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            videoView=itemView.findViewById(R.id.videoview);
        }
    }

}
