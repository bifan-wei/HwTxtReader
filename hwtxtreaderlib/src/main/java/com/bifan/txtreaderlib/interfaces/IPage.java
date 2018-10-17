package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.bean.TxtChar;

import java.util.List;


/*
* create by bifan-wei
* 2017-11-13
*/
public interface IPage {
    ITxtLine getLine(int index);
    void addLine(ITxtLine line);
    void addLineTo(ITxtLine line, int index);
    void setLines(List<ITxtLine> lines);
    TxtChar getFirstChar();
    TxtChar getLastChar();
    ITxtLine getFirstLine();
    ITxtLine getLastLine();
    List<ITxtLine> getLines();
    ICursor<ITxtLine> getLineCursor();
    int getLineNum();
    boolean isFullPage();//是否满页了
    void setFullPage(boolean fullPage);
    int CurrentIndex();
    Boolean HasData();
}
