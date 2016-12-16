package hwtxtreader.bean;

public interface PageCache {

	public void addPage(Page p);

	public Page getFirestPage();

	public Page getLastPage();

	public Page getPage(int PageIndex);

	public void clear();

	public int getPagesize();

	public Page searClosestePage(CharElement element);
	
	public Boolean isHasData();

}
