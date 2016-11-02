package com.hw.txtreader.readerstyle;

import com.hw.beans.ReaderStyle;
import com.hw.hwtxtreader.R;

import android.graphics.Color;

/**
 * @author 黄威
 * 2016年11月2日下午1:56:22
 * 主页：http://blog.csdn.net/u014614038
 */
public class TxtReaderNormalStyle extends ReaderStyle {

	@Override
	public int getBrawable() {
		return R.drawable.reading__reading_themes_vine_paper;
	}

	@Override
	public Style getStyle() {
		return Style.normal;
	}

	@Override
	public int getTextColor() {
		
		return Color.parseColor("#0f140d");
	}

}
