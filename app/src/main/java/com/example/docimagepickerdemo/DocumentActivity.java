package com.example.docimagepickerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.docimagepickerdemo.photoeditor.RecyclerSelectedPhotosModel;
import com.example.docimagepickerdemo.photoeditor.SelectedPhotosModel;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> list;
    ArrayList<RecyclerSelectedPhotosModel> selectedPhotosModels;
    RecyclerView rv_photos;
    LinearLayoutManager layoutManager;
    Photosadapter photosadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);


        selectedPhotosModels = (ArrayList<RecyclerSelectedPhotosModel>) getIntent().getSerializableExtra("multipleImage");
        context = this;

        rv_photos = findViewById(R.id.rv_photos);
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        rv_photos.setLayoutManager(layoutManager);
        /*photosadapter = new Photosadapter(context, selectedPhotosModels);
        rv_photos.setAdapter(photosadapter);*/

      //  list = (ArrayList<String>) getIntent().getSerializableExtra("multipleImage");
    }
}