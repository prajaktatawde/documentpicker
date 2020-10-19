package com.example.docimagepickerdemo;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilesActivity extends Activity {

    Context context;
    ArrayList<String> list;
    Button done;
    RecyclerView selected_recycler_view;
    SelectedFilesAdapter filesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        context = this;

        list = (ArrayList<String>) getIntent().getSerializableExtra("multipleImage");

        init();

    }

    private void init() {
        done = findViewById(R.id.done);
        selected_recycler_view = findViewById(R.id.selected_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        selected_recycler_view.setLayoutManager(layoutManager);
        filesAdapter = new SelectedFilesAdapter(this, list);
        selected_recycler_view.setAdapter(filesAdapter);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}