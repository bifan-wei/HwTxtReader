package com.hw.action;

import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威
 * 2016年11月2日上午11:01:25
 * 主页：http://blog.csdn.net/u014614038
 */
public class TxtProgressAction extends ReaderAction {
	private int progressindex;

	public TxtProgressAction(ReaderContex readerContex) {
		super(readerContex);

	}

	public void setPageProgress(int pageindex) {
		progressindex = pageindex;
	}

	@Override
	public void Run() {
		TxtReaderContex Contex = (TxtReaderContex) readerContex;
		Contex.mPageCursor.movePageIndex(progressindex);
		Contex.mBitmapManager.CommitDatatoBitmap();
		Contex.mBitmapManager.initDraw();
		Contex.getReaderView().postInvalidate();

	}

	@Override
	public Action getActionType() {

		return Action.progresschange;
	}

}