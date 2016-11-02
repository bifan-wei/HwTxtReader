package com.hw.action;

import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtReaderContex;

/**
 *
 * HwReader阅读器是由黄威开发 创建时间：2016年10月29日下午11:34:48
 * 主页：http://blog.csdn.net/u014614038/
 */
public class TxtTextSizeReduceAction extends ReaderAction {

	public TxtTextSizeReduceAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		TxtReaderContex Contex = (TxtReaderContex) readerContex;
		Contex.mViewSetting.TextSize = Contex.mViewSetting.TextSize - 2;
		if (Contex.mViewSetting.TextSize < 30) {
			Contex.mViewSetting.TextSize = 30;
		}
		Contex.mPaintContex.CommitSetting();
		Contex.mPageCursor.moveToIndex(Contex.mBook.PreReadParagraphIndex, Contex.mBook.PreReadCharIndex);
		Contex.mBitmapManager.CommitDatatoBitmap();
		Contex.mBitmapManager.initDraw();
		Contex.getReaderView().postInvalidate();

	}

	@Override
	public Action getActionType() {
		return Action.reducetextsize;
	}

}
