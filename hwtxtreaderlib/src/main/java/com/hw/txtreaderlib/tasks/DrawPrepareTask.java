package com.hw.txtreaderlib.tasks;

import android.graphics.Color;
import android.graphics.Paint;

import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.main.PaintContext;
import com.hw.txtreaderlib.main.TxtConfig;
import com.hw.txtreaderlib.main.TxtReaderContext;
import com.hw.txtreaderlib.utils.ELogger;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class DrawPrepareTask implements ITxtTask {
    private String tag = "DrawPrepareTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        callBack.onMessage("start do DrawPrepare");
        ELogger.log(tag, "do DrawPrepare");
        initPainContext(readerContext.getPaintContext(), readerContext.getTxtConfig());
        readerContext.getPaintContext().textPaint.setColor(Color.WHITE);
        ITxtTask txtTask = new BitmapProduceTask();
        txtTask.Run(callBack, readerContext);

    }

    private void initPainContext(PaintContext paintContext, TxtConfig txtConfig) {
        TxtConfigInitTask.initPainContext(paintContext, txtConfig);
    }
}
