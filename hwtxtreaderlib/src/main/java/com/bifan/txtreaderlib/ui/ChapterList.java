package com.bifan.txtreaderlib.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bifan.txtreaderlib.R;
import com.bifan.txtreaderlib.interfaces.IChapter;

import java.util.List;

/**
 * Created by bifan-wei
 * on 2017/12/12.
 */

public class ChapterList extends PopupWindow {
    private Context mContext;
    private ListView mRootView;
    private MyAdapter mAdapter;
    private List<IChapter> mChapters;
    private int CurrentIndex = -1;
    private int AllCharNum ;
    private int ViewHeight;

    public ChapterList(Context mContext, int ViewHeight, List<IChapter> mChapters, int allCharNum) {
        super(mContext);
        this.mContext = mContext;
        this.ViewHeight = ViewHeight;
        this.mChapters = mChapters;
        this.AllCharNum = allCharNum;
        initRootView();
    }

    public int getAllCharNum() {
        return AllCharNum;
    }

    public void setCurrentIndex(int currentIndex) {
        CurrentIndex = currentIndex;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public ListView getListView() {
        return mRootView;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    protected void initRootView() {
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(metrics);

        int ViewHeight = this.ViewHeight;
        int ViewWidth = metrics.widthPixels;
        mRootView = new ListView(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
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

    public void setBackGroundColor(int color){
        mRootView.setBackgroundColor(color);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mChapters == null ? 0 : mChapters.size();
        }

        @Override
        public Object getItem(int i) {
            return mChapters.get(i);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chapterlist, null);
                holder.index = view.findViewById(R.id.adapter_chatperlist_index);
                holder.title = view.findViewById(R.id.adapter_chatperlist_title);
                holder.progress = view.findViewById(R.id.adapter_chatperlist_progress);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            IChapter chapter = mChapters.get(i);
            if (CurrentIndex == i) {
                holder.progress.setTextColor(Color.parseColor("#3f4032"));
                holder.progress.setText("当前");
            } else {
                holder.progress.setTextColor(Color.parseColor("#aeaca2"));
                float p = 0;
                if (AllCharNum > 0) {
                    p = (float) chapter.getStartIndex() / (float) AllCharNum;
                    if (p > 1) {
                        p = 1;
                    }
                }
                holder.progress.setText((int) (p * 100) + "%");
            }

            holder.index.setText(i + 1 + "");
            holder.title.setText((chapter.getTitle() + "").trim());
            return view;
        }

        private class Holder {
            TextView index;
            TextView title;
            TextView progress;
        }
    }

    public void onDestroy() {
        mContext = null;
        mRootView = null;
        mAdapter = null;
        if (mChapters != null) {
            mChapters.clear();
            mChapters = null;
        }
    }

}
