package com.hw.beans;

/**
 * @author 黄威
 * 2016年10月20日下午7:38:15
 * 主页：http://blog.csdn.net/u014614038
 */
public class CharElement {
	public char chardata;
	public float top;
	public float bottom;
	public float left;
	public float right;
	public int paragraphindex;
	public int indexinparagraph;
	
	
	
	public char getChardata() {
		return chardata;
	}

	public void setChardata(char chardata) {
		this.chardata = chardata;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public float getBottom() {
		return bottom;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public int getParagraphindex() {
		return paragraphindex;
	}

	public void setParagraphindex(int paragraphindex) {
		this.paragraphindex = paragraphindex;
	}

	public int getIndexinparagraph() {
		return indexinparagraph;
	}

	public void setIndexinparagraph(int indexinparagraph) {
		this.indexinparagraph = indexinparagraph;
	}

	public String getValue(){
		return String.valueOf(chardata);
	}
	
	@Override
	public String toString() {
		return "CharElementt [chardata=" + chardata + ", top=" + top + ", bottom=" + bottom + ", left=" + left
				+ ", right=" + right + ", paragraphindex=" + paragraphindex + ", indexinparagraph=" + indexinparagraph
				+ "]";
	}

}
