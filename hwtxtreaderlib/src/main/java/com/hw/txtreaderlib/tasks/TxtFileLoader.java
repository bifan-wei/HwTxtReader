package com.hw.txtreaderlib.tasks;

import com.hw.txtreaderlib.bean.TxtFileMsg;
import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.main.TxtReaderContext;
import com.hw.txtreaderlib.utils.ELogger;
import com.hw.txtreaderlib.utils.FileUtil;

import java.io.File;


/**
 * created by ： bifan-wei
 */

public class TxtFileLoader {
    private String tag = "TxtFileLoader";

    public void load(String filePath, TxtReaderContext readerContext, ILoadListener loadListener) {
        load(filePath, null, readerContext, loadListener);
    }

    public void load(String filePath, String fileName, TxtReaderContext readerContext, ILoadListener loadListener) {
        if (!FileUtil.FileExist(filePath)) {
            loadListener.onFail(TxtMsg.FileNoExist);
            return;
        }
        loadListener.onMessage("initFile start");
        initFile(filePath, fileName, readerContext);
        ELogger.log(tag, "initFile done");
        loadListener.onMessage("initFile done");
        ITxtTask txtTask = new FileDataLoadTask();
        txtTask.Run(loadListener, readerContext);

    }

    private void initFile(String filePath, String fileName, TxtReaderContext readerContext) {
        File file = new File(filePath);
        TxtFileMsg fileMsg = new TxtFileMsg();
        fileMsg.FileSize = file.getTotalSpace();
        fileMsg.FilePath = filePath;
        fileMsg.FileCode = FileUtil.getCharset(filePath);

        fileMsg.CurrentParagraphIndex = 0;
        fileMsg.CurrentParagraphIndex = 0;
        fileMsg.PreParagraphIndex = 0;
        fileMsg.PreCharIndex = 0;
        if (fileName == null||fileName.trim().length()==0) {
            fileName = file.getName();
        }
        fileMsg.FileName = fileName;
        readerContext.setFileMsg(fileMsg);
    }
}
