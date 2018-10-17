package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.bean.TxtChar;

/**
 * created by ： bifan-wei
 */

public interface ISliderListener {
    void onShowSlider(TxtChar txtChar);
    void onShowSlider(String CurrentSelectedText);
    void onReleaseSlider();
}
