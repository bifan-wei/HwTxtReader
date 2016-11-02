package com.hw.menus;

import com.hw.txtreader.TxtReaderContex;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * @author 黄威 2016年11月1日下午12:02:10 主页：http://blog.csdn.net/u014614038
 */
public class ReaderLayerEvenProcessor {
	private TxtReaderContex readerContex;
	private int mWindow_With;
	private int mWindow_Heigh;
	private CenterAreaTouchListsner areaTouchListsner;
	private Boolean CenterTouched = false;

	/**
	 * @param readerContex
	 *            不能为null，readerContex.mContext.也不能为null
	 */
	public ReaderLayerEvenProcessor(TxtReaderContex readerContex, CenterAreaTouchListsner areaTouchListsner) {
		this.readerContex = readerContex;
		this.areaTouchListsner = areaTouchListsner;
		init();
	}

	private void init() {
		WindowManager m = (WindowManager) readerContex.mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;
	}

	public Boolean Process(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!CenterTouched) {				
				if (InYArea(y) && InXArea(x)) {
					CenterTouched = true;
					areaTouchListsner.onCenterTouch();
					return true;

				}
				return false;
			} else {
				if (CenterTouched) {
					areaTouchListsner.onOutSideTouch();
					CenterTouched = false;
					return true;
				}

				return false;
			}
		}

		return false;
	}

	private boolean InYArea(float yposition) {
		return yposition > getBorderTop() && yposition < getBorderBottom();
	}

	private boolean InXArea(float xposition) {
		return xposition > getBordeleft() && xposition < getBorderRight();
	}

	private int getBorderBottom() {

		return getViewHeigh() * 3 / 5;
	}

	private int getBorderTop() {

		return getViewHeigh() * 2 / 5;
	}

	private float getBorderRight() {

		return getViewWith() * 3 / 5 + 5;
	}

	private float getBordeleft() {

		return getViewWith() * 2 / 5 - 5;
	}

	private int getViewWith() {

		return mWindow_With;
	}

	private int getViewHeigh() {
		return mWindow_Heigh;
	}

}
