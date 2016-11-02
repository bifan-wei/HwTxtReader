package com.hw.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 行数据
 * 
 * @author 黄威 2016年10月20日下午7:51:10 主页：http://blog.csdn.net/u014614038
 */
public class Line implements ILine<CharElement>{

	private List<CharElement> elements = new ArrayList<CharElement>();

	public List<CharElement> getElements() {

		return elements;
	}

	public void setElements(List<CharElement> linelements) {
		this.elements = linelements;
	}

	public int getSizes() {
		return elements.size();
	}

	/**
	 * 判断是否有元素、是否有数据
	 * 
	 * @return
	 */
	public Boolean hasdata() {
		return getSizes() > 0;
	}

	/**
	 * 调用这个方法前需要确定是有数据的，建议先调hasdata()判断一下
	 * 
	 * @return
	 */
	public CharElement getFirstElement() {
		return getCharElement(0);
	}

	/**
	 * 调用这个方法前需要确定是有数据的，建议先调hasdata()判断一下
	 * 
	 * @return
	 */
	public CharElement getLastElement() {
		return getCharElement(getSizes() - 1);
	}

	public CharElement getCharElement(int index) {
		return elements.get(index);
	}

	public void addElement(CharElement charElementt) {
		getElements().add(charElementt);

	}

	public void clear() {
		getElements().clear();
	}

	@Override
	public String toString() {
		String str = "";
		for (CharElement c : elements) {
			str = str + c.getValue();
		}
		return str;
	}

	public ICursor<CharElement> getCursors() {
		CharElementCursor charElementCursor = new CharElementCursor();
		for (CharElement e : getElements()) {
			charElementCursor.addElement(e);
		}
		return charElementCursor;

	}
}
