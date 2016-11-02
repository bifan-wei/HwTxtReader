package com.hw.beans;

/**
 * @author 黄威
 * @param <T>
 *            2016年10月20日下午8:06:37 主页：http://blog.csdn.net/u014614038
 */
public interface ICursor<T> {

	/**
	 * 移动到开始位置
	 */
	public T MoveTofirst();

	/**
	 * 移动到最后位置
	 */
	public T MoveToLast();

	/**如果没有下一个的话就会返回空
	 * @return
	 */
	public T Next();
	
	/**如果没有下一个的话就会返回空
	 * @return
	 */
	public T Pre();

	/**判断是否是最后一个
	 * @return
	 */
	public Boolean isLast();

	/**判断是否是第一个
	 * @return
	 */
	public Boolean isFirst();
	
	/**
	 * 添加游标元素
	 */
	public void addElement(T t);
	
	/**判断是否有数据
	 * @return
	 */
	public Boolean HasData();
}
