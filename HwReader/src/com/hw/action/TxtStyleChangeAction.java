package com.hw.action;

import com.hw.beans.PageStyle;
import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtHorizontalPagePipelineImp;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威 2016年11月2日下午2:39:40 主页：http://blog.csdn.net/u014614038
 */
public class TxtStyleChangeAction extends ReaderAction {

	public TxtStyleChangeAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		TxtReaderContex contex = (TxtReaderContex) readerContex;
		int backgroungrs = contex.mViewSetting.getPageBackground().getBrawable();
		int textcolor = contex.mViewSetting.getPageBackground().getTextColor();
		contex.mBook.PrePagestyle = PageStyle.horizontal;
		contex.mPageStyle =  PageStyle.horizontal;
		contex.mPagePipeline = new TxtHorizontalPagePipelineImp(contex);		
		contex.mViewSetting.TextColor = textcolor;
		contex.mPaintContex.CommitSetting();
		contex.mBitmapManager.ChangePageBitmapColor(contex.mContext.getResources(), backgroungrs,
				(int) contex.mPaintContex.Viewwidth, (int) contex.mPaintContex.Viewheight);
		
		contex.mPageCursor.moveToIndex(contex.mBook.PreReadParagraphIndex, contex.mBook.PreReadCharIndex);
		contex.mBitmapManager.CommitDatatoBitmap();
		contex.mBitmapManager.initDraw();
		contex.getReaderView().postInvalidate();
	}

	@Override
	public Action getActionType() {

		return Action.backgroundstylechanged;
	}

}
