package com.hw.action;

import com.hw.beans.PageStyle;
import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtHorizontalPagePipelineImp;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威 2016年11月2日下午3:39:04 主页：http://blog.csdn.net/u014614038
 */
public class TxtPageTypesettingChangeTohorizontalAction extends ReaderAction {

	public TxtPageTypesettingChangeTohorizontalAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		TxtReaderContex Contex = (TxtReaderContex) readerContex;
		Contex.mPagePipeline = new TxtHorizontalPagePipelineImp(Contex);
		Contex.mBook.PrePagestyle = PageStyle.horizontal;
		Contex.mPageStyle =  PageStyle.horizontal;
		Contex.mPaintContex.CommitSetting();
		Contex.mPageCursor.moveToIndex(Contex.mBook.PreReadParagraphIndex, Contex.mBook.PreReadCharIndex);
		Contex.mBitmapManager.CommitDatatoBitmap();
		Contex.mBitmapManager.initDraw();
		Contex.getReaderView().postInvalidate();

	}

	@Override
	public Action getActionType() {

		return Action.TypesettingChangetohorozontal;
	}

}
