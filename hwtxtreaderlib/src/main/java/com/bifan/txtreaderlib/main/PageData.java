package com.bifan.txtreaderlib.main;

import com.bifan.txtreaderlib.interfaces.IPage;

/**
 * Created by bifa-wei
 * on 2017/11/27.
 */

public class PageData {
    private final IPage[] pages = new IPage[3];
    public final int[] refreshTag = new int[]{1, 1, 1};//1:需要刷新，0不需要刷新

    public void setFirstPage(IPage page) {
        pages[0] = page;
    }

    public void setMidPage(IPage page) {
        pages[1] = page;
    }

    public void setLastPage(IPage page) {
        pages[2] = page;
    }

    public IPage FirstPage() {
        return pages[0];
    }

    public IPage MidPage() {
        return pages[1];
    }

    public IPage LastPage() {
        return pages[2];
    }

    public IPage[] getPages() {
        return pages;
    }

    public void onDestroy() {
        pages[0]=null;
        pages[1]=null;
        pages[2]=null;
    }


}
