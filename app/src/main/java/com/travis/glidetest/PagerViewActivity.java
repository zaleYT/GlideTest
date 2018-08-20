package com.travis.glidetest;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yutao on 2018/8/17.
 */

public class PagerViewActivity extends Activity {

    public static final String  TAG = "PagerViewActivity";

    ViewPager viewPager;

    private int currentIndex;
    private int startIndex;

    private boolean mIsReturning;

    private final SharedElementCallback mCallBack = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning){
                ImageView sharedElement = ((PagerViewAdapter)viewPager.getAdapter()).getImageView();
                if (sharedElement == null){
                    names.clear();
                    sharedElements.clear();
                }else if (startIndex != currentIndex){
                    names.clear();
                    names.add(sharedElement.getTransitionName());

                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.pager_view_image);
        postponeEnterTransition(); // 延迟执行transition

        setEnterSharedElementCallback(mCallBack);

        viewPager = findViewById(R.id.view_pager);

        Intent intent = getIntent();
        startIndex = intent.getIntExtra("start_image", 0);
        ArrayList<String> list =  intent.getStringArrayListExtra("image_path_list");

        viewPager.setAdapter(new PagerViewAdapter(this,list,
                new PagerViewAdapter.ImageOnClick() {
            @Override
            public void onClickCallBack(int position) {
                //ActivityCompat.finishAfterTransition(PagerViewActivity.this);
                finishAfterTransition();
            }
        }));

        currentIndex = startIndex;
        viewPager.setCurrentItem(startIndex);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position:" + position);
                currentIndex = position;
            }
        });
    }

    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_START_INDEX,startIndex);
        data.putExtra(MainActivity.EXTRA_CURRENT_INDEX, currentIndex);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }
}
