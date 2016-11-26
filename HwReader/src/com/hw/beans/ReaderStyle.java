package com.hw.beans;

import com.hw.hwtxtreader.R;

import android.graphics.Color;

/**
 * @author 黄威 2016年11月2日下午1:44:25 主页：http://blog.csdn.net/u014614038
 */
public class ReaderStyle {
	protected Style mStyle = Style.normal;

	public enum Style {
		normal, sorft, night, nice, ancientry2
	}

	public int getBrawable() {
		return getDrawablebyStylecode(mStyle);
	}

	public Style getStyle() {

		return mStyle;
	}

	public int getTextColor() {
		return getTextColorbyStylecode(mStyle);
	}

	public static int getDrawablebyStylecode(Style syStyle) {
		switch (syStyle) {
		case normal:
			return R.drawable.reading__reading_themes_vine_paper;
		case sorft:
			return R.drawable.reading__reading_themes_vine_defautl_white;
		case night:
			return R.drawable.reading__reading_themes_vine_dark;
		case ancientry2:
			return R.drawable.reading__reading_themes_vine_yellow1;
		case nice:
			return R.drawable.reading__reading_themes_vine_yellow1;
		default:
			return R.drawable.reading__reading_themes_vine_paper;
		}
	}

	public static int getTextColorbyStylecode(Style syStyle) {
		switch (syStyle) {
		case normal:
			return Color.BLACK;
		case sorft:
			return Color.parseColor("#0f140d");
		case night:
			return Color.parseColor("#ffffff");
		case ancientry2:
			return Color.parseColor("#090f05");
		case nice:
			return Color.parseColor("#6e5942");
		default:
			return Color.BLACK;
		}
	}
}
