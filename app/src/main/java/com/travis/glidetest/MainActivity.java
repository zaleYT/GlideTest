package com.travis.glidetest;

import android.Manifest;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    private RecyclerView recyclerView;

    private ArrayList<String> list = new ArrayList<String>();

    private RecyclerAdapter adapter;

    private boolean isInited = false;

    public static final int INDEX_IMAGE_ID = 0;
    public static final int INDEX_IMAGE_PATH = 1;
    public static final int INDEX_IMAGE_SIZE = 2;
    public static final int INDEX_IMAGE_DISPLAY_NAME = 3;

    public static final int EXTERNAL_STORAGE_REQ_CODE = 15 ;

    static final String EXTRA_START_INDEX = "extra_start_index";
    static final String EXTRA_CURRENT_INDEX = "extra_current_index";

    String[] projImage = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA, // 路径
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME
    };

    Uri mImangeUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private Bundle mTemReenterState;


    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

            if (mTemReenterState != null){
                int startPos = mTemReenterState.getInt(EXTRA_START_INDEX);
                int currentPos = mTemReenterState.getInt(EXTRA_CURRENT_INDEX);

                if (startPos != currentPos){
                    //String newTransitionName = currentPos+"#";
                    ImageView newSharedElement = recyclerView.findViewWithTag(currentPos);
                    String newTransitionName = newSharedElement.getTransitionName();
                    if (newSharedElement != null){
                        names.clear();
                        names.add(newTransitionName);

                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                    mTemReenterState = null;
                }
            }else {
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null){
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }

                if (statusBar != null){
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setExitSharedElementCallback(mCallback);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        if (Build.VERSION.SDK_INT > 23){
            getRuntimePermission();
        }else {
            init();
        }
    }

    private void init() {
        initImagePath();

        //recyclerView.addOnScrollListener(new RecyclerScrollListener());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new RecyclerAdapter(this, list, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void initImagePath() {
        list = new ArrayList<String>();

        final Cursor cursor = getContentResolver().query(
                mImangeUri,
                projImage,
                null,
                null,
                null
        );

        if (cursor != null){
            String path;
            while (cursor.moveToNext()){
                path = cursor.getString(INDEX_IMAGE_PATH);
                list.add(path);
                //Log.d(TAG, "image, path:" + cursor.getString(INDEX_IMAGE_PATH));
            }
        }

        cursor.close();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

        mTemReenterState = new Bundle(data.getExtras());

        int startIndex = mTemReenterState.getInt(EXTRA_START_INDEX);
        int currentIndex = mTemReenterState.getInt(EXTRA_CURRENT_INDEX);

        if (startIndex != currentIndex){
            recyclerView.scrollToPosition(currentIndex);
        }

        postponeEnterTransition();

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                recyclerView.requestLayout();

                startPostponedEnterTransition();

                return true;
            }
        });
    }

    private void getRuntimePermission() {
        // 没有获得权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            // 如果APP的权限曾经被用户拒绝过，就需要在这里更用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                //Toast.makeText(this, "please give me the permission", Toast.LENGTH_LONG).show();
            }
            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }else {
            // 获得了权限
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                init();
            }else{
                finish();
            }
        }
    }
}
