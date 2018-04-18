package com.bifan.txtreaderlib.main;

import com.bifan.txtreaderlib.interfaces.IParagraphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* create by bifan-wei
* 2017-11-13
*/
public class ParagraphData implements IParagraphData {
    private int CharNum = 0;
    private HashMap<Integer, String> paragraph = new HashMap<>();
    private List<Integer> charIndex = new ArrayList<>();//每个段落开始字符位置

    @Override
    public void addParagraph(int ParaIndex, String p) {
        if (p != null) {
            paragraph.put(ParaIndex, p);
            charIndex.add(CharNum);
            CharNum += p.length();
        }
    }

    @Override
    public void addParagraph(String p) {
        addParagraph(paragraph.size(), p);
    }

    @Override
    public int getParagraphNum() {
        return paragraph.size();
    }

    @Override
    public int getCharNum() {
        return CharNum;
    }

    @Override
    public int findParagraphIndexByCharIndex(int CharIndex) {
        int pre = 0;
        int next = 0;
        int index = 0;

        for (Integer v : charIndex) {
            if (index == 0) {
                pre = v;
            } else {
                if (CharIndex >= pre && CharIndex < v) {
                    return index;
                } else {
                    pre = next;
                    next = v;
                }
            }
            index++;
        }
        return index;
    }


    @Override
    public int getParaStartCharIndex(int ParaIndex) {
        return charIndex.get(ParaIndex);
    }

    @Override
    public String getParagraphStr(int ParaIndex) {
        return paragraph.get(ParaIndex);
    }

    @Override
    public String getParagraphStrFromStart(int ParaIndex, int CharIndex) {
        String S = getParagraphStr(ParaIndex) + "";
        if (CharIndex < 0) CharIndex = 0;
        if (CharIndex > S.length()) CharIndex = S.length();
        return S.substring(CharIndex);
    }

    @Override
    public String getParagraphStrToEnd(int ParaIndex, int CharIndex) {
        String S = getParagraphStr(ParaIndex) + "";
        if (CharIndex < 0) CharIndex = 0;
        if (CharIndex > S.length()) CharIndex = S.length();
        return S.substring(0, CharIndex);
    }

    @Override
    public void Clear() {
        charIndex.clear();
        paragraph.clear();
        CharNum = 0;
    }

}
