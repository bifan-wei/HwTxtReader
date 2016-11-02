package com.hw.readermain;

import com.hw.beans.IParagraph;

/**段落数据获取的地方
 * @author 黄威
 * 2016年10月21日上午11:25:15
 * 主页：http://blog.csdn.net/u014614038
 */
public interface IParagraphModel {

	/**根据段落位置获取段落
	 * @param paragraphindex
	 * @return 越界会有异常
	 */
	public IParagraph getParagraphByIndex(int paragraphindex);
	
	/**
	 * @param charindex
	 * @return 找不到返回null
	 * 
	 */
	public IParagraph findParagraphByCharIndex(long charnums);
	
}
