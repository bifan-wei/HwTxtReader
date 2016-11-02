package com.hw.beans;

public class Paragraph implements IParagraph {

	public int index;
	public String paragraphstring = "";

	@Override
	public void setString(String ParagraphString) {
		if (ParagraphString != null) {
			paragraphstring = ParagraphString;
		}
	}

	@Override
	public void setChars(char[] chars) {
		if (chars != null) {
			paragraphstring = String.valueOf(chars);
		}
	}

	@Override
	public int Length() {

		return paragraphstring.length();
	}

	@Override
	public void setParagraphIndex(int index) {
		this.index = index;
	}

	@Override
	public int getParagraphIndex() {

		return index;
	}

	@Override
	public String getString() {

		return paragraphstring;
	}

	@Override
	public char[] getChars() {

		return getString().toCharArray();
	}

	@Override
	public void clear() {
		paragraphstring = "";
	}

	/**
	 * 超出的话返回为0长度的字符串
	 *
	 */
	@Override
	public String Skip(int nums) {
		if (nums >= Length()) {
			return paragraphstring = "";
		}
		return paragraphstring=(paragraphstring.substring(nums, Length()));
	}

	@Override
	public void leftNumsFromStart(int nums) {
		if(Length()>nums){
			paragraphstring = paragraphstring.substring(0, nums);
		}
	}

}
