package com.bifan.txtreaderlib.bean;

import android.graphics.Path;

/**
 * Created by bifan-wei
 * on 2017/12/5.
 */

public abstract class Slider {
    public Boolean ShowBellow= true;
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;
    public int SliderWidth;

    public abstract float getX(float dx);//手势判断位置x坐标
    public abstract float getY(float dy);//手势判断位置y坐标
    public abstract Path getPath(TxtChar txtChar,Path path);//获取当前滑动条Path
    @Override
    public String toString() {
        return "Slider{" +
                "ShowBellow=" + ShowBellow +
                ", Left=" + Left +
                ", Right=" + Right +
                ", Top=" + Top +
                ", Bottom=" + Bottom +
                '}';
    }
}
