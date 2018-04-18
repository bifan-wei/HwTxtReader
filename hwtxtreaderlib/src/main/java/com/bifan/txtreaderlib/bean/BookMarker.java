package com.bifan.txtreaderlib.bean;

/*
* create by bifan-wei
* 2017-11-13
*/
public class BookMarker {
    public String Title;
    public int StartParagraphIndex;
    public int StartCharIndex;

    @Override
    public String toString() {
        return "BookMarker{" +
                "Title='" + Title + '\'' +
                ", StartParagraphIndex=" + StartParagraphIndex +
                ", StartCharIndex=" + StartCharIndex +
                '}';
    }
}
