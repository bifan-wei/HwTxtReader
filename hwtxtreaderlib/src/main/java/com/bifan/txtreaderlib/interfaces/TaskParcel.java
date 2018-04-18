package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.main.TxtReaderContext;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface TaskParcel {
    void onNextTask(TaskParcel parcel, TxtReaderContext readerContext);
    void onBack(TxtMsg msg);
}
