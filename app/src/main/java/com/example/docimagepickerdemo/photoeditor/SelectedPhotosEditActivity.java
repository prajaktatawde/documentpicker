package com.example.docimagepickerdemo.photoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedadeltito.photoeditorsdk.BrushDrawingView;
import com.ahmedadeltito.photoeditorsdk.OnPhotoEditorSDKListener;
import com.ahmedadeltito.photoeditorsdk.PhotoEditorSDK;
import com.ahmedadeltito.photoeditorsdk.ViewType;
import com.bumptech.glide.Glide;
import com.example.docimagepickerdemo.DocumentActivity;
import com.example.docimagepickerdemo.Photosadapter;
import com.example.docimagepickerdemo.R;
import com.example.docimagepickerdemo.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.PageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SelectedPhotosEditActivity extends AppCompatActivity implements View.OnClickListener, OnPhotoEditorSDKListener {

    Context context;
    ViewPager viewPager;
    RecyclerView rv_photos;
    ArrayList<String> list;
    ArrayList<SelectedPhotosModel> selectedPhotosModels = new ArrayList<>();
    ArrayList<RecyclerSelectedPhotosModel> recyclerSelectedPhotosModels = new ArrayList<>();
    SelectedPhotosModel selectedmodel;
    RecyclerSelectedPhotosModel recyclerSelectedPhotosModel;
    ViewpagerAdapter adapterView;
    LinearLayoutManager listlayoutManager;
    Photosadapter photosadapter;

    private final String TAG = "PhotoEditorActivity";
    private RelativeLayout parentImageRelativeLayout;
    private RecyclerView drawingViewColorPickerRecyclerView;
    private TextView undoTextView, undoTextTextView, doneDrawingTextView, eraseDrawingTextView, addPencil;
    private SlidingUpPanelLayout mLayout;
    private View topShadow;
    private RelativeLayout topShadowRelativeLayout;
    private View bottomShadow;
    private RelativeLayout bottomShadowRelativeLayout;
    private ArrayList<Integer> colorPickerColors;
    private int colorCodeTextView = -1;
    private PhotoEditorSDK photoEditorSDK;
    private EditText edtCaption;
    String doc_type = "", doc_description = "";
    String image_final_path = "";
    int vp_image_position = 0;
    ImageView photoEditImageView;
    String doc_text = "";
    LinearLayout clear;
    int optionSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photos_edit);

        context = this;
        list = getIntent().getExtras().getStringArrayList("multipleImage");
        initView();

    }

    private void initView() {
        clear = findViewById(R.id.clear);
        viewPager = findViewById(R.id.viewpager);

        for(int i = 0;i < list.size(); i++){
            selectedmodel = new SelectedPhotosModel(list.get(i),doc_type,doc_description);
            selectedPhotosModels.add(selectedmodel);

            recyclerSelectedPhotosModel = new RecyclerSelectedPhotosModel(list.get(i),doc_type,doc_description);
            recyclerSelectedPhotosModels.add(recyclerSelectedPhotosModel);
        }

        adapterView = new ViewpagerAdapter(this,selectedPhotosModels);
        viewPager.setAdapter(adapterView);

        rv_photos = findViewById(R.id.rv_photos);
        listlayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        rv_photos.setLayoutManager(listlayoutManager);
        photosadapter = new Photosadapter(SelectedPhotosEditActivity.this,recyclerSelectedPhotosModels);
        rv_photos.setAdapter(photosadapter);
        recyclerSelectedPhotosModels.get(optionSelected).setSelected(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                vp_image_position = position;

                recyclerSelectedPhotosModels.get(optionSelected).setSelected(false);
                recyclerSelectedPhotosModels.get(position).setSelected(true);
                optionSelected = position;

                photosadapter.notifyDataSetChanged();

                if(selectedPhotosModels.get(vp_image_position).getDoc_description().equals("")){
                    edtCaption.setText("");
                }else{
                    edtCaption.setText(selectedPhotosModels.get(vp_image_position).getDoc_description());
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        /*rv_photos.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {

                recyclerSelectedPhotosModels.get(optionSelected).setSelected(false);
                recyclerSelectedPhotosModels.get(position).setSelected(true);
                optionSelected = position;

                photosadapter.notifyDataSetChanged();

                viewPager.setCurrentItem(optionSelected);

            }

            @Override
            public void onItemLongPress(View childView, int position) {

            }
        }));*/

        BrushDrawingView brushDrawingView = (BrushDrawingView) findViewById(R.id.drawing_view);
        drawingViewColorPickerRecyclerView = (RecyclerView) findViewById(R.id.drawing_view_color_picker_recycler_view);
        parentImageRelativeLayout = (RelativeLayout) findViewById(R.id.parent_image_rl);
        TextView closeTextView = (TextView) findViewById(R.id.close_tv);
        TextView addTextView = (TextView) findViewById(R.id.add_text_tv);
        addPencil = (TextView) findViewById(R.id.add_pencil_tv);
        RelativeLayout deleteRelativeLayout = (RelativeLayout) findViewById(R.id.delete_rl);
        TextView deleteTextView = (TextView) findViewById(R.id.delete_tv);
        TextView addImageEmojiTextView = (TextView) findViewById(R.id.add_image_emoji_tv);
        TextView saveTextView = (TextView) findViewById(R.id.save_tv);
        TextView saveTextTextView = (TextView) findViewById(R.id.save_text_tv);
        edtCaption = findViewById(R.id.edtCaption);
        edtCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doc_text = edtCaption.getText().toString();
                selectedPhotosModels.get(vp_image_position).setDoc_description(doc_text);
                recyclerSelectedPhotosModels.get(vp_image_position).setDoc_description(doc_text);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        undoTextView = (TextView) findViewById(R.id.undo_tv);
        undoTextTextView = (TextView) findViewById(R.id.undo_text_tv);
        doneDrawingTextView = (TextView) findViewById(R.id.done_drawing_tv);
        eraseDrawingTextView = (TextView) findViewById(R.id.erase_drawing_tv);
        TextView clearAllTextView = (TextView) findViewById(R.id.clear_all_tv);
        TextView clearAllTextTextView = (TextView) findViewById(R.id.clear_all_text_tv);
        TextView goToNextTextView = (TextView) findViewById(R.id.go_to_next_screen_tv);
        photoEditImageView = (ImageView) findViewById(R.id.photo_edit_iv);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        topShadow = findViewById(R.id.top_shadow);
        topShadowRelativeLayout = (RelativeLayout) findViewById(R.id.top_parent_rl);
        bottomShadow = findViewById(R.id.bottom_shadow);
        bottomShadowRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_parent_rl);

        ViewPager pager = (ViewPager) findViewById(R.id.image_emoji_view_pager);
        PageIndicator indicator = (PageIndicator) findViewById(R.id.image_emoji_indicator);


        final List<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(new ImageFragment());

        PreviewSlidePagerAdapter adapter = new PreviewSlidePagerAdapter(getSupportFragmentManager(), fragmentsList);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(5);
        indicator.setViewPager(pager);

        photoEditorSDK = new PhotoEditorSDK.PhotoEditorSDKBuilder(SelectedPhotosEditActivity.this)
                .parentView(parentImageRelativeLayout) // add parent image view
                .childView(photoEditImageView) // add the desired image view
                .deleteView(deleteRelativeLayout) // add the deleted view that will appear during the movement of the views
                .brushDrawingView(brushDrawingView) // add the brush drawing view that is responsible for drawing on the image view
                .buildPhotoEditorSDK(); // build photo editor sdk
        photoEditorSDK.setOnPhotoEditorSDKListener(this);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mLayout.setScrollableView(((ImageFragment) fragmentsList.get(position)).imageRecyclerView);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        closeTextView.setOnClickListener(this);
        addImageEmojiTextView.setOnClickListener(this);
        addTextView.setOnClickListener(this);
        addPencil.setOnClickListener(this);
        saveTextView.setOnClickListener(this);
        saveTextTextView.setOnClickListener(this);
        undoTextView.setOnClickListener(this);
        undoTextTextView.setOnClickListener(this);
        doneDrawingTextView.setOnClickListener(this);
        eraseDrawingTextView.setOnClickListener(this);
        clearAllTextView.setOnClickListener(this);
        clearAllTextTextView.setOnClickListener(this);
        goToNextTextView.setOnClickListener(this);

        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.black));
        colorPickerColors.add(getResources().getColor(R.color.blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.brown_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.green_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.sky_blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.violet_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.white));
        colorPickerColors.add(getResources().getColor(R.color.yellow_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.yellow_green_color_picker));


        new CountDownTimer(500, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mLayout.setScrollableView(((ImageFragment) fragmentsList.get(0)).imageRecyclerView);
            }

        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_tv) {
            onBackPressed();
        } else if (v.getId() == R.id.add_image_emoji_tv) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else if (v.getId() == R.id.add_text_tv) {
            openAddTextPopupWindow("", -1);
        } else if (v.getId() == R.id.add_pencil_tv) {
            updateBrushDrawingView(true);
        } else if (v.getId() == R.id.done_drawing_tv) {
            updateBrushDrawingView(false);
        } else if (v.getId() == R.id.save_tv || v.getId() == R.id.save_text_tv) {
            returnBackWithSavedImage();
        } else if (v.getId() == R.id.clear_all_tv || v.getId() == R.id.clear_all_text_tv) {
            clearAllViews();
        } else if (v.getId() == R.id.undo_text_tv || v.getId() == R.id.undo_tv) {
            undoViews();
        } else if (v.getId() == R.id.erase_drawing_tv) {
            eraseDrawing();
        } else if (v.getId() == R.id.go_to_next_screen_tv) {
           // returnBackWithSavedImage();
            Intent intent = new Intent(context, DocumentActivity.class);
            intent.putExtra("multipleImage",selectedPhotosModels);
            startActivity(intent);
        }
    }

    private void undoViews() {
        photoEditorSDK.viewUndo();
    }

    private void eraseDrawing() {
        photoEditorSDK.brushEraser();
    }

    private void clearAllViews() {
        photoEditorSDK.clearAllViews();
    }

    private void returnBackWithSavedImage() {
        updateView(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentImageRelativeLayout.setLayoutParams(layoutParams);
        new CountDownTimer(1000, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageName = "IMG_" + timeStamp + ".jpg";
                Intent returnIntent = new Intent();
                returnIntent.putExtra("imagePath", photoEditorSDK.saveImage("PhotoEditorSDK", imageName));
                returnIntent.putExtra("isFrom", getIntent().getExtras().getString("isFrom"));
                returnIntent.putExtra("description", edtCaption.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }.start();
    }

    private void updateBrushDrawingView(boolean brushDrawingMode) {
        photoEditorSDK.setBrushDrawingMode(brushDrawingMode);
        if (brushDrawingMode) {
            updateView(View.GONE);
            viewPager.setVisibility(View.GONE);
            photoEditImageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(selectedPhotosModels.get(vp_image_position).getDoc_link())
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(photoEditImageView);
            drawingViewColorPickerRecyclerView.setVisibility(View.VISIBLE);
            doneDrawingTextView.setVisibility(View.VISIBLE);
            eraseDrawingTextView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SelectedPhotosEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
            drawingViewColorPickerRecyclerView.setLayoutManager(layoutManager);
            drawingViewColorPickerRecyclerView.setHasFixedSize(true);
            ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(SelectedPhotosEditActivity.this, colorPickerColors);
            colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
                @Override
                public void onColorPickerClickListener(int colorCode) {
                    photoEditorSDK.setBrushColor(colorCode);
                }
            });
            drawingViewColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        } else {
            updateView(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            photoEditImageView.setVisibility(View.GONE);
            drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
            doneDrawingTextView.setVisibility(View.GONE);
            eraseDrawingTextView.setVisibility(View.GONE);
            image_final_path = "";
            Toast.makeText(context, "hhhhh"+vp_image_position, Toast.LENGTH_SHORT).show();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "IMG_" + timeStamp + ".jpg";
            image_final_path = photoEditorSDK.saveImage("PhotoEditorSDK", imageName);


            selectedPhotosModels.get(vp_image_position).setDoc_link(image_final_path);
            recyclerSelectedPhotosModels.get(vp_image_position).setDoc_link(image_final_path);

            adapterView = new ViewpagerAdapter(this,selectedPhotosModels);
            viewPager.setAdapter(adapterView);

            viewPager.setCurrentItem(vp_image_position);
            photosadapter = new Photosadapter(SelectedPhotosEditActivity.this,recyclerSelectedPhotosModels);
            rv_photos.setAdapter(photosadapter);

        }
    }

    private void updateView(int visibility) {
        topShadow.setVisibility(visibility);
        topShadowRelativeLayout.setVisibility(visibility);
        bottomShadow.setVisibility(visibility);
        bottomShadowRelativeLayout.setVisibility(visibility);
    }

    @Override
    public void onEditTextChangeListener(String text, int colorCode) {
        openAddTextPopupWindow(text, colorCode);
    }

    private void openAddTextPopupWindow(String text, int colorCode) {
        colorCodeTextView = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        final EditText addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);
        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SelectedPhotosEditActivity.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(SelectedPhotosEditActivity.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                addTextEditText.setTextColor(colorCode);
                colorCodeTextView = colorCode;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.setTextColor(colorCode == -1 ? getResources().getColor(R.color.white) : colorCode);
        }
        final PopupWindow pop = new PopupWindow(SelectedPhotosEditActivity.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText(addTextEditText.getText().toString(), colorCodeTextView);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                pop.dismiss();
            }
        });
    }

    private boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    private void addText(String text, int colorCodeTextView) {
        photoEditorSDK.addText(text, colorCodeTextView);
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        if (numberOfAddedViews > 0) {
            undoTextView.setVisibility(View.VISIBLE);
            undoTextTextView.setVisibility(View.VISIBLE);
        }
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onAddViewListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onAddViewListener");
                break;
            case TEXT:
                Log.i("TEXT", "onAddViewListener");
                break;
        }
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.i(TAG, "onRemoveViewListener");
        if (numberOfAddedViews == 0) {
            undoTextView.setVisibility(View.GONE);
            undoTextTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onStartViewChangeListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onStartViewChangeListener");
                break;
            case TEXT:
                Log.i("TEXT", "onStartViewChangeListener");
                break;
        }
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onStopViewChangeListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onStopViewChangeListener");
                break;
            case TEXT:
                Log.i("TEXT", "onStopViewChangeListener");
                break;
        }
    }

    public void selectedImageClick(int position) {
        recyclerSelectedPhotosModels.get(optionSelected).setSelected(false);
        recyclerSelectedPhotosModels.get(position).setSelected(true);
        optionSelected = position;

        photosadapter.notifyDataSetChanged();

        viewPager.setCurrentItem(optionSelected);
    }

    private class ViewpagerAdapter extends PagerAdapter {

        SelectedPhotosEditActivity newPhotoEditiorActivity;
        ArrayList<SelectedPhotosModel> list;
        private LayoutInflater inflater;

        public ViewpagerAdapter(SelectedPhotosEditActivity newPhotoEditiorActivity, ArrayList<SelectedPhotosModel> list) {
            this.newPhotoEditiorActivity = newPhotoEditiorActivity;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.photo_edit_iv1);

            Glide.with(context)
                    .load(list.get(position).getDoc_link())
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .into(imageView);

            view.addView(imageLayout, 0);

            return imageLayout;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    private class PreviewSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        PreviewSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragments == null) {
                return (null);
            }
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}