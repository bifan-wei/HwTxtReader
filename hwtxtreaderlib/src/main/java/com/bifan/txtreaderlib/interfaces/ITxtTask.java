package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.main.TxtReaderContext;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface ITxtTask {
    void Run(ILoadListener callBack, TxtReaderContext readerContext);
}
