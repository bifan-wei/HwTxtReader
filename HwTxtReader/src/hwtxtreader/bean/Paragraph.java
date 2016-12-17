package hwtxtreader.bean;


public interface Paragraph {

	void setParagraphIndex(int index);

	void Clear();

	void addStringdata(String str);
	
	String getStringdata();

	int getIndex();

}
