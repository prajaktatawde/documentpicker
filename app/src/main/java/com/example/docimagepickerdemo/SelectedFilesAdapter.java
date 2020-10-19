package com.example.docimagepickerdemo;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SelectedFilesAdapter extends RecyclerView.Adapter<SelectedFilesAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> list;

    public SelectedFilesAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public SelectedFilesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_image_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedFilesAdapter.MyViewHolder holder, final int position) {
       /* Glide.with(context)
                .load(list.get(position))
                .placeholder(R.color.codeGray)
                .centerCrop()
                .into(holder.image);
*/
        String fileName = list.get(position).substring(list.get(position).lastIndexOf('/') + 1);
        holder.txt_display_name.setText(fileName);

        if(list.get(position).endsWith(".pdf")){
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(holder.image);
        }else if(list.get(position).endsWith(".xls") || list.get(position).endsWith(".xlsx")){
            Glide.with(context)
                    .load(R.drawable.xls)
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(holder.image);
        }else if(list.get(position).endsWith(".doc") || list.get(position).endsWith(".docx")){
            Glide.with(context)
                    .load(R.drawable.doc)
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(holder.image);
        }else if(list.get(position).endsWith(".txt")){
            Glide.with(context)
                    .load(R.drawable.txt)
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(holder.image);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+list.get(position), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView txt_display_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            txt_display_name = itemView.findViewById(R.id.txt_display_name);
        }
    }
}
