package com.hw.readermain;

import com.hw.beans.IParagraph;
import com.hw.beans.Paragraph;

public class ParagraphModel implements IParagraphModel {
	private BookParagraphCacheCenter cacheCenter;

	public ParagraphModel(BookParagraphCacheCenter cacheCenter) {
		this.cacheCenter = cacheCenter;
		if(cacheCenter==null){
			throw new NullPointerException();
		}
	}

	@Override
	public IParagraph getParagraphByIndex(int paragraphindex) {
        Paragraph paragraph = new Paragraph();
        String str = cacheCenter.getParagraphString(paragraphindex);
        paragraph.setString(str);
        paragraph.setParagraphIndex(paragraphindex);
		return paragraph;
	}

	@Override
	public IParagraph findParagraphByCharIndex(long charnums) {
		
		 int index = cacheCenter.getParagraphIndexByCharNums(charnums);
		return index>=0?getParagraphByIndex(index):null;
	}

}
