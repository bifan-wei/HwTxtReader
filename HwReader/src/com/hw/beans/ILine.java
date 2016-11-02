package com.hw.beans;

import java.util.List;

/**E： 行元素
 * @author 黄威
 * @param <E>
 * 2016年10月21日上午9:19:14
 * 主页：http://blog.csdn.net/u014614038
 */
public interface ILine<E> {
	/**
	 * @return
	 */
	public List<E> getElements();

	/**
	 * @param linelements
	 */
	public void setElements(List<E> linelements);

	/**
	 * @return
	 */
	public int getSizes();

	/**
	 * 判断是否有元素、是否有数据
	 * 
	 * @return
	 */
	public Boolean hasdata();

	/**
	 * 调用这个方法前需要确定是有数据的，建议先调hasdata()判断一下
	 * 
	 * @return
	 */
	public CharElement getFirstElement();

	/**
	 * 调用这个方法前需要确定是有数据的，建议先调hasdata()判断一下
	 * 
	 * @return
	 */
	public CharElement getLastElement();

	/**
	 * @param index
	 * @return
	 */
	public CharElement getCharElement(int index);

	/**
	 * @param charElementt
	 */
	public void addElement(E charElementt);

	/**
	 * 清楚行数据
	 */
	public void clear();

	/**
	 * @return
	 */
	public String toString();

	/**获取行遍历游标
	 * @return
	 */
	public ICursor<E> getCursors();
}
