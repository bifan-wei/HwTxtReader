package hwtxtreader.bean;

import java.util.ArrayList;
import java.util.List;

public class LineCharImp implements LineChar {
	private List<CharElement> linedata;

	public LineCharImp() {
		linedata = new ArrayList<>();
	}

	@Override
	public int getElementSize() {

		return linedata.size();
	}

	@Override
	public void addElement(CharElement charElement) {
		linedata.add(charElement);
	}

	@Override
	public CharElement getFirstElement() {

		return getElementSize() == 0 ? null : linedata.get(0);
	}

	@Override
	public String getLineString() {

		return new String(getLineChars());
	}

	@Override
	public char[] getLineChars() {

		char[] cs = new char[linedata.size()];
		int i = 0;
		for (CharElement e : linedata) {
			cs[i++] = e.data;
		}
		return cs;

	}

	@Override
	public void clear() {
		linedata.clear();
	}

	@Override
	public Boolean hasdata() {

		return getElementSize() != 0;
	}

	@Override
	public CharElement getLastElement() {

		return getElementSize() == 0 ? null : linedata.get(getElementSize() - 1);
	}

}
