package com.hw.txtreaderlib.ui;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hw.txtreaderlib.R;
import com.hw.txtreaderlib.interfaces.IChapter;

import java.util.List;

/**
 * Created by bifan-wei
 * on 2017/12/12.
 */

public class ChapterList extends PopupWindow {
    private Context context;
    private ListView mRootView;
    private MyAdapter mAdapter;
    private List<IChapter> chapters;
    private int CurrentIndex = -1;
    private int AllCharNum = 0;
    private int height;

    public ChapterList(Context context, int height, List<IChapter> chapters, int
            allCharNum) {
        super(context);
        this.context = context;
        this.height = height;
        this.chapters = chapters;
        this.AllCharNum = allCharNum;
        initRootView();
    }


    public void setCurrentIndex(int currentIndex) {
        CurrentIndex = currentIndex;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public ListView getListView(){
        return mRootView;
    }

    public BaseAdapter getAdapter(){
        return mAdapter;
    }
    protected void initRootView() {
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(metrics);

        int ViewHeight = height;
        int ViewWidth = metrics.widthPixels;
        mRootView = new ListView(context);
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        mRootView.setLayoutParams(params);
        this.setContentView(mRootView);
        this.setWidth(ViewWidth);
        this.setHeight(ViewHeight);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.HwTxtChapterMenuAnimation);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f1f6b8")));
        mAdapter = new MyAdapter();
        mRootView.setAdapter(mAdapter);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return chapters == null ? 0 : chapters.size();
        }

        @Override
        public Object getItem(int i) {
            return chapters.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder;
            if (view == null) {
                holder = new Holder();
                view = LayoutInflater.from(context).inflate(R.layout.adapter_chapterlist, null);
                holder.index = view.findViewById(R.id.adapter_chatperlist_index);
                holder.title = view.findViewById(R.id.adapter_chatperlist_title);
                holder.progress = view.findViewById(R.id.adapter_chatperlist_progress);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            IChapter chapter = chapters.get(i);
            if (CurrentIndex == i) {
                holder.progress.setTextColor(Color.parseColor("#3f4032"));
                holder.progress.setText("当前");
            } else {
                holder.progress.setTextColor(Color.parseColor("#aeaca2"));
                float p = 0;
                if (AllCharNum > 0) {
                    p = (float) chapter.getStartIndex()/ (float) AllCharNum;
                    if (p > 1) {
                        p = 1;
                    }
                }
                holder.progress.setText((int) (p * 100) + "%");
            }

            holder.index.setText(i+ 1 + "");
            holder.title.setText((chapter.getTitle() + "").trim());
            return view;
        }

        private class Holder {
            TextView index;
            TextView title;
            TextView progress;
        }
    }

}
