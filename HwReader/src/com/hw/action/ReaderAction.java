package com.hw.action;

import com.hw.readermain.ReaderContex;

/**
 *
 * HwReader阅读器是由黄威开发
 * 创建时间：2016年10月29日下午11:08:24
 * 主页：http://blog.csdn.net/u014614038/
 */
public abstract class ReaderAction {
	protected ReaderContex readerContex;

	public ReaderAction(ReaderContex readerContex) {
		this.readerContex = readerContex;
		if (readerContex == null) {
			throw new NullPointerException();
		}
	}
	
	/**
	 * 执行操作
	 */
	public abstract void Run();
	
	/**
	 * @return
	 */
	public abstract Action getActionType();

}
