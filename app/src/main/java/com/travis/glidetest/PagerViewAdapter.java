package com.travis.glidetest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by yutao on 2018/8/17.
 */

public class PagerViewAdapter extends PagerAdapter {

    public static final String TAG = "PagerViewAdapter";

    private List<String> list;
    private PagerViewActivity context;

    private ImageOnClick imageOnClick;

    private ImageView imageView;

    public PagerViewAdapter(PagerViewActivity context, List<String> list, ImageOnClick callback) {
        this.list = list;
        this.context = context;

        this.imageOnClick = callback;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //return super.instantiateItem(container, position);

        // 从log看，position为当前position的前一个或者后一个position
        // 向前滑动的话，position就是当前位置的前一个，向后滑，就当前位置的后一个

        View view = View.inflate(context, R.layout.pager_view_image_item, null);
        imageView = (ImageView) view.findViewById(R.id.vp_item_image);
        imageView.setTransitionName(position + "#");// 设置shared transitionName

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageOnClick.onClickCallBack(position);
            }
        });

        Glide.with(context).load(list.get(position)).into(imageView);

        container.addView(imageView);

        context.startPostponedEnterTransition();// 开始transition

        return view;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        imageView = (ImageView)object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);

        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public ImageView getImageView(){
        return imageView;
    }

    interface ImageOnClick{
        void onClickCallBack(int position);
    }
}
