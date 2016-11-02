package com.hw.txtreader;

import com.hw.readermain.CavansProcessor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class TxtCavansProcessor extends CavansProcessor {
	private TxtReaderContex ReaderContex;
	private Paint shadowPaint = new Paint();

	public TxtCavansProcessor(TxtReaderContex txtReaderContex) {
		ReaderContex = txtReaderContex;
		if (ReaderContex == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public void ProcessCanans(Canvas canvas) {

		Bitmap behiBitmap = getBehideBrawPageBitmap();

		if (behiBitmap != null) {

			Rect srcRect = new Rect(0, 0, behiBitmap.getWidth(), behiBitmap.getHeight());// 截取bmp1中的矩形区域
			Rect dstRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());// bmp1在目标画布中的位置

			canvas.drawBitmap(behiBitmap, srcRect, dstRect, getTextPaint());
			canvas.save();
		} else {
			Log.e("preBitmap", "preBitmap is null");
		}

		Bitmap preBitmap = getPreBrawPageBitmap();

		if (preBitmap != null) {
			int srcleft = 0;
			int srctop = 0;
			int srcright = 0;
			int srcbott = getViewHeigh();

			if (divider_position() <= 0) {
				srcleft = 0 - divider_position();
				srcright = getViewWith();
			} else {
				srcleft = 0;
				srcright = getViewWith() - divider_position();
			}

			Rect srcRect1 = new Rect(srcleft, srctop, srcright, srcbott);// 截取bmp1中的矩形区域

			int dstleft = 0;
			int dsttop = 0;
			int dstright = 0;
			int dstbott = canvas.getHeight();

			if (divider_position() <= 0) {
				dstleft = 0;
				dstright = getViewWith() + divider_position();
				dstright = dstright < 0 ? 0 : dstright;
			} else {
				dstleft = divider_position();
				dstright = getViewWith();
			}

			
			Rect dstRect1 = new Rect(dstleft, dsttop, dstright, dstbott);// bmp1在目标画布中的位置

			canvas.drawBitmap(preBitmap, srcRect1, dstRect1, getTextPaint());
			canvas.save();

			if (showshadow()) {
				int left = 0;
				int top = 0;
				int right = 0;
				int bottom = getViewHeigh();

				if (divider_position() < 0 && divider_position() > (shadowwith() - getViewWith())) {

					left = getViewWith() + divider_position();
					right = left + shadowwith();

				} else if (divider_position() > 0 && divider_position() < (getViewWith() - shadowwith())) {
					left = divider_position() - shadowwith();
					right = divider_position();
				}

				canvas.drawRect(left, top, right, bottom, getBgLightPaint(canvas.getWidth()));
				canvas.save();
			}

		} else {
			Log.e("behideBitmap", "behideBitmap is null");
		}

	}

	private Paint getBgLightPaint(int canvanwith) {

		int x0 = 0;
		int y0 = 0;
		int x1 = 0;
		int y1 = 0;

		if (divider_position() < 0) {

			x0 = canvanwith + divider_position();
			x1 = x0 + shadowwith();

		} else if (divider_position() > 0) {
			x0 = divider_position();
			x1 = divider_position() - shadowwith();
		}
		LinearGradient gradient = new LinearGradient(x1, y0, x0, y1,
				new int[] { Color.parseColor("#00666666"), Color.parseColor("#11666666"), Color.parseColor("#33666666"),
						Color.parseColor("#44666666"), Color.parseColor("#88666666"), Color.parseColor("#ee666666") },
				null, LinearGradient.TileMode.CLAMP);
		shadowPaint.setShader(gradient);
		return shadowPaint;
	}

	private int shadowwith() {

		return (int) ReaderContex.mViewSetting.DeviderWidth;
	}

	private Bitmap getBgBitmp() {

		return ReaderContex.mBitmapManager.getPagebgbitmap();
	}

	private int divider_position() {
		if (ReaderContex.txtEvenProcessor == null) {
			return 0;
		}
		return (int) ReaderContex.txtEvenProcessor.divider_position;
	}

	private boolean showshadow() {

		return ReaderContex.mViewSetting.HasDiverder;
	}

	private Paint getTextPaint() {

		return ReaderContex.mPaintContex.TextPaint;
	}

	private int getViewHeigh() {

		return (int) ReaderContex.mPaintContex.Viewheight;
	}

	private int getViewWith() {

		return (int) ReaderContex.mPaintContex.Viewwidth;
	}

	private Bitmap getPreBrawPageBitmap() {
		return ReaderContex.mBitmapManager.predrawbitmap;
	}

	private Bitmap getBehideBrawPageBitmap() {

		return ReaderContex.mBitmapManager.nexdrawbitmap;
	}
}
