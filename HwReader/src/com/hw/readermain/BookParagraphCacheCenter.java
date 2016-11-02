package com.hw.readermain;

import java.util.ArrayList;
import java.util.List;

/**
 * 退出阅读器尽量调用一下clear方法,或者切换书籍时也调用一下clear
 * 
 * @author 黄威 2016年10月25日下午3:46:25 主页：http://blog.csdn.net/u014614038
 */
public class BookParagraphCacheCenter {

	/**
	 * 这里使用了全部保存在内存去的方式，有可能导致占据过多内存而奔溃
	 */
	private List<String> paragraphs = new ArrayList<String>();
	private long[] charcounts = new long[] {};

	public void addParagraphStr(String str) {
		if (str != null) {
			paragraphs.add(str);
			long[] newcounts = new long[charcounts.length + 1];
			if (charcounts.length > 0) {
				newcounts[charcounts.length] = charcounts[charcounts.length - 1] + str.length();
			} else {
				newcounts[charcounts.length] = str.length();
			}
			System.arraycopy(charcounts, 0, newcounts, 0, charcounts.length);
			charcounts = newcounts;

		}

	}

	private int BinarySearch(long charnums) {
		if (getParagraphSize() == 0 || charnums > getCharCounts()[getParagraphSize() - 1]) {
			return -1;
		}

		int startindex = 0;
		int endindex = getCharCounts().length - 1;

		while (startindex < endindex) {
			int middle = (startindex + endindex) / 2;
			if (charnums < getCharCounts()[middle]) {
				endindex = middle;
			} else if (charnums > getCharCounts()[middle]) {
				startindex = middle;
			} else {
				return middle;
			}
			if (startindex + 1 == endindex) {
				break;
			}
		}

		// 循环后只有两种情况：1.startindex + 1 == endindex 2.startindex = endindex=0

		if (charnums <= getCharCounts()[startindex]) {
			return startindex;
		}

		if (charnums <= getCharCounts()[endindex]) {
			return endindex;
		}

		return -1;
	}

	/**
	 * 获取指定段落字符串
	 * 
	 * @param index
	 * @return 没有段落数据，返回null
	 */
	public String getParagraphString(int index) {
		if(paragraphs.size()==0){
			return null;
		}
		return paragraphs.get(index);
	}

	/**
	 * 如果没有数据，返回0
	 * 
	 * @return
	 */
	public long getAllCharNums() {
		return getParagraphSize() > 0 ? getCharCounts()[getParagraphSize() - 1] : 0;
	}

	/**
	 * @param charnums
	 *            字符数
	 * @return 如果没有找到段落数据，返回-1
	 */
	public int getParagraphIndexByCharNums(long charnums) {
		return BinarySearch(charnums);
	}

	public long[] getCharCounts() {
		return charcounts;
	}

	public int getParagraphSize() {
		return getCharCounts().length;
	}

	/**
	 * @param ParagraphIndex
	 *            小于0的话返回0，大于数据长度的话，返回总字符数
	 * @return
	 */
	public long getChartNumsByParagraphindex(int ParagraphIndex) {
		if (ParagraphIndex > charcounts.length) {
			return getAllCharNums();
		} else if (ParagraphIndex < 0) {
			return 0;
		}
		return charcounts[ParagraphIndex];
	}

	/**
	 * 释放缓存
	 */
	public void clear() {
		for (@SuppressWarnings("unused")
		String ps : paragraphs) {
			ps = null;
		}
		paragraphs.clear();
		charcounts = null;
		System.gc();
		System.gc();
	}

}
