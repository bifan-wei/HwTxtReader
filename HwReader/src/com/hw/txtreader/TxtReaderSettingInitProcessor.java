package com.hw.txtreader;

import com.hw.data.ViewSettingsDB;
import com.hw.readermain.Book;
import com.hw.readermain.BookDataLoadProcessor;
import com.hw.readermain.CallBack;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderException.Type;
import com.hw.readermain.ReaderSettingInitProcessor;

public class TxtReaderSettingInitProcessor extends ReaderSettingInitProcessor {

	@Override
	public void Process(CallBack<Type> callBack, ReaderContex readerContex) {
		TxtReaderContex contex = (TxtReaderContex) readerContex;
		getHistorySettings(contex);
		TxtPaintContex txtPaintContex = new TxtPaintContex();
		txtPaintContex.init(contex);
		contex.mPaintContex = txtPaintContex;

		BookDataLoadProcessor processor = new TxtDataLoadProcessor();
		processor.Process(callBack, contex);

	}

	private void getHistorySettings(TxtReaderContex contex) {
		ViewSettingsDB db = new ViewSettingsDB(contex.mContext);
		db.CreateTable();
		Book book = contex.mBook;
		TxtReaderViewSettings setting = (TxtReaderViewSettings) db.getViewSettingMsg(book.BOOKHashName,
				TxtReaderViewSettings.class);
		if (setting != null) {
			contex.mViewSetting = setting;
		} else {
			contex.mViewSetting = new TxtReaderViewSettings();
		}
		db.close();
	}

}
