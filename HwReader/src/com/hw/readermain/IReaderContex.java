package com.hw.readermain;

/**阅读器上下文接口类
 * @author 黄威
 * 2016年10月19日下午3:48:42
 * 主页：http://blog.csdn.net/u014614038
 */
public interface IReaderContex {

	public EvenProcessor getEvenProcessor();
	
	public CavansProcessor getCavansProcessor();
}
