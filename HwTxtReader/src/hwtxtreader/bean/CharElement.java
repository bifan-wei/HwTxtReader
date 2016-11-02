package hwtxtreader.bean;

public class CharElement {

	public char data;
	public int paragraphindex;
	public int charindex;
	public float Xposition;
	public float Yposition;

	
	public Boolean isSpace() {
		return Character.isWhitespace(data);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CharElement) {

			CharElement c = (CharElement) obj;
			return data == c.data && paragraphindex == c.charindex && charindex == c.charindex;
		}
		return false;
	}

}
