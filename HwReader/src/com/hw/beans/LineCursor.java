package com.hw.beans;

public class LineCursor implements ICursor<ILine<CharElement>> {
	private LineScaleBean current;

	@Override
	public ILine<CharElement> MoveTofirst() {
		while (current.getPre() != null) {
			current = current.getPre();
		}
		return current.getLine();
	}

	@Override
	public ILine<CharElement> MoveToLast() {
           while(current.getNext()!=null){
        	   current = current.getNext();
           }
		return current.getLine();
	}

	@Override
	public ILine<CharElement> Next() {

		return current.getNext()!=null?(current=current.getNext()).getLine():null;
	}

	@Override
	public ILine<CharElement> Pre() {

		return current.getPre()!=null?(current=current.getPre()).getLine():null;
	}

	@Override
	public Boolean isLast() {

		return current.getNext() == null;
	}

	@Override
	public Boolean isFirst() {

		return current.getPre() == null;
	}

	@Override
	public void addElement(ILine<CharElement> t) {
		if (current == null) {
			current = new LineScaleBean();
			current.setLine(t);
		} else {
			LineScaleBean s = new LineScaleBean();
			s.setPre(current);
			s.setLine(t);
			current.setNext(s);
			current = s;
		}
	}

	@Override
	public Boolean HasData() {

		return current != null;
	}

}
