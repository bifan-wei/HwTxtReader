package com.bifan.txtreaderlib.main;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.widget.Scroller;

import com.bifan.txtreaderlib.interfaces.ITextSelectDrawer;

/**
 * Created by bifan-wei
 * on 2017/12/2.
 */

public class PageDrawerBase {
    protected int PageSwitchTime = 400;
    protected TxtReaderView readerView;
    protected TxtReaderContext readerContext;
    protected Scroller scroller;
    protected Path mPath = new Path();
    protected ITextSelectDrawer textSelectDrawer;


    public PageDrawerBase(TxtReaderView readerView, TxtReaderContext readerContext, Scroller scroller) {
        this.readerView = readerView;
        this.readerContext = readerContext;
        this.scroller = scroller;
        PageSwitchTime = TxtConfig.getPageSwitchDuration(readerContext.context);
    }
    protected int getWidth() {
        return readerView.getWidth();
    }
    protected float getMoveDistance() {
        return readerView.getMoveDistance();
    }
    protected int getHeight() {
        return readerView.getHeight();
    }
    protected Bitmap getTopPage() {
        return readerView.getTopPage();
    }
    protected Bitmap getBottomPage() {
        return readerView.getBottomPage();
    }



    public ITextSelectDrawer getTextSelectDrawer() {
        if (textSelectDrawer == null) {
            textSelectDrawer = new NormalTextSelectDrawer();
        }
        return textSelectDrawer;
    }

    public void setTextSelectDrawer(ITextSelectDrawer textSelectDrawer) {
        this.textSelectDrawer = textSelectDrawer;
    }



}
