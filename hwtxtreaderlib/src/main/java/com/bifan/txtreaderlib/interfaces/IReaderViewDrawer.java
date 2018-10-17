package com.bifan.txtreaderlib.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by bifan-wei
 * on 2017/12/1.
 */

public interface IReaderViewDrawer {
    void drawPageNextPageShadow(Canvas canvas);
    //绘制执行下一页的页边阴影

    void drawPageNextBottomPage(Canvas canvas);
    //绘制执行下一页时的下面页部分


    void drawPageNextTopPage(Canvas canvas);
    //绘制执行下一页时的上面页部分


    void drawPagePrePageShadow(Canvas canvas);
    //绘制执行上一页的页边阴影


    void drawPagePreBottomPage(Canvas canvas);
    //绘制执行上一页时的下面页部分


    void drawPagePreTopPage(Canvas canvas);
    //绘制执行上一页时的上面页部分


    void startPageStateBackAnimation();
     //状态恢复动画

    void startPageNextAnimation();
    //执行下一页动画

    void startPagePreAnimation();
    //执行上一页动画

    void onTextSelectMoveForward(MotionEvent event);
    // 文字向前选中

    void onTextSelectMoveBack(MotionEvent event);
    //文字向后选中

    void onPageMove(MotionEvent event);
    //页面移动

    void drawNote(Canvas canvas);
   //绘制笔记

    void drawSelectedText(Canvas canvas);
    //文字选中

    void computeScroll();
    //完成移动
}
