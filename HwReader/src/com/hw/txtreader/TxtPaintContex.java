package com.hw.txtreader;

import java.lang.reflect.Field;

import com.hw.beans.PageStyle;
import com.hw.readermain.PaintContex;
import com.hw.utils.DisPlayUtil;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author 黄威 2016年10月24日下午7:55:43 主页：http://blog.csdn.net/u014614038
 */
public class TxtPaintContex extends PaintContex {
	public TxtReaderContex readerContex;
	public Paint PageIndexPaint;
	public int PageLineNums;

	/**
	 * 确保readerContex不为null，且readerContex.mContex不为null
	 * 
	 * @param readerContex
	 */
	public void init(TxtReaderContex readerContex) {
		this.readerContex = readerContex;
		if (TextPaint == null) {
			TextPaint = new Paint();
		}
		if (PageIndexPaint == null) {
			PageIndexPaint = new Paint();
		}

		TxtReaderViewSettings viewSettings = readerContex.mViewSetting;
		TextPaint.setTextSize(viewSettings.TextSize);
		TextPaint.setFakeBoldText(viewSettings.MakeBoldText);

		if (viewSettings.TexttTypeFile != null) {
			TextPaint.setTypeface(
					Typeface.createFromAsset(readerContex.mContext.getAssets(), viewSettings.TexttTypeFile));
		}
 
		TextPaint.setColor(viewSettings.TextColor);
		PageIndexPaint.setTextSize(viewSettings.PageIndextextSize);
		PageIndexPaint.setFakeBoldText(viewSettings.MakePageIndexBoldText);

		if (viewSettings.PageIndextestTypeFile != null) {
			PageIndexPaint.setTypeface(
					Typeface.createFromAsset(readerContex.mContext.getAssets(), viewSettings.PageIndextestTypeFile));
		}

		PageIndexPaint.setColor(viewSettings.PageIndexTextColor);
		initViewWidthAndHeigh();
		countLineNums();
	}

	public void CommitSetting() {
		init(readerContex);

	}

	private void initViewWidthAndHeigh() {
		WindowManager windowManager = (WindowManager) readerContex.mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		float windowwidth = displayMetrics.widthPixels;
		float windowheigh = displayMetrics.heightPixels;
		TxtReaderViewSettings viewSettings = readerContex.mViewSetting;
		if (viewSettings.Hidestatebar) {
			Viewheight = windowheigh;
		} else {
			Viewheight = windowheigh - getBarheigh();
		}
		//Viewwidth = windowwidth - viewSettings.Paddingleft - viewSettings.Paddingright;
		Viewwidth = windowwidth;
	
	}

	/**
	 * TODO 下午3:10:32
	 * 
	 * @return 返回状态栏高度
	 */
	private int getBarheigh() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return readerContex.mContext.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return 0;
	}

	private void countLineNums() {
		TxtReaderViewSettings viewSettings = readerContex.mViewSetting;
		float textheight = viewSettings.TextSize;
		float textpxheigh = DisPlayUtil.sp2px(readerContex.mContext, textheight);

		if (readerContex.mPageStyle == PageStyle.horizontal) {
			PageLineNums = (int) ((Viewheight - viewSettings.Paddingtop - viewSettings.Paddingbottom)
					/ (textpxheigh + viewSettings.LinePadding));
		} else if (readerContex.mPageStyle == PageStyle.vertical) {
			PageLineNums = (int) ((Viewwidth - viewSettings.Paddingleft - viewSettings.Paddingright)
					/ (textpxheigh + viewSettings.LinePadding));
		}
	}

	public int getPageLineNums() {
		return PageLineNums;
	}

	public void setPageLineNums(int pageLineNums) {
		PageLineNums = pageLineNums;
	}

	public Paint getPageIndexPaint() {
		return PageIndexPaint;
	}

	public void setPageIndexPaint(Paint pageIndexPaint) {
		PageIndexPaint = pageIndexPaint;
	}

}
