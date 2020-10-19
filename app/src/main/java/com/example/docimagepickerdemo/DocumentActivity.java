package com.example.docimagepickerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        context = this;

        list = (ArrayList<String>) getIntent().getSerializableExtra("multipleImage");
    }
}