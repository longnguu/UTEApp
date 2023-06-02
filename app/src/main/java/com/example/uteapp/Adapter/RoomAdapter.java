package com.example.uteapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uteapp.Activity.RoomActivity;
import com.example.uteapp.Model.Data;
import com.example.uteapp.Model.RoomList;
import com.example.uteapp.Model.SanPham;
import com.example.uteapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    List<RoomList> roomLists;
    Context context;

    public RoomAdapter(List<RoomList> roomLists, Context context) {
        this.roomLists = roomLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_group, parent, false);
        return new RoomAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RoomList roomList = roomLists.get(position);
        holder.databaseReference.child("Room").child(roomList.getKey()).child("thanhvien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getRef());
                holder.sl= String.valueOf(snapshot.getChildrenCount());
                holder.soLuong.setText(holder.sl+" thành viên");
                if (snapshot.hasChild(Data.dataPhone)){
                    holder.btn.setText("Vào");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tenNhom.setText(roomList.getTenNhom());
        holder.mota.setText(roomList.getMoTa());
        Picasso.get().load(roomList.getAvt()).into(holder.img);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.btn.getText().toString().equals("Vào")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Nhập mật khẩu");
                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấp vào nút "OK"
                            String password = input.getText().toString();
                            if (password.equals(roomList.getPass())){
                                holder.databaseReference.child("Room").child(roomList.getKey()).child("thanhvien").child(Data.dataPhone).setValue(1);
                                Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấp vào nút "Hủy"
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    Intent intent=new Intent(context, RoomActivity.class);
                    intent.putExtra("roomid",roomList.getKey());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenNhom,soLuong,mota,btn;
        ImageView img;
        String sl;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenNhom=itemView.findViewById(R.id.tenNhom);
            soLuong=itemView.findViewById(R.id.soThanhVien);
            img=itemView.findViewById(R.id.avtNhom);
            mota=itemView.findViewById(R.id.NhomMota);
            btn=itemView.findViewById(R.id.btnJoin);
        }
    }
    public void updateSanPham(List<RoomList> sanPhams){
        this.roomLists=sanPhams;
        notifyDataSetChanged();
    }
}
