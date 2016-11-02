package com.hw.readermain;

/**
 * @author 黄威 2016年10月19日下午4:13:21 主页：http://blog.csdn.net/u014614038
 */
public abstract class BookProcessor<T> {
	public abstract void Process(CallBack<T> callBack, ReaderContex readerContex);
}
