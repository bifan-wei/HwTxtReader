package com.bifan.txtreaderlib.interfaces;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface IParagraphData {
    void addParagraph(int ParaIndex, String p);
    void addParagraph(String p);
    int getParagraphNum();
    int getCharNum();
    int findParagraphIndexByCharIndex(int CharIndex);
    int getParaStartCharIndex(int ParaIndex);
    String getParagraphStr(int ParaIndex);
    /**
     * @param ParaIndex 段落位置
     * @param CharIndex 字符位置
     * @return 如果段落位置超出范围，报越界异常，如果字符位置超出段落范围，返回空字符
     */
    String getParagraphStrFromStart(int ParaIndex,int CharIndex);

    /**
     * @param ParaIndex
     * @param CharIndex
     * @return  如果段落位置超出范围，报越界异常，如果字符位置超出段落范围，返回空字符
     */
    String getParagraphStrToEnd(int ParaIndex,int CharIndex);

    void Clear();

}
