package com.hw.utils;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * HwReader阅读器是由黄威开发 创建时间：2016年10月29日下午7:40:25
 * 主页：http://blog.csdn.net/u014614038/
 */
public class CharUtil {

	public static float getRealShowCharHeight(char c, Paint paint) {
		float h = getCharHeighth(c, paint);
		return h = h < 20 ? 20 : h;
	}

	public static float getCharWidth(char c, Paint paint) {
		Rect r = getCharRect(c, paint);
		return r.right - r.left;
	}

	public static float getCharHeighth(char c, Paint paint) {
		Rect r = getCharRect(c, paint);
		return r.bottom - r.top;
	}

	private static Rect getCharRect(char c, Paint paint) {
		Rect r = new Rect();
		char[] txt = new char[] { c };
		paint.getTextBounds(txt, 0, 1, r);
		return r;
	}

}
