package hwtxtreader.bean;


/**
 * @author huangwei
 * 2016下午5:22:48
 * 主页：http://blog.csdn.net/u014614038
 * 
 */
public interface Paragraph {

	void setParagraphIndex(int index);

	void Clear();

	void addStringdata(String str);
	
	String getStringdata();

	int getIndex();

}
