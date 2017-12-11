package com.hw.txtreaderlib.bean;

import com.hw.txtreaderlib.interfaces.IChapter;

/*
* create by bifan-wei
* 2017-11-13
*/
public class Chapter implements IChapter {
    public String Title;
    public int Index;
    public int StartParagraphIndex;
    public int EndParagraphIndex;
    public int StartCharIndex;
    public int EndCharIndex;

    public Chapter(String title, int startParagraphIndex, int endParagraphIndex) {
        Title = title;
        StartParagraphIndex = startParagraphIndex;
        EndParagraphIndex = endParagraphIndex;
    }

    public Chapter(int index,String title, int startParagraphIndex, int endParagraphIndex, int startCharIndex, int endCharIndex) {
        Title = title;
        Index = index;
        StartParagraphIndex = startParagraphIndex;
        EndParagraphIndex = endParagraphIndex;
        StartCharIndex = startCharIndex;
        EndCharIndex = endCharIndex;
    }

    public Chapter() {

    }

    public void setTitle(String title) {
        Title = title;
    }


    @Override
    public int getStartCharIndex() {
        return StartCharIndex;
    }

    @Override
    public int getEndCharIndex() {
        return EndCharIndex;
    }

    @Override
    public int getStartParagraphIndex() {
        return StartParagraphIndex;
    }

    @Override
    public int getEndParagraphIndex() {
        return EndParagraphIndex;
    }

    @Override
    public String getTitle() {
        return Title;
    }

    @Override
    public void setStartParagraphIndex(int index) {
      StartParagraphIndex = index;
    }

    @Override
    public void setEndParagraphIndex(int index) {
      EndParagraphIndex = index;
    }

    @Override
    public int getIndex() {
        return Index;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "Title='" + Title + '\'' +
                ", Index=" + Index +
                ", StartParagraphIndex=" + StartParagraphIndex +
                ", EndParagraphIndex=" + EndParagraphIndex +
                ", StartCharIndex=" + StartCharIndex +
                ", EndCharIndex=" + EndCharIndex +
                '}';
    }
}
