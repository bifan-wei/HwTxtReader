package com.bifan.txtreaderlib.bean;

/*
* create by bifan-wei
* 2017-11-13
*/
public class Note {
    public String Note;
    public int StartParagraphIndex;
    public int StartCharIndex;
    public int EndParagraphIndex;
    public int EndCharIndex;

    @Override
    public String toString() {
        return "Note{" +
                "Note='" + Note + '\'' +
                ", StartParagraphIndex=" + StartParagraphIndex +
                ", StartCharIndex=" + StartCharIndex +
                ", EndParagraphIndex=" + EndParagraphIndex +
                ", EndCharIndex=" + EndCharIndex +
                '}';
    }
}
