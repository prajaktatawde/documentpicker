package com.example.docimagepickerdemo.photoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.docimagepickerdemo.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBaseActivity extends AppCompatActivity {

    Context context;
    ViewPager viewpager;
    RecyclerView rv_photos;
    LinearLayoutManager layoutManager;
    ArrayList<SelectedPhotosModel> list;
    ImagePagerAdapter imagePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

        context = this;

        list = (ArrayList<SelectedPhotosModel>) getIntent().getExtras().getSerializable("multipleImage");
        initView();
    }

    private void initView() {

        rv_photos = findViewById(R.id.rv_photos);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        rv_photos.setLayoutManager(layoutManager);

        viewpager = findViewById(R.id.viewpager);
        setupViewPager(viewpager, list);

    }

    private void setupViewPager(ViewPager viewpager,List<SelectedPhotosModel> list) {
        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), getFragments(list), list);
        //imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(imagePagerAdapter);
    }


    private List<Fragment> getFragments(List<SelectedPhotosModel> subCategory) {
        List<Fragment> fList = new ArrayList<Fragment>();
        for (int i = 0; i < subCategory.size(); i++) {
           // fList.add(new SelectedImagesFragment(subCategory.get(i).getDoc_link()));
            fList.add(new SelectedImagesFragment(subCategory.get(i)));
        }
        return fList;
    }

    private class ImagePagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        List<SelectedPhotosModel> list;

        public ImagePagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<SelectedPhotosModel> list) {
            super(fm);
            this.fragments = fragments;
            this.list = list;
        }

        public ImagePagerAdapter(FragmentManager fm, List<SelectedPhotosModel> list) {
            super(fm);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
          // return new SelectedImagesFragment(list);
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}