package hwtxtreader.bean;

import java.util.ArrayList;
import java.util.List;

public class ParagraphCacheImp implements ParagraphCache {

	private List<Paragraph> paragraphs;

	public ParagraphCacheImp() {
		paragraphs = new ArrayList<>();
	}

	@Override
	public void addParagraph(Paragraph p) {
		paragraphs.add(p);
	}

	@Override
	public void Clear() {
		paragraphs.clear();
		System.gc();
		System.gc();
	}

	@Override
	public Paragraph getParagraphByIndex(int Paragraphindex) {

		
		if (Paragraphindex >= 0 && Paragraphindex < paragraphs.size()) {
			return paragraphs.get(Paragraphindex);
		}
		System.out.println("Paragraphindex:"+Paragraphindex);
		System.out.println("paragraphs.size():"+paragraphs.size());
		return null;
	}

	@Override
	public int getParagraphSize() {

		return paragraphs.size();
	}

	@Override
	public Boolean isHasParagraphCache() {
 
		return getParagraphSize() != 0;
	}

}
