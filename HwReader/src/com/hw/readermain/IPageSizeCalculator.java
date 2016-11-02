package com.hw.readermain;
/**
 * @author 黄威
 * 2016年10月25日下午4:05:40
 * 主页：http://blog.csdn.net/u014614038
 */
public interface IPageSizeCalculator {
	
	/**
	 * @return 返回当前页数，每次调用都会计算一遍，请谨慎
	 */
	public int getPageNums();
	
	/**
	 * @param chartnums 字符数
	 * @return
	 */
	public int getPageNumsByCharNums(long chartnums);
	
	
	
	
	

}
