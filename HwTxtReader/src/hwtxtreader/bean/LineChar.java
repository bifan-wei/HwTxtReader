package hwtxtreader.bean;


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
