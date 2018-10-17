package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.bean.TxtChar;

import java.util.List;


/*
* create by bifan-wei
* 2017-11-14
*/
public interface ITxtLine {
    List<TxtChar> getTxtChars();
    int getCharNum();
    TxtChar getFirstChar();
    TxtChar getLastChar();
    TxtChar getChar(int index);
    ICursor<TxtChar> getCharCursor();
    String getLineStr();
    char[] getLineChar();
    Boolean HasData();
    void addChar(TxtChar txtChar);
    void Clear();
    int CurrentIndex();
    boolean isParagraphEndLine();//判断是否是段落最后一句
    void setParagraphEndLine(boolean isParagraphEndLine);
}
