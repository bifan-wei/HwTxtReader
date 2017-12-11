package com.hw.txtreaderlib.interfaces;

/**
 * Created by bifan-wei
 * on 2017/12/5.
 */

public interface ITextSelectListener {
    void onTextChanging(String selectText);
    void onTextSelected(String selectText);
}
