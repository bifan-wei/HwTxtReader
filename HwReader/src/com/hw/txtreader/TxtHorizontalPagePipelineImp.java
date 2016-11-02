package com.hw.txtreader;

import java.util.ArrayList;
import java.util.List;

import com.hw.beans.CharElement;
import com.hw.beans.ILine;
import com.hw.beans.IPage;
import com.hw.beans.Line;
import com.hw.beans.Page;
import com.hw.beans.Paragraph;
import com.hw.readermain.BookParagraphCacheCenter;

import android.graphics.Paint;

/**
 * @author 黄威 2016年10月23日下午5:16:52 主页：http://blog.csdn.net/u014614038
 */
public class TxtHorizontalPagePipelineImp implements TxtPagePipeline {

	TxtReaderContex readerContex;

	public TxtHorizontalPagePipelineImp(TxtReaderContex readerContex) {
		this.readerContex = readerContex;

		if (readerContex == null) {
			throw new NullPointerException();
		}

	}

	@Override
	public int getPageSize() {

		return readerContex.mPageSizeCalculator.getPageNums();
	}

	@Override
	public int getCurrentPageIndex(long charnums) {

		return readerContex.mPageSizeCalculator.getPageNumsByCharNums(charnums);
	}

	@Override
	public IPage<CharElement> getPageFromStart(int startparagraphindex, int startcharindex) {

		Paint paint = readerContex.mPaintContex.TextPaint;
		float measurewidth = readerContex.mPaintContex.Viewwidth - readerContex.mViewSetting.Paddingleft
				- readerContex.mViewSetting.Paddingright;
		BookParagraphCacheCenter datas = readerContex.mCacheCenter;
		int psize = datas.getParagraphSize();
		int linenums = readerContex.mPaintContex.PageLineNums;

		if (startparagraphindex >= psize) {// 超出数据的话，返回空
			return null;
		}

		int pindex = startparagraphindex;// 段落指针
		String paragraphstr = datas.getParagraphString(pindex);

		if (paragraphstr == null) {// 找不到数据，返回空
			return null;
		}

		if (startparagraphindex >= (psize - 1) && startcharindex >= paragraphstr.length()) {// 如果已经是最后的话返回空
			return null;
		}

		Paragraph paragraph = new Paragraph();
		paragraph.index = pindex;
		paragraph.setString(paragraphstr);// 将该开始段落获取
		startcharindex = startcharindex < paragraph.Length() ? startcharindex : paragraph.Length();
		paragraph.Skip(startcharindex);
		Page page = new Page();

		while (page.getLineSize() < linenums) {

			// 获取该段落行数据
			List<ILine<CharElement>> plines = getLinesFromParagraph(paragraph, startcharindex, paint, measurewidth);

			for (ILine<CharElement> l : plines) {
				if (page.getLineSize() < linenums) {
					page.addLine(l);

				} else {
					return page;
				}

			}
			// 有数据
			pindex++;
			startcharindex = 0;

			if (pindex < psize) {
				paragraph = new Paragraph();
				paragraph.index = pindex;
				String str = datas.getParagraphString(pindex);

				paragraph.setString(str);
			} else {
				break;
			}

		}

		return page;
	}

	@Override
	public IPage<CharElement> getPageFromEnd(int endparagraphindex, int endcharindex) {
		Paint paint = readerContex.mPaintContex.TextPaint;
		float measurewidth = readerContex.mPaintContex.Viewwidth - readerContex.mViewSetting.Paddingleft
				- readerContex.mViewSetting.Paddingright;
		
		BookParagraphCacheCenter datas = readerContex.mCacheCenter;
		int psize = datas.getParagraphSize();
		int linenums = readerContex.mPaintContex.PageLineNums;

		if (endparagraphindex >= (psize - 1)) {
			endparagraphindex = psize - 1;
		}

		if (endparagraphindex < 0) {
			endcharindex = 0;
		}

		if (endparagraphindex <= 0 && endcharindex <= 0) {// 如果是最开始位置的话，返回null
			return null;
		}

		int startcharindex = 0;
		int pindex = endparagraphindex;// 段落指针
		String paragraphstr = datas.getParagraphString(pindex);
		Paragraph paragraph = new Paragraph();
		paragraph.index = pindex;
		paragraph.setString(paragraphstr);// 将该开始段落获取

		endcharindex = endcharindex < paragraph.Length() ? endcharindex : paragraph.Length();
		endcharindex = endcharindex < 0 ? 0 : endcharindex;

		paragraph.leftNumsFromStart(endcharindex);

		Page page = new Page();

		List<ILine<CharElement>> lines = new ArrayList<ILine<CharElement>>();
		while (lines.size() < linenums) {

			// 获取该段落行数据
			List<ILine<CharElement>> plines = getLinesFromParagraph(paragraph, startcharindex, paint, measurewidth);

			for (int i = plines.size() - 1; i >= 0; i--) {
				if (lines.size() < linenums) {
					lines.add(plines.get(i));

				} else {
					break;
				}

			}
			// 有数据
			pindex--;

			if (pindex >= 0) {
				paragraph = new Paragraph();
				paragraph.index = pindex;
				String str = datas.getParagraphString(pindex);

				paragraph.setString(str);
			} else {

				break;
			}

		}

		if (lines.size() < linenums) {// 这是没有填充完的情况,从头开始填充
			return getPageFromStart(0, 0);
		} else {
			if (lines.size() > 0) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					page.addLine(lines.get(i));
				}
			}
		}

		return page;
	}

	/**
	 * @param paragraph
	 * @param startcharindexinparagraph
	 *            开始字符在段落的字符位置
	 * @return没有数据时返回长度为0的集合，不会为null
	 */
	private List<ILine<CharElement>> getLinesFromParagraph(Paragraph paragraph, int startcharindexinparagraph,
			Paint paint, float measurewidth) {

		List<ILine<CharElement>> lines = new ArrayList<ILine<CharElement>>();

		if (paragraph == null || paragraph.Length() == 0) {
			return lines;
		}

		int braeknums = 0;
		int index = startcharindexinparagraph;
		while (paragraph.Length() > 0) {
			braeknums = paint.breakText(paragraph.paragraphstring, true, measurewidth, null);
			String linestr = paragraph.paragraphstring.substring(0, braeknums);
			paragraph.Skip(braeknums);
			Line line = MakeStringtoLine(linestr, paragraph.index, index);

			if (line != null) {
				lines.add(line);
				index = index + braeknums;
			}

		}

		return lines;

	}

	/**
	 * @param linestr
	 * @param paragraphindex
	 *            属于的段落位置
	 * @param stringstartcharindexinparagraph
	 *            字符串开始字符在段落的字符位置
	 * @return
	 */
	private Line MakeStringtoLine(String linestr, int paragraphindex, int stringstartcharindexinparagraph) {
		if (linestr == null || linestr.length() == 0)
			return null;

		Line line = new Line();
		char[] chars = linestr.toCharArray();

		int index = stringstartcharindexinparagraph;
		for (char c : chars) {
			CharElement element = new CharElement();
			element.paragraphindex = paragraphindex;
			element.indexinparagraph = index++;
			element.chardata = c;
			line.addElement(element);

		}

		return line;

	}

}
