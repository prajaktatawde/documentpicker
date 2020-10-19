package com.example.docimagepickerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {

    ImageView myImage, back;
    VideoView vd_gallery;
    String image_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        myImage = findViewById(R.id.image);
        vd_gallery = findViewById(R.id.vd_gallery);
        back = findViewById(R.id.back);

        image_image = getIntent().getStringExtra("image_image");

        if(image_image.equals("image_image")){
            myImage.setVisibility(View.VISIBLE);
            vd_gallery.setVisibility(View.GONE);
            Glide.with(this)
                    .load(getIntent().getStringExtra("image"))
                    .placeholder(R.color.codeGray)
                    .into(myImage);
        }else{
            myImage.setVisibility(View.GONE);
            vd_gallery.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(getIntent().getStringExtra("image"));
            vd_gallery.setVideoURI(uri);
            vd_gallery.start();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }
}