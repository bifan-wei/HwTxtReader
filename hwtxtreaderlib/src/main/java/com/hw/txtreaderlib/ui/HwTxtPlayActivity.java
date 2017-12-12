package com.hw.txtreaderlib.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.txtreaderlib.R;
import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.interfaces.IChapter;
import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.IPageChangeListener;
import com.hw.txtreaderlib.main.TxtConfig;
import com.hw.txtreaderlib.main.TxtReaderView;

import java.io.File;

/**
 * Created by bifan-wei
 * on 2017/12/8.
 */

public class HwTxtPlayActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hwtxtpaly);
        getIntentData();
        init();
        loadFile();
        registerListener();
    }

    public static void LoadTxtFile(Context context, String FilePath) {
        LoadTxtFile(context, FilePath, FilePath);
    }

    public static void LoadTxtFile(Context context, String FilePath, String FileName) {
        Intent intent = new Intent();
        intent.putExtra("FilePath", FilePath);
        intent.putExtra("FileName", FileName);
        intent.setClass(context, HwTxtPlayActivity.class);
        context.startActivity(intent);
    }

    private View mTopDecoration, mBottomDecoration;
    private TextView mChapterNameText;
    private TextView mChapterMenuText;
    private TextView mProgressText;
    private TextView mSettingText;
    private TxtReaderView mTxtReaderView;
    private MenuHolder mMenuHolder = new MenuHolder();
    private View mTopMenu;
    private View mBottomMenu;
    private View mCoverView;
    private ChapterList mChapterListPop;

    private void init() {
        mHandler = new Handler();
        mTopDecoration = findViewById(R.id.activity_hwtxtplay_top);
        mBottomDecoration = findViewById(R.id.activity_hwtxtplay_bottom);
        mTxtReaderView = (TxtReaderView) findViewById(R.id.activity_hwtxtplay_readerView);
        mChapterNameText = (TextView) findViewById(R.id.activity_hwtxtplay_chaptername);
        mChapterMenuText = (TextView) findViewById(R.id.activity_hwtxtplay_chapter_menutext);
        mProgressText = (TextView) findViewById(R.id.activity_hwtxtplay_progress_text);
        mSettingText = (TextView) findViewById(R.id.activity_hwtxtplay_setting_text);
        mTopMenu = findViewById(R.id.activity_hwtxtplay_menu_top);
        mBottomMenu = findViewById(R.id.activity_hwtxtplay_menu_bottom);
        mCoverView = findViewById(R.id.activity_hwtxtplay_cover);

        mMenuHolder.mTitle = (TextView) findViewById(R.id.txtreadr_menu_title);
        mMenuHolder.mPreChapter = (TextView) findViewById(R.id.txtreadr_menu_chapter_pre);
        mMenuHolder.mNextChapter = (TextView) findViewById(R.id.txtreadr_menu_chapter_next);
        mMenuHolder.mSeekBar = (SeekBar) findViewById(R.id.txtreadr_menu_seekbar);
        mMenuHolder.mTextSizeDel = (TextView) findViewById(R.id.txtreadr_menu_textsize_del);
        mMenuHolder.mTextSize = (TextView) findViewById(R.id.txtreadr_menu_textsize);
        mMenuHolder.mTextSizeAdd = (TextView) findViewById(R.id.txtreadr_menu_textsize_add);
        mMenuHolder.mBoldSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting1_bold);
        mMenuHolder.mBoldSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting1_boldpic);
        mMenuHolder.mNormalSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting1_normal);
        mMenuHolder.mNormalSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting1_normalpic);
        mMenuHolder.mCoverSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting2_cover);
        mMenuHolder.mCoverSelectedPic = (ImageView) findViewById(R.id.txtreadr_menu_textsetting2_coverpic);
        mMenuHolder.mTranslateSelectedLayout = findViewById(R.id.txtreadr_menu_textsetting2_translate);
        mMenuHolder.mTranslateSelectedPc = (ImageView) findViewById(R.id.txtreadr_menu_textsetting2_translatepic);
        mMenuHolder.mTextSize = (TextView) findViewById(R.id.txtreadr_menu_textsize);
        mMenuHolder.mStyle1 = findViewById(R.id.hwtxtreader_menu_style1);
        mMenuHolder.mStyle2 = findViewById(R.id.hwtxtreader_menu_style2);
        mMenuHolder.mStyle3 = findViewById(R.id.hwtxtreader_menu_style3);
        mMenuHolder.mStyle4 = findViewById(R.id.hwtxtreader_menu_style4);
        mMenuHolder.mStyle5 = findViewById(R.id.hwtxtreader_menu_style5);
    }

    private final int[] StyleTextColors = new int[]{
            Color.parseColor("#4a453a"),
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };

    private String FilePath = null;
    private String FileName = null;

    private void getIntentData() {
        FilePath = getIntent().getStringExtra("FilePath");
        FileName = getIntent().getStringExtra("FileName");

    }

    private void loadFile() {
        if (TextUtils.isEmpty(FilePath) || !(new File(FilePath).exists())) {
            toast("文件不存在");
        }
        mTxtReaderView.loadTxtFile(FilePath, new ILoadListener() {
            @Override
            public void onSuccess() {
                if (TextUtils.isEmpty(FileName)) {
                    FileName = mTxtReaderView.getTxtReaderContext().getFileMsg().FileName;
                }
                setBookName(FileName);
                initWhenLoadDone();
            }

            @Override
            public void onFail(TxtMsg txtMsg) {
                toast(txtMsg + "");
            }

            @Override
            public void onMessage(String message) {
            }
        });
    }

    private void initWhenLoadDone() {
        FileName = mTxtReaderView.getTxtReaderContext().getFileMsg().FileName;
        mMenuHolder.mTextSize.setText(mTxtReaderView.getTextSize() + "");
        mTopDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        mBottomDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
        //字体设置
        onTextSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().Bold);
        //翻页设置
        onPageSwitchSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().SwitchByTranslate);
        //章节设置
        if (mTxtReaderView.getChapters() != null) {
            WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            m.getDefaultDisplay().getMetrics(metrics);
            int ViewHeight = metrics.heightPixels - mTopDecoration.getHeight();
            mChapterListPop = new ChapterList(this, ViewHeight, mTxtReaderView.getChapters(), mTxtReaderView.getTxtReaderContext().getParagraphData().getCharNum());

            mChapterListPop.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IChapter chapter = (IChapter) mChapterListPop.getAdapter().getItem(i);
                    mChapterListPop.dismiss();
                    mTxtReaderView.loadFromProgress(chapter.getStartParagraphIndex(), 0);
                }
            });
        }
    }

    private void registerListener() {
        mTopMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mBottomMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mCoverView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Gone(mTopMenu, mBottomMenu, mCoverView);
                return true;
            }
        });

        mSettingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show(mTopMenu, mBottomMenu, mCoverView);
            }
        });

        mChapterMenuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChapterListPop.isShowing()) {
                    mChapterListPop.showAsDropDown(mTopDecoration);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IChapter currentChapter = mTxtReaderView.getCurrentChapter();
                            if (currentChapter != null) {
                                mChapterListPop.setCurrentIndex(currentChapter.getIndex());
                                mChapterListPop.notifyDataSetChanged();
                            }
                        }
                    }, 300);
                } else {
                    mChapterListPop.dismiss();
                }
            }
        });

        mMenuHolder.mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mTxtReaderView.loadFromProgress(mMenuHolder.mSeekBar.getProgress());
                }
                return false;
            }
        });

        mTxtReaderView.setPageChangeListener(new IPageChangeListener() {
            @Override
            public void onCurrentPage(float progress) {
                int p = (int) (progress * 1000);
                mProgressText.setText(((float) p / 10) + "%");
                mMenuHolder.mSeekBar.setProgress((int) (progress * 100));
                IChapter currentChapter = mTxtReaderView.getCurrentChapter();
                if (currentChapter != null) {
                    mChapterNameText.setText((currentChapter.getTitle() + "").trim());
                } else {
                    mChapterNameText.setText("无章节");
                }
            }
        });


        mTopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChapterListPop.isShowing()) {
                    mChapterListPop.dismiss();
                }
            }
        });


        mMenuHolder.mPreChapter.setOnClickListener(new ChapterChangeClickListener(true));
        mMenuHolder.mNextChapter.setOnClickListener(new ChapterChangeClickListener(false));
        mMenuHolder.mTextSizeAdd.setOnClickListener(new TextChangeClickListener(true));
        mMenuHolder.mTextSizeDel.setOnClickListener(new TextChangeClickListener(false));
        mMenuHolder.mStyle1.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor1), StyleTextColors[0]));
        mMenuHolder.mStyle2.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor2), StyleTextColors[1]));
        mMenuHolder.mStyle3.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor3), StyleTextColors[2]));
        mMenuHolder.mStyle4.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor4), StyleTextColors[3]));
        mMenuHolder.mStyle5.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor5), StyleTextColors[4]));
        mMenuHolder.mBoldSelectedLayout.setOnClickListener(new TextSettingClickListener(true));
        mMenuHolder.mNormalSelectedLayout.setOnClickListener(new TextSettingClickListener(false));
        mMenuHolder.mTranslateSelectedLayout.setOnClickListener(new SwitchSettingClickListener(true));
        mMenuHolder.mCoverSelectedLayout.setOnClickListener(new SwitchSettingClickListener(false));

    }

    private void onTextSettingUi(Boolean isBold) {
        if (isBold) {
            mMenuHolder.mBoldSelectedPic.setBackgroundResource(R.drawable.ic_selected);
            mMenuHolder.mNormalSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
        } else {
            mMenuHolder.mBoldSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
            mMenuHolder.mNormalSelectedPic.setBackgroundResource(R.drawable.ic_selected);
        }
    }

    private void onPageSwitchSettingUi(Boolean isTranslate) {
        if (isTranslate) {
            mMenuHolder.mTranslateSelectedPc.setBackgroundResource(R.drawable.ic_selected);
            mMenuHolder.mCoverSelectedPic.setBackgroundResource(R.drawable.ic_unselected);
        } else {
            mMenuHolder.mTranslateSelectedPc.setBackgroundResource(R.drawable.ic_unselected);
            mMenuHolder.mCoverSelectedPic.setBackgroundResource(R.drawable.ic_selected);
        }
    }

    private class TextSettingClickListener implements View.OnClickListener {
        private Boolean Bold = false;

        public TextSettingClickListener(Boolean bold) {
            Bold = bold;
        }

        @Override
        public void onClick(View view) {
            mTxtReaderView.setTextBold(Bold);
            onTextSettingUi(Bold);
        }
    }

    private class SwitchSettingClickListener implements View.OnClickListener {
        private Boolean isSwitchTranslate = false;

        public SwitchSettingClickListener(Boolean pre) {
            isSwitchTranslate = pre;
        }

        @Override
        public void onClick(View view) {
            if (!isSwitchTranslate) {
                mTxtReaderView.setPageSwitchByCover();
            } else {
                mTxtReaderView.setPageSwitchByTranslate();
            }
            onPageSwitchSettingUi(isSwitchTranslate);
        }
    }


    private class ChapterChangeClickListener implements View.OnClickListener {
        private Boolean Pre = false;

        public ChapterChangeClickListener(Boolean pre) {
            Pre = pre;
        }

        @Override
        public void onClick(View view) {
            if (Pre) {
                mTxtReaderView.jumpToPreChapter();
            } else {
                mTxtReaderView.jumpToNextChapter();
            }
        }
    }

    private class TextChangeClickListener implements View.OnClickListener {
        private Boolean Add = false;

        public TextChangeClickListener(Boolean pre) {
            Add = pre;
        }

        @Override
        public void onClick(View view) {
            int textSize = mTxtReaderView.getTextSize();
            if (Add) {
                if (textSize + 2 <= TxtConfig.MAX_TEXT_SIZE) {
                    mTxtReaderView.setTextSize(textSize + 2);
                    mMenuHolder.mTextSize.setText(textSize + 2 + "");
                }
            } else {
                if (textSize - 2 >= TxtConfig.MIN_TEXT_SIZE) {
                    mTxtReaderView.setTextSize(textSize - 2);
                    mMenuHolder.mTextSize.setText(textSize - 2 + "");
                }
            }

        }
    }

    private class StyleChangeClickListener implements View.OnClickListener {
        private int BgColor;
        private int TextColor;

        public StyleChangeClickListener(int bgColor, int textColor) {
            BgColor = bgColor;
            TextColor = textColor;
        }

        @Override
        public void onClick(View view) {
            mTxtReaderView.setStyle(BgColor, TextColor);
            mTopDecoration.setBackgroundColor(BgColor);
            mBottomDecoration.setBackgroundColor(BgColor);
        }
    }

    private void setBookName(String name) {
        mMenuHolder.mTitle.setText(name + "");
    }

    private void Show(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    private void Gone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }


    private Toast t;

    private void toast(final String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(HwTxtPlayActivity.this, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    private class MenuHolder {
        TextView mTitle;
        TextView mPreChapter;
        TextView mNextChapter;
        SeekBar mSeekBar;
        TextView mTextSizeDel;
        TextView mTextSizeAdd;
        TextView mTextSize;
        View mBoldSelectedLayout;
        ImageView mBoldSelectedPic;
        View mNormalSelectedLayout;
        ImageView mNormalSelectedPic;
        View mCoverSelectedLayout;
        ImageView mCoverSelectedPic;
        View mTranslateSelectedLayout;
        ImageView mTranslateSelectedPc;
        View mStyle1;
        View mStyle2;
        View mStyle3;
        View mStyle4;
        View mStyle5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTxtReaderView.getTxtReaderContext().Clear();
    }

    public void BackClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
