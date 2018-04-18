package com.bifan.txtreaderlib.main;

import android.graphics.Bitmap;

/**
 * Created by bifa-wei
 * on 2017/11/27.
 */

public class BitmapData {
    private final Bitmap[] pages = new Bitmap[3];
    private Bitmap BgBitmap;

    public void setFirstPage(Bitmap page) {
        pages[0] = page;
    }

    public void setMidPage(Bitmap page) {
        pages[1] = page;
    }

    public void setLastPage(Bitmap page) {
        pages[2] = page;
    }

    public Bitmap FirstPage() {
        return pages[0];
    }

    public Bitmap MidPage() {
        return pages[1];
    }

    public Bitmap LastPage() {
        return pages[2];
    }

    public void setBgBitmap(Bitmap bgBitmap) {
        BgBitmap = bgBitmap;
    }

    public Bitmap getBgBitmap() {
        return BgBitmap;
    }

    public Bitmap[] getPages() {
        return pages;
    }

    public void onDestroy() {
        recycle(getBgBitmap());
        recycle(FirstPage());
        recycle(MidPage());
        recycle(LastPage());
    }

    private void recycle(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
