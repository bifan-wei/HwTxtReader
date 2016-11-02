package com.hw.txtreader.readerstyle;

import com.hw.beans.ReaderStyle;
import com.hw.hwtxtreader.R;

import android.graphics.Color;

public class TxtReaderNiceStyle extends ReaderStyle {

	@Override
	public int getBrawable() {

		return R.drawable.reading__reading_themes_vine_yellow1;
	}

	@Override
	public Style getStyle() {

		return Style.nice;
	}

	@Override
	public int getTextColor() {
		
		return Color.parseColor("#6e5942");
	}

}
