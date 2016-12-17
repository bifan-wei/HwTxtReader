package hwtxtreader.bean;

public interface ParagraphCache {

	public void addParagraph(Paragraph p);
	
	public void Clear();
	
	public Paragraph getParagraphByIndex(int Paragraphindex);
	
	public int getParagraphSize();
	
	public Boolean isHasParagraphCache();
	
	

}
