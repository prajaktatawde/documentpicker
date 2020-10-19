package com.example.docimagepickerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.File;
import java.util.ArrayList;

public class FirstScreenActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    LinearLayout linearCamera, linearGallery, linearFile, linearVideo;
    ImageView image_view;
    private static final int RESULT_LOAD_File = 1000;
    ArrayList<String> selectedFilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        context = this;

        linearVideo = findViewById(R.id.linearVideo);
        linearVideo.setOnClickListener(this);
        linearCamera = findViewById(R.id.linearCamera);
        linearCamera.setOnClickListener(this);
        linearGallery = findViewById(R.id.linearGallery);
        linearGallery.setOnClickListener(this);
        linearFile = findViewById(R.id.linearFile);
        linearFile.setOnClickListener(this);
        image_view = findViewById(R.id.image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearCamera:
                //captureImage();
                break;

            case R.id.linearGallery:
                Intent intent = new Intent(context, GallerySelectionActivity.class);
                intent.putExtra("type", "image");
                startActivity(intent);
                break;

            case R.id.linearVideo:
                Intent intent1 = new Intent(context, VideosActivity.class);
                intent1.putExtra("type", "video");
                startActivity(intent1);
                break;

            case R.id.linearFile:
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = {"application/pdf", "application/msword", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain"};
                intent2.setType("*/*");
                intent2.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent2, RESULT_LOAD_File);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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