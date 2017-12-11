package com.hw.txtreaderlib.bean;

import android.graphics.Color;

/*
* create by bifan-wei
* 2017-11-13
*/
public class NumChar extends TxtChar {
    public NumChar(char aChar) {
        super(aChar);
    }
    private final int DefaultTextColor = Color.parseColor("#45a1cd");
    @Override
    public int getTextColor() {
        return DefaultTextColor;
    }

    @Override
    public int getCharType() {
        return Char_Num;
    }
}
