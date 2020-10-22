package com.example.docimagepickerdemo.photoeditor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docimagepickerdemo.Photosadapter;

import java.util.ArrayList;

public class SelectedPhotosAdapter extends RecyclerView.Adapter<SelectedPhotosAdapter.MyViewHolder> {
    Context context;
    ArrayList<RecyclerSelectedPhotosModel> list;

    public SelectedPhotosAdapter(Context context, ArrayList<RecyclerSelectedPhotosModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SelectedPhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPhotosAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
