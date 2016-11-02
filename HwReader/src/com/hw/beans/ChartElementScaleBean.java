package com.hw.beans;

public class ChartElementScaleBean {

	private ChartElementScaleBean pre;
	private ChartElementScaleBean next;
	private CharElement charElement;

	public ChartElementScaleBean() {
		
	}

	public ChartElementScaleBean(CharElement charElement, ChartElementScaleBean preelement, ChartElementScaleBean nextelement) {
		setCharElement(charElement);
		setPre(preelement);
		setNext(nextelement);

	}

	public ChartElementScaleBean getPre() {
		return pre;
	}

	public void setPre(ChartElementScaleBean pre) {
		this.pre = pre;
	}

	public ChartElementScaleBean getNext() {
		return next;
	}

	public void setNext(ChartElementScaleBean next) {
		this.next = next;
	}

	public CharElement getCharElement() {
		return charElement;
	}

	public void setCharElement(CharElement charElement) {
		this.charElement = charElement;
	}

}
