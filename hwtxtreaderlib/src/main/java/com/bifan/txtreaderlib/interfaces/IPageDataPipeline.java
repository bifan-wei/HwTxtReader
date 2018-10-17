package com.bifan.txtreaderlib.interfaces;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface IPageDataPipeline {
    IPage getPageStartFromProgress(int paragraphIndex, int charIndex);
    IPage getPageEndToProgress(int paragraphIndex, int charIndex);
}
