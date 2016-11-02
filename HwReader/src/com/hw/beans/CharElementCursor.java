package com.hw.beans;

/**
 * @author 黄威 2016年10月20日下午8:23:10 主页：http://blog.csdn.net/u014614038
 */
public class CharElementCursor implements ICursor<CharElement> {

	private ChartElementScaleBean currentelement;

	@Override
	public CharElement MoveTofirst() {
		while (!isFirst()) {
			currentelement = currentelement.getPre();
		}
		return currentelement.getCharElement();
	}

	@Override
	public CharElement MoveToLast() {
		while (!isLast()) {
			currentelement = currentelement.getNext();
		}
		
		return currentelement.getCharElement();

	}

	@Override
	public CharElement Next() {

		return currentelement.getNext() != null ? (currentelement = currentelement.getNext()).getCharElement() : null;
	}

	@Override
	public CharElement Pre() {

		return currentelement.getPre() != null ? (currentelement = currentelement.getPre()).getCharElement() : null;
	}

	@Override
	public Boolean isLast() {

		return currentelement.getNext() == null;
	}

	@Override
	public Boolean isFirst() {

		return currentelement.getPre() == null;
	}

	@Override
	public void addElement(CharElement t) {
		if (currentelement == null) {
			currentelement = new ChartElementScaleBean();
			currentelement.setCharElement(t);

		} else {
			ChartElementScaleBean scalebean = new ChartElementScaleBean();
			scalebean.setCharElement(t);
			scalebean.setPre(currentelement);
			currentelement.setNext(scalebean);
			currentelement = scalebean;
		}
	}

	@Override
	public Boolean HasData() {

		return currentelement != null;
	}

}
