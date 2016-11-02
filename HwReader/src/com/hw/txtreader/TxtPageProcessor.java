package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.ILine;
import com.hw.beans.IPage;
import com.hw.beans.PageStyle;
import com.hw.readermain.BookBitmapProcessor;
import com.hw.readermain.BookPageProcessor;
import com.hw.readermain.CallBack;
import com.hw.readermain.IPageSizeCalculator;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderException.Type;

import android.util.Log;

/**
 * @author 黄威 2016年10月25日下午8:22:47 主页：http://blog.csdn.net/u014614038
 */
public class TxtPageProcessor extends BookPageProcessor {

	@Override
	public void Process(CallBack<Type> callBack, ReaderContex readerContex) {
		TxtReaderContex contex = (TxtReaderContex) readerContex;
		contex.mPageStyle = contex.mBook.PrePagestyle;
		TxtPagePipeline pagePipeline = null;
		if (contex.mPageStyle == PageStyle.vertical) {
			pagePipeline = new TxtVerticalPagePipleLineImp(contex);
		} else if (contex.mPageStyle == PageStyle.horizontal) {
			pagePipeline = new TxtHorizontalPagePipelineImp(contex);
		}

		// TxtPagePipeline

		contex.mPagePipeline = pagePipeline;
		IPageSizeCalculator calculator = TxtPageSizeCalculator.getInstance(contex);
		contex.mPageSizeCalculator = calculator;
		TxtPageCursor cursor = new TxtPageCursor(contex);
		contex.mPageCursor = cursor;
		cursor.moveToIndex(readerContex.mBook.PreReadParagraphIndex, readerContex.mBook.PreReadCharIndex);
		BookBitmapProcessor processor = new TxtBitmapProcessor();
		processor.Process(callBack, contex);

	}

	@SuppressWarnings("unused")
	private void dolog(TxtReaderContex contex) {
		IPage<CharElement> pre = contex.mPageManager.getPrePage();
		IPage<CharElement> mid = contex.mPageManager.getCurrentPage();
		IPage<CharElement> nex = contex.mPageManager.getNexPage();
		if (pre != null && pre.HasData()) {
			for (ILine<CharElement> l : pre.getLines()) {
				Log.e("l", l.toString());
			}
		} else {
			Log.e("l", "pre is null");
		}
		Log.e("l", "-----------------");
		if (mid != null && mid.HasData()) {
			for (ILine<CharElement> l : mid.getLines()) {
				Log.e("l", l.toString());
			}
		} else {
			Log.e("l", "mid is null");
		}
		Log.e("l", "-----------------");
		if (nex != null && nex.HasData()) {
			for (ILine<CharElement> l : nex.getLines()) {
				Log.e("l", l.toString());
			}
		} else {
			Log.e("l", "nex is null");
		}
		Log.e("l", "-----------------");

		Log.e("l", "isfirst:" + contex.mPageCursor.isFirst());
		Log.e("l", "islast:" + contex.mPageCursor.isLast());

	}

}
