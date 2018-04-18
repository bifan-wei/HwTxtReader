package com.bifan.txtreaderlib.tasks;

import com.bifan.txtreaderlib.interfaces.IChapter;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.IParagraphData;
import com.bifan.txtreaderlib.interfaces.ITxtTask;
import com.bifan.txtreaderlib.main.ParagraphData;
import com.bifan.txtreaderlib.main.TxtReaderContext;
import com.bifan.txtreaderlib.utils.ELogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bifan-wei
 * on 2018/1/28.
 */

public class TextLoader  {
    private String tag = "FileDataLoadTask";
    public void load(String text, TxtReaderContext readerContext, ILoadListener callBack) {
        IParagraphData paragraphData = new ParagraphData();
        List<IChapter> chapter = new ArrayList<>();
        callBack.onMessage("start read text");
        ELogger.log(tag, "start read text");
        paragraphData.addParagraph(text + "");
        readerContext.setParagraphData(paragraphData);
        readerContext.setChapters(chapter);
        ITxtTask txtTask = new TxtConfigInitTask();
        txtTask.Run(callBack, readerContext);
    }
}
