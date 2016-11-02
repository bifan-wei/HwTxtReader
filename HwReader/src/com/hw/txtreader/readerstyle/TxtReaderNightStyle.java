package com.hw.txtreader.readerstyle;

import com.hw.beans.ReaderStyle;
import com.hw.hwtxtreader.R;

import android.graphics.Color;

public class TxtReaderNightStyle extends ReaderStyle {

	@Override
	public int getBrawable() {
		return R.drawable.reading__reading_themes_vine_dark;
	}

	@Override
	public Style getStyle() {

		return Style.night;
	}

	@Override
	public int getTextColor() {
		
		return Color.parseColor("#ffffff");
	}

}
