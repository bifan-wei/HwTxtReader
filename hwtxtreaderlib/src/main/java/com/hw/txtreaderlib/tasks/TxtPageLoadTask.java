package com.hw.txtreaderlib.tasks;

import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.IPage;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.main.TxtReaderContext;
import com.hw.txtreaderlib.utils.ELogger;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class TxtPageLoadTask implements ITxtTask {
    private String tag = "TxtPageLoadTask";
    private int startParagraphIndex;
    private int startCharIndex;

    public TxtPageLoadTask(int startParagraphIndex, int startCharIndex) {
        this.startParagraphIndex = startParagraphIndex;
        this.startCharIndex = startCharIndex;
    }

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {

        IPage firstPage = null;
        IPage midPage = null;
        IPage nextPage = null;

        midPage = readerContext.getPageDataPipeline().getPageStartFromProgress(startParagraphIndex, startCharIndex);
        int lineNum = readerContext.getPageParam().PageLineNum;

        if (midPage != null && midPage.HasData()) {
            firstPage = readerContext.getPageDataPipeline().getPageEndToProgress(midPage.getFirstChar().ParagraphIndex, midPage.getFirstChar().CharIndex - 1);
        }

        if (midPage != null && midPage.getLineNum() == lineNum) {
            nextPage = readerContext.getPageDataPipeline().getPageStartFromProgress(midPage.getLastChar().ParagraphIndex, midPage.getLastChar().CharIndex + 1);
        }

        ELogger.log(tag, "获取进度数据完成");
        ELogger.log(tag, "startParagraphIndex/ startCharIndex+" + startParagraphIndex + "/" + startCharIndex);
        if (firstPage != null) {
            ELogger.log(tag, "firstPage:" + firstPage.toString());
            if (!firstPage.HasData()) {
                firstPage = null;
            }

        } else {
            ELogger.log(tag, "firstPage is null");
        }

        if (midPage != null) {
            ELogger.log(tag, "midPage:" + midPage.toString());
            if (!midPage.HasData()) {
                midPage = null;
            }

        } else {
            ELogger.log(tag, "midPage is null");
        }

        if (nextPage != null) {
            ELogger.log(tag, "nextPage:" + nextPage.toString());
            if (!nextPage.HasData()) {
                nextPage = null;
            }
        } else {
            ELogger.log(tag, "nextPage is null");
        }

        readerContext.getPageData().setFirstPage(firstPage);
        readerContext.getPageData().setMidPage(midPage);
        readerContext.getPageData().setLastPage(nextPage);

        ITxtTask txtTask = new DrawPrepareTask();
        txtTask.Run(callBack, readerContext);

    }
}
