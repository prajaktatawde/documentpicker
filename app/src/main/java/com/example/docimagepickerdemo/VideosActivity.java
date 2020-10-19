package com.example.docimagepickerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideosActivity extends Activity {

    Context context;
    public static final int STORAGE_PERMISSION = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGES = 2;
    RecyclerView imageRecyclerView, selectedImageRecyclerView;
    Button done;
    String[] projection = {MediaStore.MediaColumns.DATA};
    ArrayList<ImageModel> imageList;
    ArrayList<String> selectedImageList;
    VideosAdapter videosAdapter;
    SelectedVideosAdapter selectedVideosAdapter;
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        context = this;
        if (isStoragePermissionGranted()) {
            init();
            getAllVideos();
            setVideosList();
            setSelectedVideosList();
        }

    }

    private void setSelectedVideosList() {
        selectedVideosAdapter = new SelectedVideosAdapter(this, selectedImageList);
        selectedImageRecyclerView.setAdapter(selectedVideosAdapter);
    }

    private void setVideosList() {
        videosAdapter = new VideosAdapter(getApplicationContext(), imageList);
        imageRecyclerView.setAdapter(videosAdapter);

        videosAdapter.setOnItemClickListener(new VideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                try {
                    if (!imageList.get(position).isSelected) {
                        selectImage(position);
                    } else {
                        unSelectImage(position);
                    }
                } catch (ArrayIndexOutOfBoundsException ed) {
                    ed.printStackTrace();
                }

            }
        });
     //   setImagePickerList();
    }

    // Add image in selectedImageList
    public void selectImage(int position) {
        // Check before add new item in ArrayList;
        if (!selectedImageList.contains(imageList.get(position).getImage())) {
            imageList.get(position).setSelected(true);
            selectedImageList.add(0,imageList.get(position).getImage());
            selectedVideosAdapter.notifyDataSetChanged();
            videosAdapter.notifyDataSetChanged();
        }
    }

    // Remove image from selectedImageList
    public void unSelectImage(int position) {
        for (int i = 0; i < selectedImageList.size(); i++) {
            if (imageList.get(position).getImage() != null) {
                if (selectedImageList.get(i).equals(imageList.get(position).getImage())) {
                    imageList.get(position).setSelected(false);
                    selectedImageList.remove(i);
                    selectedVideosAdapter.notifyDataSetChanged();
                    videosAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // get all images from external storage
    private void getAllVideos() {
        imageList.clear();
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            ImageModel ImageModel = new ImageModel();
            ImageModel.setImage(absolutePathOfImage);
            imageList.add(ImageModel);
        }
        cursor.close();

      /*  imageList.clear();
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

        // every column, although that is huge waste, you probably need
        // BaseColumns.DATA (the path) only.
        String[] projection = null;

        // exclude media files, they would be here also.
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
       // String [] mimeTypes = {"application/pdf","application/msword","application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/plain"};

        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        //String[] selectionArgs =new String[]{"application/pdf","application/msword","application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/plain"}; // there is no ? in selection so null here
        String[] selectionArgs =new String[]{mimeType}; // there is no ? in selection so null here

        String sortOrder = null; // unordered
        Cursor cursor = cr.query(uri, projection, selectionMimeType, selectionArgs, sortOrder);

        //Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            ImageModel ImageModel = new ImageModel();
            ImageModel.setImage(absolutePathOfImage);
            imageList.add(ImageModel);
        }
        cursor.close();*/
    }

    private void init() {
        selectedImageRecyclerView = findViewById(R.id.selected_recycler_view);
        imageRecyclerView = findViewById(R.id.recycler_view);
        done = findViewById(R.id.done);
        selectedImageList = new ArrayList<>();
        imageList = new ArrayList<>();

        imageRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        selectedImageRecyclerView.setLayoutManager(layoutManager);
    }


    public boolean isStoragePermissionGranted() {
        int ACCESS_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
            getAllVideos();
            setVideosList();
            setSelectedVideosList();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    addImage(mCurrentPhotoPath);
                }
            } else if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        getImageFilePath(uri);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    getImageFilePath(uri);
                }
            }
        }
    }


    // Get image file path
    public void getImageFilePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                if (absolutePathOfImage != null) {
                    checkImage(absolutePathOfImage);
                } else {
                    checkImage(String.valueOf(uri));
                }
            }
        }
    }
    public void checkImage(String filePath) {
        // Check before adding a new image to ArrayList to avoid duplicate images
        if (!selectedImageList.contains(filePath)) {
            for (int pos = 0; pos < imageList.size(); pos++) {
                if (imageList.get(pos).getImage() != null) {
                    if (imageList.get(pos).getImage().equalsIgnoreCase(filePath)) {
                        imageList.remove(pos);
                    }
                }
            }
            addImage(filePath);
        }
    }

    // add image in selectedImageList and imageList
    public void addImage(String filePath) {
        ImageModel imageModel = new ImageModel();
        imageModel.setImage(filePath);
        imageModel.setSelected(true);
        imageList.add(imageModel);
        selectedImageList.add(filePath);
        selectedVideosAdapter.notifyDataSetChanged();
        videosAdapter.notifyDataSetChanged();
    }
}