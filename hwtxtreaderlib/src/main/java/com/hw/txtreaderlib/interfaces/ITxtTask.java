package com.hw.txtreaderlib.interfaces;

import com.hw.txtreaderlib.main.TxtReaderContext;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface ITxtTask {
    void Run(ILoadListener callBack, TxtReaderContext readerContext);
}
