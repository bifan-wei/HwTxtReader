package com.bifan.txtreaderlib.bean;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * created by ： bifan-wei
 */

public class DefaultRightSlider extends Slider{
    @Override
    public float getX(float dx) {
        return Left + dx - 5;
    }

    @Override
    public float getY(float dy) {
        return Top + dy - 5;
    }

    @Override
    public Path getPath(TxtChar txtChar,Path path) {
        if (txtChar != null) {
            Path p = path;
            p.reset();
            p.moveTo(txtChar.Right, txtChar.Bottom + SliderWidth);
            p.lineTo(txtChar.Right, txtChar.Bottom);
            p.lineTo(txtChar.Right + SliderWidth, txtChar.Bottom);
            Rect rect = new Rect(txtChar.Right, txtChar.Bottom, txtChar.Right + SliderWidth * 2, txtChar.Bottom + SliderWidth * 2);
            p.addArc(new RectF(rect), -90, 270);
            return p;
        } else {
            return null;
        }
    }
}
