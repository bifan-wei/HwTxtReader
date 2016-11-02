package com.hw.beans;

/**
 * E :单个元素
 * 
 * @author 黄威 2016年10月21日上午9:31:46 主页：http://blog.csdn.net/u014614038
 */
public interface IParagraph {

	/**
	 * @param ParagraphString
	 */
	public void setString(String ParagraphString);

	/**
	 * @param chars
	 */
	public void setChars(char[] chars);

	/**
	 * 返回字符个数
	 * 
	 * @return
	 */
	public int Length();

	/**
	 * @param index
	 */
	public void setParagraphIndex(int index);

	/**
	 * 获取段落下标
	 * 
	 * @return
	 */
	public int getParagraphIndex();

	/**
	 * 获取字符串形式
	 * 
	 * @return
	 */
	public String getString();

	/**
	 * 获取字符数组形式 每次是返回新的数组，调用时才产生
	 * 
	 * @return
	 */
	public char[] getChars();

	/**
	 * 清空
	 */
	public void clear();

	/**
	 * @return 
	 */
	public String Skip(int nums);
	
	/**
	 * 从开始位置起，留下nums个字符，nums超过数据长度，保留原理长度数据
	 */
	public void leftNumsFromStart(int nums);
}
