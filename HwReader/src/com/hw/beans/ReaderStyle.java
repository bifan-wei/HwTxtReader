package com.hw.beans;

/**
 * @author 黄威 2016年11月2日下午1:44:25 主页：http://blog.csdn.net/u014614038
 */
public abstract class ReaderStyle {

	public enum Style {
		normal, sorft, night, nice, ancientry2
	}

	public abstract int getBrawable();

	public abstract Style getStyle();
	
	public abstract int getTextColor();

	/*public int getSaveStylecode() {
		switch (getStyle()) {
		case normal:
			return 1;
		case sorft:
			return 2;
		case night:
			return 3;
		case ancientry1:
			return 4;
		case ancientry2:
			return 5;
		default:
			return -1;
		}
	}*/
}
