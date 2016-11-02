package com.hw.readermain;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;

/**
 * @author 黄威
 * 2016年10月21日下午3:50:59
 * 主页：http://blog.csdn.net/u014614038
 */
public class PageModel implements IPageModel<CharElement>{

	@Override
	public IPage<CharElement> getPageFromPosition(int paragraphindex, int charindexinparagraph) {
		
		return null;
	}

	@Override
	public IPage<CharElement> getPageFromCharPosition(long charindex) {
		
		return null;
	}

	@Override
	public int getPageNums() {
		
		return 0;
	}

	@Override
	public int getPageIndexByCharIndex(long charindex) {
		
		return 0;
	}

	

}
