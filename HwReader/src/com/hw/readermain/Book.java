package com.hw.readermain;

import com.hw.beans.PageStyle;

public class Book {

	public static enum BookType {
		Txt,

	}

	/**
	 * 
	 */
	public BookType mBookType = BookType.Txt;

	public PageStyle PrePagestyle = PageStyle.horizontal;

	/**
	 * 
	 */
	public String BookPath;
	/**
	 * 编码
	 */
	public String BookCode;
	/**
	 * 字节长度
	 */
	public long BookLength;

	/**
	 * 
	 */
	public int PreReadParagraphIndex;

	/**
	 * 
	 */
	public int PreReadCharIndex;

	/**
	 * 
	 */
	public String BookName = "";
	/**
	 * 
	 */
	public String BOOKHashName = "";

	/**
	 * 
	 */
	public float PreReadProgress;

	/**
	 * 
	 */
	public int TotalReadTime;

	public BookType getmBookType() {
		return mBookType;
	}

	public void setmBookType(BookType mBookType) {
		this.mBookType = mBookType;
	}

	public String getBookPath() {
		return BookPath;
	}

	public void setBookPath(String bookPath) {
		BookPath = bookPath;
	}

	public String getBookCode() {
		return BookCode;
	}

	public void setBookCode(String bookCode) {
		BookCode = bookCode;
	}

	public long getBookLength() {
		return BookLength;
	}

	public void setBookLength(long bookLength) {
		BookLength = bookLength;
	}

	public int getPreReadParagraphIndex() {
		return PreReadParagraphIndex;
	}

	public void setPreReadParagraphIndex(int preReadParagraphIndex) {
		PreReadParagraphIndex = preReadParagraphIndex;
	}

	public int getPreReadCharIndex() {
		return PreReadCharIndex;
	}

	public void setPreReadCharIndex(int preReadCharIndex) {
		PreReadCharIndex = preReadCharIndex;
	}

	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		BookName = bookName;
	}

	public String getBOOKHashName() {
		return BOOKHashName;
	}

	public void setBOOKHashName(String bOOKHashName) {
		BOOKHashName = bOOKHashName;
	}

	public float getPreReadProgress() {
		return PreReadProgress;
	}

	public void setPreReadProgress(float preReadProgress) {
		PreReadProgress = preReadProgress;
	}

	public int getTotalReadTime() {
		return TotalReadTime;
	}

	public void setTotalReadTime(int totalReadTime) {
		TotalReadTime = totalReadTime;
	}

	@Override
	public String toString() {
		return "Book [mBookType=" + mBookType + ", BookPath=" + BookPath + ", BookCode=" + BookCode + ", BookLength="
				+ BookLength + ", PreReadParagraphIndex=" + PreReadParagraphIndex + ", PreReadCharIndex="
				+ PreReadCharIndex + ", BookName=" + BookName + ", BOOKHashName=" + BOOKHashName + ", PreReadProgress="
				+ PreReadProgress + ", TotalReadTime=" + TotalReadTime + "]";
	}

}
