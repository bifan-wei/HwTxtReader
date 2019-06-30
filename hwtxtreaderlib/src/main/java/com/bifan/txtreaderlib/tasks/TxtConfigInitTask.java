package com.bifan.txtreaderlib.tasks;

import android.graphics.Paint;

import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.ITxtTask;
import com.bifan.txtreaderlib.main.PageParam;
import com.bifan.txtreaderlib.main.PaintContext;
import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.main.TxtReaderContext;
import com.bifan.txtreaderlib.utils.ELogger;
import com.bifan.txtreaderlib.utils.TxtBitmapUtil;

/**
 * Created by HP on 2017/11/26.
 */

public class TxtConfigInitTask implements ITxtTask {
    private String atg = "TxtConfigInitTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        ELogger.log(atg, "do TxtConfigInit");
        callBack.onMessage("start init settings in TxtConfigInitTask");

        TxtConfig config = readerContext.getTxtConfig();
        initTxtConfig(readerContext, config);

        //if not null ,do recycle
        if (readerContext.getBitmapData().getBgBitmap() != null) {
            readerContext.getBitmapData().getBgBitmap().recycle();
        }

        int width = readerContext.getPageParam().PageWidth;
        int height = readerContext.getPageParam().PageHeight;

        //init the bg bitmap
        readerContext.getBitmapData().setBgBitmap(TxtBitmapUtil.createBitmap(config.backgroundColor, width, height));
        //initPageParam
        initPageParam(readerContext);
        //start draw prepare

        //get preRead Progress
        int startParagraphIndex = 0;
        int startCharIndex = 0;

        if (readerContext.getFileMsg() != null) {
            startParagraphIndex = readerContext.getFileMsg().PreParagraphIndex;
            startCharIndex = readerContext.getFileMsg().PreCharIndex;
        }
        //init  Context
        initPainContext(readerContext.getPaintContext(), readerContext.getTxtConfig());

        ITxtTask txtTask = new TxtPageLoadTask(startParagraphIndex, startCharIndex);
        txtTask.Run(callBack, readerContext);
    }

    /**
     * get pre or default config
     *
     * @param readerContext
     * @param config
     */
    private void initTxtConfig(TxtReaderContext readerContext, TxtConfig config) {
        config.showNote = TxtConfig.getIsShowNote(readerContext.context);
        config.canPressSelect = TxtConfig.getCanPressSelect(readerContext.context);
        config.textColor = TxtConfig.getTextColor(readerContext.context);
        config.textSize = TxtConfig.getTextSize(readerContext.context);
        config.backgroundColor = TxtConfig.getBackgroundColor(readerContext.context);
        config.NoteColor = TxtConfig.getNoteTextColor(readerContext.context);
        config.SelectTextColor = TxtConfig.getSelectTextColor(readerContext.context);
        config.SliderColor = TxtConfig.getSliderColor(readerContext.context);
        config.Bold = TxtConfig.isBold(readerContext.context);
        config.Page_Switch_Mode = TxtConfig.getPageSwitchMode(readerContext.context);
        config.ShowSpecialChar = TxtConfig.IsShowSpecialChar(readerContext.context);
        config.VerticalPageMode = TxtConfig.IsOnVerticalPageMode(readerContext.context);
        config.PageSwitchDuration = TxtConfig.getPageSwitchDuration(readerContext.context);
    }

    /**
     * @param paintContext
     * @param txtConfig
     */
    public static void initPainContext(PaintContext paintContext, TxtConfig txtConfig) {
        paintContext.textPaint.setTextSize(txtConfig.textSize);
        paintContext.textPaint.setFakeBoldText(txtConfig.Bold);
        paintContext.textPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.textPaint.setColor(txtConfig.textColor);
        paintContext.textPaint.setAntiAlias(true);
        paintContext.notePaint.setTextSize(txtConfig.textSize);
        paintContext.notePaint.setColor(txtConfig.NoteColor);
        paintContext.notePaint.setTextAlign(Paint.Align.LEFT);
        paintContext.notePaint.setAntiAlias(true);
        paintContext.selectTextPaint.setTextSize(txtConfig.textSize);
        paintContext.selectTextPaint.setColor(txtConfig.SelectTextColor);
        paintContext.selectTextPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.selectTextPaint.setAntiAlias(true);
        paintContext.sliderPaint.setColor(txtConfig.SliderColor);
        paintContext.sliderPaint.setAntiAlias(true);
        paintContext.textPaint.setFakeBoldText(txtConfig.Bold);
    }

    private void initPageParam(TxtReaderContext readerContext) {
        PageParam param = readerContext.getPageParam();
        int lineHeight = readerContext.getTxtConfig().textSize + param.LinePadding;
        param.LineHeight = lineHeight;
        if (!readerContext.getTxtConfig().VerticalPageMode) {
            param.LineWidth = param.PageWidth - param.PaddingLeft - param.PaddingRight;
            param.LineHeight = lineHeight;
            param.PageLineNum = (param.PageHeight - param.PaddingTop - param.PaddingBottom - readerContext.getTxtConfig().textSize - 2) / lineHeight + 1;
        } else {
            param.LineWidth = lineHeight;
            param.LineHeight = param.PageHeight - param.PaddingTop - param.PaddingBottom;
            param.PageLineNum = (param.PageWidth - param.PaddingLeft - param.PaddingRight - readerContext.getTxtConfig().textSize - 2) / lineHeight + 1;
        }
        param.PaddingLeft = TxtConfig.Page_PaddingLeft;
        param.LinePadding = TxtConfig.Page_LinePadding;
        param.PaddingRight = TxtConfig.Page_PaddingRight;
        param.PaddingTop = TxtConfig.Page_PaddingTop;
        param.PaddingBottom = TxtConfig.Page_PaddingBottom;
        param.ParagraphMargin = TxtConfig.Page_Paragraph_margin;
    }


}
