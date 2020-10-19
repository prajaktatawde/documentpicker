package com.example.docimagepickerdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    private static final int RESULT_LOAD_File = 1000;
    LinearLayout linearCamera, linearGallery, linearFile, linearVideo;
    String[] projection = {MediaStore.MediaColumns.DATA};
    ArrayList<ImageModel> filesList;
    ArrayList<String> selectedFilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        linearCamera = findViewById(R.id.linearCamera);
        linearGallery = findViewById(R.id.linearGallery);
        linearFile = findViewById(R.id.linearFile);
        linearVideo = findViewById(R.id.linearVideo);

        linearGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImagesActivity.class);
                intent.putExtra("type", "image");
                startActivity(intent);
            }
        });

        linearVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideosActivity.class);
                intent.putExtra("type", "video");
                startActivity(intent);
            }
        });

        linearFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = {"application/pdf", "application/msword", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain"};
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, RESULT_LOAD_File);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_LOAD_File && resultCode == RESULT_OK) {
            if (null != data) { // checking empty selection
                Uri uri;
                selectedFilesList.clear();
                if (null != data.getClipData()) { // checking multiple selection or not
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        uri = data.getClipData().getItemAt(i).getUri();
                        String uriString = uri.toString();
                        File myFile = new File(uriString);
                        String path = myFile.getAbsolutePath();
                        String displayName = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            displayName = ImageFilePath.getPath(this, uri);
                        }
                        selectedFilesList.add(displayName);
                    }

                } else {
                    uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        displayName = ImageFilePath.getPath(this, uri);
                    }
                    selectedFilesList.add(displayName);
                }
                Intent intent=new Intent(getBaseContext(),FilesActivity.class);
                intent.putExtra("multipleImage",selectedFilesList);
                startActivity(intent);
            }



        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}