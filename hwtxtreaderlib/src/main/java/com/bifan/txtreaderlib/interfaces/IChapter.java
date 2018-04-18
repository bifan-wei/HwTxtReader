package com.bifan.txtreaderlib.interfaces;

/**
 * Created by HP on 2017/11/15.
 */

public interface IChapter {

    int getIndex();

    int getStartParagraphIndex();

    int getEndParagraphIndex();

    int getStartCharIndex();

    int getEndCharIndex();

    int getStartIndex();

    String getTitle();

    void setStartParagraphIndex(int index);

    void setEndParagraphIndex(int index);

}
