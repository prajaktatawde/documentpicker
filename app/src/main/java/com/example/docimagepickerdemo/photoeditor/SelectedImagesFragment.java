package com.example.docimagepickerdemo.photoeditor;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.docimagepickerdemo.R;

import java.util.List;

public class SelectedImagesFragment extends Fragment {

    Context context;
    SelectedPhotosModel selectedPhotosModel;
    ImageView photo_edit_iv;
    List<SelectedPhotosModel> subCategory;
    String selectedPhotosModelString;

    public SelectedImagesFragment(List<SelectedPhotosModel> subCategory) {
        // Required empty public constructor
        this.subCategory = subCategory;
    }

    public SelectedImagesFragment(String selectedPhotosModel) {
        this.selectedPhotosModelString = selectedPhotosModel;
    }

    public SelectedImagesFragment(SelectedPhotosModel selectedPhotosModel) {
        this.selectedPhotosModel = selectedPhotosModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        context = getActivity();

        initView(view);
        return view;
    }

    private void initView(View view) {
        photo_edit_iv = view.findViewById(R.id.photo_edit_iv);


       /* Glide.with(context)
                .load(selectedPhotosModel)
                .placeholder(R.color.codeGray)
                .centerCrop()
                .into(photo_edit_iv);*/
    }
}