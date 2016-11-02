package com.hw.txtreader.readerstyle;

import com.hw.beans.ReaderStyle;
import com.hw.hwtxtreader.R;

import android.graphics.Color;

public class TxtReaderAncientry2Style extends ReaderStyle {

	@Override
	public int getBrawable() { 

		return R.drawable.reading__reading_themes_vine_yellow1;
	}

	@Override
	public Style getStyle() {

		return Style.ancientry2;
	}

	@Override
	public int getTextColor() {
		
		return Color.parseColor("#090f05");
	}

}
