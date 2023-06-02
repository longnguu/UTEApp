package com.example.uteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uteapp.Model.ChatList;
import com.example.uteapp.Model.CommentList;
import com.example.uteapp.Model.Data;
import com.example.uteapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentList> commentLists;
    private Context context;

    public CommentAdapter(List<CommentList> commentLists, Context context) {
        this.commentLists = commentLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentList commentList= new CommentList();
        commentList=commentLists.get(position);
        holder.name.setText(commentList.getName());
        holder.cmt.setText(commentList.getCmt());
        holder.time.setText(commentList.getDate()+" - "+ commentList.getTime());
        Activity activity= (Activity) context;
        if (commentList.getKeyy().equals(commentList.getDataKey())){
            holder.cardView.setVisibility(View.VISIBLE);
        }else
            holder.cardView.setVisibility(View.GONE);
        Picasso.get().load(commentList.getAvt()).into(holder.avt);
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }
    public void updateComment(List<CommentList> chatLists){
        this.commentLists=chatLists;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout myLayout,opoLayout;
        TextView name,cmt,time;
        CardView cardView;
        ImageView avt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.gchu);
            name =  itemView.findViewById(R.id.cmt_nameTextView);
            cmt =  itemView.findViewById(R.id.commentTextView);
            time =  itemView.findViewById(R.id.cmt_time);
            avt =  itemView.findViewById(R.id.cmt_avt);
        }
    }
}
