package com.hw.txtreader;
/**
 * @author 黄威
 * 2016年11月2日上午10:02:46
 * 主页：http://blog.csdn.net/u014614038
 */
public interface ITxtPageChangeListener {
	
	/**
	 * @param currentpageindex 当前页码
	 * @param pageunms 总页数
	 */
	public void onPageChange(int currentpageindex,int pageunms);

}
