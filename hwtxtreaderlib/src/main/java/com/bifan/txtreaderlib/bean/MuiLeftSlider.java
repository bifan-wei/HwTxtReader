package com.bifan.txtreaderlib.bean;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * created by ： bifan-wei
 */

public class MuiLeftSlider extends Slider {
    @Override
    public float getX(float dx) {
        return Right + dx - 5;
    }

    @Override
    public float getY(float dy) {
        return Top + dy - 5;
    }

    @Override
    public Path getPath(TxtChar txtChar, Path path) {
        if (txtChar != null) {

            int r = SliderWidth;
            int leftWidth = (int) (Math.cos(30) * r);
            int heightWidth = r * 3 / 2;
            Path p = path;
            p.reset();
            p.moveTo(txtChar.Left, txtChar.Top);
            p.lineTo(txtChar.Left - leftWidth, txtChar.Top - heightWidth);

            Rect rect = new Rect(
                    txtChar.Left - r,
                    txtChar.Top - 3 * r,
                    txtChar.Left + r,
                    txtChar.Top - r);

            p.addArc(new RectF(rect), 150, 240);
            p.lineTo(txtChar.Left, txtChar.Top);
            return p;
        } else {
            return null;
        }
    }
}