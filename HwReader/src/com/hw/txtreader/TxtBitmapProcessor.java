package com.hw.txtreader;

import com.hw.readermain.BookBitmapProcessor;
import com.hw.readermain.CallBack;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderException.Type;

public class TxtBitmapProcessor extends BookBitmapProcessor {

	@Override
	public void Process(CallBack<Type> callBack, ReaderContex readerContex) {

		TxtReaderContex txtreaderContex = (TxtReaderContex) readerContex;
		int backgroungrs = txtreaderContex.mViewSetting.getPageBackground().getBrawable();
		int textcolor = txtreaderContex.mViewSetting.getPageBackground().getTextColor();
		txtreaderContex.mBitmapManager.ChangePageBitmapColor(txtreaderContex.mContext.getResources(), backgroungrs,
				(int) txtreaderContex.mPaintContex.Viewwidth, (int) txtreaderContex.mPaintContex.Viewheight);
		txtreaderContex.mViewSetting.setTextColor(textcolor);
		txtreaderContex.mPaintContex.CommitSetting();
		txtreaderContex.mBitmapManager.CommitDatatoBitmap();
		txtreaderContex.mBitmapManager.initDraw();
		callBack.onBack(Type.sucess);
	}

}
