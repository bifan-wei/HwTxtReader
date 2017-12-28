package com.hw.txtreaderlib.tasks;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.IPage;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.main.TxtReaderContext;
import com.hw.txtreaderlib.utils.ELogger;
import com.hw.txtreaderlib.utils.TxtBitmapUtil;

import java.util.logging.Logger;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class BitmapProduceTask implements ITxtTask {
    private String tag = "BitmapProduceTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        ELogger.log(tag, "Produce Bitmap");
        callBack.onMessage("start to  Produce Bitmap");

        int[] rs = readerContext.getPageData().refreshTag;
        IPage[] pages = readerContext.getPageData().getPages();
        Bitmap[] bitmaps = readerContext.getBitmapData().getPages();
        int index = 0;

        for (int neeRefresh : rs) {
            IPage page = pages[index];
            if (neeRefresh == 1) {
                ELogger.log(tag, "page " + index + " neeRefresh");
                Bitmap bitmap = TxtBitmapUtil.createHorizontalPage(readerContext.getBitmapData().getBgBitmap(),
                        readerContext.getPaintContext(), readerContext.getPageParam(), readerContext.getTxtConfig(), page);
                bitmaps[index] = bitmap;
            } else {
                ELogger.log(tag, "page " + index + " not neeRefresh");
                //no neeRefresh ,do not change
            }
            index++;
        }
        ELogger.log(tag, "already done ,call back success");
        callBack.onMessage("already done ,call back success");
        readerContext.setInitDone(true);
        //already done ,call back success
        callBack.onSuccess();
    }
}
