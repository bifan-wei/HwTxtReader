package com.hw.readermain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 阅读器上下文界面类
 * 
 * @author 黄威 2016年10月19日下午3:49:25 主页：http://blog.csdn.net/u014614038
 */
public class ReaderView extends View {
	// 这个不允许空
	private IReaderContex readerContex;

	public ReaderView(Context context, IReaderContex readerContex) {
		super(context);
		setReaderContex(readerContex);
	}

	public ReaderView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	@SuppressLint("NewApi")
	public ReaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (readerContex != null && readerContex.getCavansProcessor() != null) {
			readerContex.getCavansProcessor().ProcessCanans(canvas);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (readerContex != null && readerContex.getEvenProcessor() != null) {
			return readerContex.getEvenProcessor().ProcessEvent(event);
		}

		return super.onTouchEvent(event);
	}

	public void setReaderContex(IReaderContex readerContex) {
		this.readerContex = readerContex;
	}
	
	public void PostInvalidate(){
		postInvalidate();
	}
	
	
}
