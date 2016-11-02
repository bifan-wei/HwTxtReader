package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;

/**页数据获取的地方,作用传入指定位置然后去缓存中获取相应 页数据，除外还有获取页码数据，每次获取页码数据都会重新计算页码
 * @author 黄威
 * 2016年10月23日下午5:10:18
 * 主页：http://blog.csdn.net/u014614038
 */
public interface TxtPagePipeline {

	/**获取页总数，每次调用这个方法都会重新计算页码总数
	 * @return
	 */
	public int getPageSize();
	
	
	
	/**获取当前页码,每次获取都会重新计算
	 * @param charnums 字符数
	 * @return
	 */
	public int getCurrentPageIndex(long charnums);
	
	
	/** 正序获取页数据
	 * @param startparagraphindex 开始段落位置
	 * @param startcharindex 开始字符位置
	 * @return 如果该位置起没有数据，返回null
	 */
	public IPage<CharElement> getPageFromStart(int startparagraphindex,int startcharindex);
	
	
	/**倒序获取页数据
	 * @param endparagraphindex 结束段落位置
	 * @param endcharindex 结束字符位置
	 * @return 如果该位置起没有数据，返回null，如果有数据但是没有填充满，会从最开始开始填充，填充完为止
	 */
	public IPage<CharElement> getPageFromEnd(int endparagraphindex,int endcharindex);
}
