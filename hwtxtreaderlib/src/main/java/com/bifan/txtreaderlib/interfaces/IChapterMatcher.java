package com.bifan.txtreaderlib.interfaces;

/**
 * created by ： bifan-wei
 */

public interface IChapterMatcher {
    /**
     * @param paragraphData 段落数据
     * @param ParagraphIndex 当前段落位置
     * @return
     */
    IChapter match(String paragraphData, int ParagraphIndex);
}
