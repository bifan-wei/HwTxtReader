package com.hw.txtreaderlib.main;

import android.graphics.Paint;

/*
* create by bifan-wei
* 2017-11-13
*/
public class PaintContext {
    public Paint textPaint;
    public Paint selectTextPaint;
    public Paint sliderPaint;
    public Paint notePaint;

    public PaintContext() {
        textPaint = new Paint();
        selectTextPaint = new Paint();
        notePaint = new Paint();
        sliderPaint = new Paint();
    }

}
