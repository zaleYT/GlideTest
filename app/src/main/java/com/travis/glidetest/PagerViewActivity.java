package com.travis.glidetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by yutao on 2018/8/17.
 */

public class PagerViewActivity extends Activity {

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.pager_view_image);
        postponeEnterTransition(); // 延迟执行transition

        viewPager = findViewById(R.id.view_pager);

        Intent intent = getIntent();
        int currentImg = intent.getIntExtra("current_image", 0);
        ArrayList<String> list =  intent.getStringArrayListExtra("image_path_list");

        viewPager.setAdapter(new PagerViewAdapter(this,list,
                new PagerViewAdapter.ImageOnClick() {
            @Override
            public void onClickCallBack(int position) {
                ActivityCompat.finishAfterTransition(PagerViewActivity.this);
            }
        }));

        viewPager.setCurrentItem(currentImg);
    }
}
