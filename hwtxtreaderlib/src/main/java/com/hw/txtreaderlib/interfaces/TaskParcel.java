package com.hw.txtreaderlib.interfaces;

import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.main.TxtReaderContext;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface TaskParcel {
    void onNextTask(TaskParcel parcel, TxtReaderContext readerContext);
    void onBack(TxtMsg msg);
}
