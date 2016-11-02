package com.hw.beans;

import java.util.List;

public interface IPage<E> {

	/**
	 * @return
	 */
	public List<ILine<E>> getLines();

	/**
	 * @param index
	 */
	public void setPageIndex(int index);

	/**
	 * 添加行数据
	 * 
	 * @param line,如果为空，不会添加进去
	 */
	public void addLine(ILine<E> line);
	
	/**
	 * 添加行数据
	 * 
	 * @param line,如果为空，不会添加进去
	 */
	public void addLines(List<ILine<E>> lines);

	/**
	 * 获取段落下标
	 * 
	 * @return
	 */
	public int getPageIndex();
	
	/**
	 * @return
	 */
	public int getPageNmus();
	
	/**
	 * @return
	 */
	public void setPageNmus(int pagenums);

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
	 * 获取元素形式 每次是返回新的数组，调用时才产生
	 * 
	 * @return
	 */
	public List<E> getElements();

	/**
	 * 获取元素个数
	 * 
	 * @return
	 */
	public int getElementsSize();

	/**
	 * 获取行个数
	 * 
	 * @return
	 */
	public int getLineSize();

	/**
	 * 获取元素游标 每次产生新的游标，调用是才产生
	 * 
	 * @return
	 */
	public ICursor<E> getElementCursor();

	/**
	 * 获取行游标 每次产生新的游标，调用是才产生
	 * 
	 * @return
	 */
	public ICursor<ILine<E>> getLineCursor();
	
	/**
	 * @return 无数据时返回null
	 */
	public E getFirstElement();
	
	/**
	 * @return  无数据时返回null
	 */
	public E getLastElement();
	
	/**判断是否有行数据
	 * @return
	 */
	public Boolean HasData();
	
	
}
