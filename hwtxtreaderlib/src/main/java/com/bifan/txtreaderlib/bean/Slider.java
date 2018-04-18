package com.bifan.txtreaderlib.bean;

/**
 * Created by bifan-wei
 * on 2017/12/5.
 */

public class Slider {
    public Boolean ShowBellow= true;
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;

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
