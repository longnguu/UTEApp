package com.example.uteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uteapp.Model.MessengerList;
import com.example.uteapp.Model.RoomList;
import com.example.uteapp.Model.UserListRoom;
import com.example.uteapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NguoiDungRoomAdapter extends RecyclerView.Adapter<NguoiDungRoomAdapter.ViewHolder> {
    private List<UserListRoom> userListRooms;
    private final Context context;

    public NguoiDungRoomAdapter(List<UserListRoom> userListRooms, Context context) {
        this.userListRooms = userListRooms;
        this.context = context;
    }

    @NonNull
    @Override
    public NguoiDungRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NguoiDungRoomAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nd_room,null));
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiDungRoomAdapter.ViewHolder holder, int position) {
        UserListRoom userListRoom = userListRooms.get(position);
        holder.textView.setText(userListRoom.getName());
        Picasso.get().load(userListRoom.getAvt()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userListRooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.avtroomus);
            textView=itemView.findViewById(R.id.room_us_name);
        }
    }
    public void update(List<UserListRoom> sanPhams){
        this.userListRooms=sanPhams;
        notifyDataSetChanged();
    }
}
