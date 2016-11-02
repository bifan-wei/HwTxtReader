package hwtxtreader.main;

public interface TxtPageChangeListsner {
	/**@param saparatepagedone 是否分页完成了
	 * @param pageindex 当前页下标
	 * @param pagenums 页数，分页没完成是返回的是-1
	 */
	public void onCurrentPage(int pageindex,int pagenums);
	
	

}
