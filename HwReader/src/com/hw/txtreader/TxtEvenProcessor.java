package com.hw.txtreader;

import com.hw.readermain.EvenProcessor;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

/**
 * @author 黄威 2016年10月19日下午4:58:01 主页：http://blog.csdn.net/u014614038
 */
public class TxtEvenProcessor extends EvenProcessor {
	private TxtReaderContex ReaderContex;
	public float divider_position = 0;
	private float MoveX0;
	private Boolean OnAnimation = false;

	public TxtEvenProcessor(TxtReaderContex txtReaderContex) {
		ReaderContex = txtReaderContex;
		if (ReaderContex == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public Boolean ProcessEvent(MotionEvent event) {
		if (OnAnimation) {
			return true;
		}
		float x0 = event.getX();
		// float y0 = event.getY();
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			MoveX0 = x0;
			break;

		case MotionEvent.ACTION_MOVE:
			doMove(event);
			break;

		case MotionEvent.ACTION_UP:
			doUp(event);

			break;
		default:
			break;
		}

		return true;
	}

	private void doUp(MotionEvent event) {

		if (divider_position < 0 && !ReaderContex.mBitmapManager.isLastPage()) {
			doPageNextAnimation();
		} else if (divider_position > 0 && !ReaderContex.mBitmapManager.isFirstPage()) {
			doPagePreAnimation();
		} else {
			divider_position = 0;
		}
	}

	private void doPagePreAnimation() {
		OnAnimation = true;
		final float startposition = divider_position;
		final float leftwidth = getViewWidth() - divider_position;
		ValueAnimator animator = ValueAnimator.ofInt(0, (int) leftwidth + 5);
		animator.setDuration(1000);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int f = (Integer) animation.getAnimatedValue();
				divider_position = startposition + f;
				if (divider_position < getViewWidth()) {
					postInvalidate();
				} else {
					animation.cancel();
					divider_position = divider_position - f;
					while (divider_position < getViewWidth()) {
						divider_position = divider_position + 5;
						postInvalidate();
					}
					ReaderContex.mPageCursor.Pre();
					ReaderContex.mBitmapManager.CommitDatatoBitmap();
					ReaderContex.mBitmapManager.initDraw();
					ReaderContex.mBitmapManager.doPre();
					divider_position = 0;
					ReaderContex.onPageChageListener();
					OnAnimation = false;

				}
			}

		});

		animator.start();

	}

	private void doPageNextAnimation() {
		OnAnimation = true;

		ValueAnimator animator = ValueAnimator.ofInt(-(int) divider_position, (int) getViewWidth() + 5);
		animator.setDuration(1000);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int f = (Integer) animation.getAnimatedValue();
				divider_position = -f;
				if (divider_position > -getViewWidth()) {
					postInvalidate();
				} else {
					postInvalidate();
					animation.cancel();
					ReaderContex.mPageCursor.Next();
					ReaderContex.mBitmapManager.CommitDatatoBitmap();
					ReaderContex.mBitmapManager.initDraw();
					ReaderContex.mBitmapManager.doNext();
					divider_position = 0;
					ReaderContex.onPageChageListener();
					OnAnimation = false;

				}

			}

		});

		animator.start();

	}

	private void doMove(MotionEvent event) {
		float movex = event.getX() - MoveX0;
		MoveX0 = event.getX();
		divider_position = divider_position + movex;
		// divider<0 –>翻下一页 –>后一页数据为下一页的数据，分界阴影线在右 –>如果后一页为空，
		// 则当前页为第一页，不允许滑动
		// –>滑动完成后：1.页数据右移动；2.初始化操作。
		if (divider_position < 0) {// 小于0
			if (!ReaderContex.mBitmapManager.isLastPage()) {
				ReaderContex.mBitmapManager.doNext();
				ReaderContex.getReaderView().PostInvalidate();
			}
		} else if (divider_position > 0) {
			if (!ReaderContex.mBitmapManager.isFirstPage()) {
				ReaderContex.mBitmapManager.doPre();
				ReaderContex.getReaderView().PostInvalidate();
			}
		}

	}

	private void postInvalidate() {
		ReaderContex.getReaderView().PostInvalidate();
	}

	private float getViewWidth() {

		return ReaderContex.mPaintContex.Viewwidth;
	}
}
