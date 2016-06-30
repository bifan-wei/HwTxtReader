package hwtxtreader.bean;


/**
 * @author huangwei
 * 2016下午5:22:57
 * 主页：http://blog.csdn.net/u014614038
 * 
 */
public interface ParagraphCache {

	public void addParagraph(Paragraph p);
	
	public void Clear();
	
	public Paragraph getParagraphByIndex(int Paragraphindex);
	
	public int getParagraphSize();
	
	public Boolean isHasParagraphCache();
	
	

}
