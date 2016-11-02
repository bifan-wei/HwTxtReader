package com.hw.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄威 2016年10月21日上午10:15:20 主页：http://blog.csdn.net/u014614038
 */
public class Page implements IPage<CharElement> {

	private List<ILine<CharElement>> lines = new ArrayList<ILine<CharElement>>();
	private int pageIndex;
	private int pageNums;

	@Override
	public List<ILine<CharElement>> getLines() {
		return lines;
	}

	@Override
	public void setPageIndex(int index) {
		pageIndex = index;
	}

	@Override
	public void addLine(ILine<CharElement> line) {
		if (line != null) {
			lines.add(line);
		}
	}

	@Override
	public int getPageIndex() {

		return pageIndex;
	}

	@Override
	public String getString() {
		String str = "";
		for (ILine<CharElement> l : lines) {
			str = str + l.toString();
		}

		return str;
	}

	@Override
	public String toString() {

		return getString();
	}

	@Override
	public char[] getChars() {

		return getString().toCharArray();
	}

	@Override
	public void clear() {
		lines.clear();
	}

	@Override
	public List<CharElement> getElements() {
		List<CharElement> list = new ArrayList<CharElement>();
		for (ILine<CharElement> l : lines) {
			list.addAll(l.getElements());
		}
		return list;
	}

	@Override
	public int getElementsSize() {

		return getElements().size();
	}

	@Override
	public int getLineSize() {

		return lines.size();
	}

	@Override
	public ICursor<CharElement> getElementCursor() {
		ICursor<CharElement> cursor = new CharElementCursor();
		for (ILine<CharElement> l : lines) {
			List<CharElement> elements = l.getElements();
			for (CharElement e : elements) {
				cursor.addElement(e);
			}
		}
		return cursor;
	}

	@Override
	public ICursor<ILine<CharElement>> getLineCursor() {
		ICursor<ILine<CharElement>> cursor = new LineCursor();
		for (ILine<CharElement> l : lines) {
			cursor.addElement(l);
		}
		return cursor;
	}

	@Override
	public void addLines(List<ILine<CharElement>> lines) {
		if (lines != null) {
			this.lines.addAll(lines);
		}
	}

	@Override
	public CharElement getFirstElement() {
		if (HasData()) {
			ILine<CharElement> firstline = getLines().get(0);
			if(firstline.hasdata()){
				return firstline.getFirstElement();
			}
		}
		return null;
	}

	@Override
	public CharElement getLastElement() {
		if (HasData()) {
			ILine<CharElement> firstline = getLines().get(getLineSize()-1);
			if(firstline.hasdata()){
				return firstline.getLastElement();
			}
		}
		return null;
	}

	@Override
	public Boolean HasData() {

		return getLineSize() > 0;
	}

	@Override
	public int getPageNmus() {
		
		return pageNums;
	}

	@Override
	public void setPageNmus(int pagenums) {
		
		this.pageNums = pagenums;
	}

	

}
