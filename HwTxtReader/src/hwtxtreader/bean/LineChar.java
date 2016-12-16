package hwtxtreader.bean;

/**
 * @author huangwei
 * 2016下午5:13:50
 * 主页：http://blog.csdn.net/u014614038
 * 
 */
public interface LineChar {

	public int getElementSize();

	public void addElement(CharElement charElement);

	public CharElement getFirstElement();

	public String getLineString();

	public char[] getLineChars();

	public void clear();
	
	public Boolean hasdata();
	
	public CharElement getLastElement();

}
