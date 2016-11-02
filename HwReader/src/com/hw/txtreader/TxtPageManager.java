package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;

/**
 * @author 黄威
 * 2016年10月25日下午8:44:33
 * 主页：http://blog.csdn.net/u014614038
 */
public class TxtPageManager {
	private IPage<CharElement> PrePage;
	private IPage<CharElement> CurrentPage;
	private IPage<CharElement> NexPage;

	public void Clear() {
		PrePage = null;
		CurrentPage = null;
		NexPage = null;
	}

	public IPage<CharElement> getPrePage() {
		return PrePage;
	}

	public void setPrePage(IPage<CharElement> prePage) {
		PrePage = prePage;
	}

	public IPage<CharElement> getCurrentPage() {
		return CurrentPage;
	}

	public void setCurrentPage(IPage<CharElement> currentPage) {
		CurrentPage = currentPage;
	}

	public IPage<CharElement> getNexPage() {
		return NexPage;
	}

	public void setNexPage(IPage<CharElement> nexPage) {
		NexPage = nexPage;
	}

	
}
