package com.hw.readermain;

import com.hw.beans.IPage;

/**获取页数据的地方
 * @author 黄威
 * 2016年10月21日下午3:40:19
 * 主页：http://blog.csdn.net/u014614038
 */
public interface IPageModel<E> {

	
	/**从指定位置获取页数据
	 * @param paragraphindex
	 * @param charindex
	 * @return 指定paragraphindex位置超过数据时，返回空，指定charindexinparagraph超出段落字符数时，默认为最后一个
	 */
	public IPage<E> getPageFromPosition(int paragraphindex,int charindexinparagraph);
	
	/**从指定字符位置获取页数据，超出时返回null
	 * @param charindex
	 * @return
	 */
	public IPage<E> getPageFromCharPosition(long charindex);
	
	
	/**获取页总数
	 * @return
	 */
	public int getPageNums();
	
	
	/**超出数据的话返回-1
	 * @param charindex
	 * @return
	 */
	public int getPageIndexByCharIndex(long charindex);
	
}
