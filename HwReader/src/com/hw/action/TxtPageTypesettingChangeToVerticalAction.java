package com.hw.action;

import com.hw.beans.PageStyle;
import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtHorizontalPagePipelineImp;
import com.hw.txtreader.TxtReaderContex;
import com.hw.txtreader.TxtVerticalPagePipleLineImp;

/**
 * @author 黄威 2016年11月2日下午3:44:16 主页：http://blog.csdn.net/u014614038
 */
public class TxtPageTypesettingChangeToVerticalAction extends ReaderAction {

	public TxtPageTypesettingChangeToVerticalAction(ReaderContex readerContex) {
		super(readerContex);
		
	}

	@Override
	public void Run() {
		TxtReaderContex Contex = (TxtReaderContex) readerContex;
		Contex.mPagePipeline = new TxtHorizontalPagePipelineImp(Contex);
		Contex.mBook.PrePagestyle = PageStyle.vertical;
		Contex.mPageStyle =  PageStyle.vertical;
		Contex.mPagePipeline = new TxtVerticalPagePipleLineImp(Contex);
		Contex.mPaintContex.CommitSetting();
		Contex.mPageCursor.moveToIndex(Contex.mBook.PreReadParagraphIndex, Contex.mBook.PreReadCharIndex);
		Contex.mBitmapManager.CommitDatatoBitmap();
		Contex.mBitmapManager.initDraw();
		Contex.getReaderView().postInvalidate();

	}

	@Override
	public Action getActionType() {

		return Action.TypesettingChangetovertical;
	}

}
