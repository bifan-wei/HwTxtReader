package com.bifan.txtreaderlib.tasks;

import com.bifan.txtreaderlib.bean.Chapter;
import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.IChapter;
import com.bifan.txtreaderlib.interfaces.IChapterMatcher;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.IParagraphData;
import com.bifan.txtreaderlib.interfaces.ITxtTask;
import com.bifan.txtreaderlib.main.ParagraphData;
import com.bifan.txtreaderlib.main.TxtReaderContext;
import com.bifan.txtreaderlib.utils.ELogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * created by ： bifan-wei
 */

public class FileDataLoadTask implements ITxtTask {
    private String tag = "FileDataLoadTask";
    private IChapterMatcher chapterMatcher;

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        IParagraphData paragraphData = new ParagraphData();
        chapterMatcher = readerContext.getChapterMatcher();
        List<IChapter> chapter = new ArrayList<>();
        callBack.onMessage("start read file data");
        Boolean readSuccess = ReadData(readerContext.getFileMsg().FilePath, readerContext.getFileMsg().FileCode, paragraphData, chapter);
        if (readSuccess) {
            ELogger.log(tag, "ReadData readSuccess");
            callBack.onMessage(" read file data success");
            readerContext.setParagraphData(paragraphData);
            readerContext.setChapters(chapter);
            ITxtTask txtTask = new TxtConfigInitTask();
            txtTask.Run(callBack, readerContext);

        } else {
            callBack.onFail(TxtMsg.InitError);
            callBack.onMessage("ReadData fail on FileDataLoadTask");
        }
    }

    private Boolean ReadData(String filePath, String Charset, IParagraphData paragraphData, List<IChapter> chapters) {
        File file = new File(filePath);
        BufferedReader bufferedReader = null;
        ELogger.log(tag, "start to  ReadData");
        ELogger.log(tag, "--file Charset:" + Charset);
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), Charset));
            try {
                String data;
                int index = 0;
                int chapterIndex = 0;
                while ((data = bufferedReader.readLine()) != null) {
                    if (data.length() > 0) {
                        IChapter chapter = compileChapter(data, paragraphData.getCharNum(), index, chapterIndex);
                        paragraphData.addParagraph(data);
                        if (chapter != null) {
                            chapterIndex++;
                            chapters.add(chapter);
                        }
                        index++;
                    }
                }
                initChapterEndIndex(chapters, paragraphData.getParagraphNum());
                return true;
            } catch (IOException e) {
                ELogger.log(tag, "IOException:" + e.toString());
            }
        } catch (UnsupportedEncodingException e) {
            ELogger.log(tag, "UnsupportedEncodingException:" + e.toString());
        } catch (FileNotFoundException e) {
            ELogger.log(tag, "FileNotFoundException:" + e.toString());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @param chapters
     * @param paragraphNum
     */
    private void initChapterEndIndex(List<IChapter> chapters, int paragraphNum) {
        if (chapters != null && chapters.size() > 0) {
            for (int i = 0, sum = chapters.size(); i < sum; i++) {
                int nextIndex = i + 1;
                IChapter chapter = chapters.get(i);
                if (nextIndex < sum) {
                    int startIndex = chapter.getStartParagraphIndex();
                    int endIndex = chapters.get(nextIndex).getEndParagraphIndex()- 1;
                    if (endIndex < startIndex) {
                        endIndex = startIndex;
                    }
                    chapter.setEndParagraphIndex(endIndex);
                } else {
                    int endIndex = paragraphNum - 1;
                    endIndex = endIndex < 0 ? 0 : endIndex;
                    chapter.setEndParagraphIndex(endIndex);
                }
            }
        }
    }

    private static final String ChapterPatternStr = "(^.{0,3}\\s*第)(.{1,9})[章节卷集部篇回](\\s*)";

    /**
     * @param data              文本数据
     * @param chapterStartIndex 开始字符在全文中的位置
     * @param ParagraphIndex    段落位置
     * @param chapterIndex      章节位置
     * @return 没有识别到章节数据返回null
     */
    private IChapter compileChapter(String data, int chapterStartIndex, int ParagraphIndex, int chapterIndex) {
        if (chapterMatcher == null) {
            if (data.trim().startsWith("第") || data.contains("第")) {
                Pattern p = Pattern.compile(ChapterPatternStr);
                Matcher matcher = p.matcher(data);
                while (matcher.find()) {
                    int startIndex = 0;
                    int endIndex = data.length();
                    IChapter c = new Chapter(chapterStartIndex, chapterIndex, data, ParagraphIndex, ParagraphIndex, startIndex, endIndex);
                    return c;
                }
            }
            return null;
        } else {
            return chapterMatcher.match(data, ParagraphIndex);
        }

    }
}
