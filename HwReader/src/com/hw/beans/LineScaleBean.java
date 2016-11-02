package com.hw.beans;

/**
 * @author 黄威
 * 2016年10月21日上午10:50:48
 * 主页：http://blog.csdn.net/u014614038
 */
public class LineScaleBean {
	
	private ILine<CharElement> line;
	private LineScaleBean pre;
	private LineScaleBean next;
	
	public LineScaleBean() {
	}

	public ILine<CharElement> getLine() {
		return line;
	}

	public void setLine(ILine<CharElement> line) {
		this.line = line;
	}

	public LineScaleBean getPre() {
		return pre;
	}

	public void setPre(LineScaleBean pre) {
		this.pre = pre;
	}

	public LineScaleBean getNext() {
		return next;
	}

	public void setNext(LineScaleBean next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "LineScaleBean [line=" + line + ", pre=" + pre + ", next=" + next + "]";
	}

}
