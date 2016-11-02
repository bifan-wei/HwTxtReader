package com.hw.action;

import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威 2016年11月1日下午5:25:35 主页：http://blog.csdn.net/u014614038
 */
public class TxtTextSortChageAction extends ReaderAction {

	public TxtTextSortChageAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		TxtReaderContex Contex = (TxtReaderContex) readerContex;
		Contex.mPaintContex.CommitSetting();
		Contex.mPageCursor.moveToIndex(Contex.mBook.PreReadParagraphIndex, Contex.mBook.PreReadCharIndex);
		Contex.mBitmapManager.CommitDatatoBitmap();
		Contex.mBitmapManager.initDraw();
		Contex.getReaderView().postInvalidate();

	}

	@Override
	public Action getActionType() {

		return Action.changetextsort;
	}

}
