package com.example.docimagepickerdemo;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SelectedVideosAdapter extends RecyclerView.Adapter<SelectedVideosAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> stringArrayList;

    public SelectedVideosAdapter(Context context, ArrayList<String> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public SelectedVideosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_image_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedVideosAdapter.MyViewHolder holder, final int position) {
        Glide.with(context)
                .load(stringArrayList.get(position))
                .placeholder(R.color.codeGray)
                .centerCrop()
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringArrayList.get(position).endsWith(".mp4")) {
                    context.startActivity(new Intent(context, FullImageActivity.class)
                            .putExtra("image_image","image_video")
                            .putExtra("image", stringArrayList.get(position)));
                }else{
                    context.startActivity(new Intent(context, FullImageActivity.class)
                            .putExtra("image_image","image_image")
                            .putExtra("image", stringArrayList.get(position)));
                }
                //context.startActivity(new Intent(context, FullImageActivity.class).putExtra("image", stringArrayList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
