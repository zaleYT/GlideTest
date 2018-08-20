package com.travis.glidetest;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by yutao on 2018/8/10.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static final String TAG = "RecyclerAdapter";

    private ArrayList<String> list;
    private Context context;
    private RecyclerView recyclerView;


    public RecyclerAdapter(Context context, ArrayList<String> list, RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ImageView imageView = holder.imageView;

        //imageView.setTag(position);
        //imageView.setImageBitmap(null);
        //imageView.setBackgroundResource(R.color.colorgray);
        imageView.setTransitionName(position + "#"); // 设置shared transitionName
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Intent intent = new Intent(context, PagerViewActivity.class);

                    intent.putExtra("start_image", position);
                    intent.putStringArrayListExtra("image_path_list", list);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) view.getContext(), view, view.getTransitionName());

                    view.getContext().startActivity(intent, options.toBundle());
                }
            }
        });

        Log.d(TAG, "positon:" +position);
        Glide.with(context)
                .load(list.get(position))
                .placeholder(R.color.colorgray)
                .into(imageView);

        imageView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_image);
        }
    }

    /*private class LoadCacheAsyncTask extends AsyncTask<String, Void, File>{

        private int index;

        public LoadCacheAsyncTask(int position) {
            index = position;
        }

        @Override
        protected File doInBackground(String... strings) {
            File file = null;
            try {
                file = Glide.with(context)
                           .load(strings[0])
                           .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                           .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null){

            }else {
                LoadImageAsyncTask task = new LoadImageAsyncTask(index);
                loadImageTasks.add(task);
                task.execute(file);
            }

            loadCacheTask.remove(this);
        }
    }

    private class LoadImageAsyncTask extends AsyncTask<File, Void, DrawableTypeRequest<File>>{

        private int index;

        public LoadImageAsyncTask(int index) {
            this.index = index;
        }

        @Override
        protected DrawableTypeRequest<File> doInBackground(File... files) {

            DrawableTypeRequest<File> type =  Glide.with(context).load(files[0]);

            return type;
        }

        @Override
        protected void onPostExecute(DrawableTypeRequest<File> bitmap) {
            ImageView imageView = recyclerView.findViewWithTag(index);
            bitmap.into(imageView);

            loadImageTasks.remove(this);
        }
    }*/
}
