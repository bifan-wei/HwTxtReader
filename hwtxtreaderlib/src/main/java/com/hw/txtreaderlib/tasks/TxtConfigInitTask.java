package com.hw.txtreaderlib.tasks;

import android.graphics.Paint;

import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.main.PageParam;
import com.hw.txtreaderlib.main.PaintContext;
import com.hw.txtreaderlib.main.TxtConfig;
import com.hw.txtreaderlib.main.TxtReaderContext;
import com.hw.txtreaderlib.utils.ELogger;
import com.hw.txtreaderlib.utils.TxtBitmapUtil;

/**
 * Created by HP on 2017/11/26.
 */

public class TxtConfigInitTask implements ITxtTask {
    private String atg = "TxtConfigInitTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        ELogger.log(atg, "do TxtConfigInit");
        TxtConfig config = readerContext.getTxtConfig();
        config.showNote = TxtConfig.getIsShowNote(readerContext.context);
        config.canPressSelect = TxtConfig.getCanPressSelect(readerContext.context);
        config.textColor = TxtConfig.getTextColor(readerContext.context);
        config.textSize = TxtConfig.getTextSize(readerContext.context);
        config.backgroundColor = TxtConfig.getBackgroundColor(readerContext.context);
        config.NoteColor = TxtConfig.getNoteTextColor(readerContext.context);
        config.SelectTextColor = TxtConfig.getSelectTextColor(readerContext.context);
        config.SliderColor = TxtConfig.getSliderColor(readerContext.context);
        //if not null ,do recycle
        if (readerContext.getBitmapData().getBgBitmap() != null) {
            readerContext.getBitmapData().getBgBitmap().recycle();
        }
        int width = readerContext.getPageParam().PageWidth;
        int height = readerContext.getPageParam().PageHeight;
        //init the bg bitmap
        readerContext.getBitmapData().setBgBitmap(TxtBitmapUtil.CreateBitmap(config.backgroundColor, width, height));
        //initPageParam
        initPageParam(readerContext);
        //start draw prepare

        int startParagraphIndex = readerContext.getFileMsg().PreParagraphIndex;
        int startCharIndex = readerContext.getFileMsg().PreCharIndex;

        //初始化一下之前的设置
        initPainContext(readerContext.getPaintContext(), readerContext.getTxtConfig());

        ITxtTask txtTask = new TxtPageLoadTask(startParagraphIndex, startCharIndex);
        txtTask.Run(callBack, readerContext);
    }

    private void initPainContext(PaintContext paintContext, TxtConfig txtConfig) {
        paintContext.textPaint.setTextSize(txtConfig.textSize);
        paintContext.textPaint.setFakeBoldText(txtConfig.Bold);
        paintContext.textPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.textPaint.setColor(txtConfig.textColor);
        paintContext.notePaint.setTextSize(txtConfig.textSize);
        paintContext.notePaint.setColor(txtConfig.NoteColor);
        paintContext.notePaint.setTextAlign(Paint.Align.LEFT);
        paintContext.selectTextPaint.setTextSize(txtConfig.textSize);
        paintContext.selectTextPaint.setColor(txtConfig.SelectTextColor);
        paintContext.selectTextPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.sliderPaint.setColor(txtConfig.SliderColor);
        paintContext.sliderPaint.setAntiAlias(true);
    }

    private void initPageParam(TxtReaderContext readerContext) {
        PageParam param = readerContext.getPageParam();
        param.LineWidth = param.PageWidth - param.PaddingLeft - param.PaddingRight;
        int lineHeight = readerContext.getTxtConfig().textSize + param.LinePadding;
        param.PageLineNum = (param.PageHeight - param.PaddingTop - param.PaddingBottom+param.LinePadding)/lineHeight;

    }


}
