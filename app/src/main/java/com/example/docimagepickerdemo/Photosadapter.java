package com.example.docimagepickerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.docimagepickerdemo.photoeditor.RecyclerSelectedPhotosModel;
import com.example.docimagepickerdemo.photoeditor.SelectedPhotosEditActivity;
import com.example.docimagepickerdemo.photoeditor.SelectedPhotosModel;

import java.util.ArrayList;

public class Photosadapter extends RecyclerView.Adapter<Photosadapter.MyViewHolder> {
    SelectedPhotosEditActivity context;
    ArrayList<RecyclerSelectedPhotosModel> list;

    public Photosadapter(SelectedPhotosEditActivity context, ArrayList<RecyclerSelectedPhotosModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Photosadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_image_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Photosadapter.MyViewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get(position).getDoc_link())
                .placeholder(R.color.codeGray)
                .centerCrop()
                .into(holder.image);

        if(list.get(position).isSelected()){
           holder.ll_image.setBackground(context.getResources().getDrawable(R.drawable.green_border));
        }else{

            holder.ll_image.setBackground(context.getResources().getDrawable(R.drawable.button_border));
        }

        holder.ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.selectedImageClick(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RelativeLayout ll_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            ll_image = itemView.findViewById(R.id.ll_image);
        }
    }
}
