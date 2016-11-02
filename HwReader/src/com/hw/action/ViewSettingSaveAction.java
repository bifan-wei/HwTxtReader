package com.hw.action;

import com.hw.data.ViewSettingsDB;
import com.hw.readermain.Book;
import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威
 * 2016年11月2日上午9:30:03
 * 主页：http://blog.csdn.net/u014614038
 */
public class ViewSettingSaveAction extends ReaderAction {

	public ViewSettingSaveAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		ViewSettingsDB viewSettingsDB = new ViewSettingsDB(readerContex.mContext);
		viewSettingsDB.getWritableDatabase();
		Book book = readerContex.mBook;
		viewSettingsDB.inserSetting(book.BookName, book.BOOKHashName, ((TxtReaderContex) readerContex).mViewSetting);
		viewSettingsDB.close();

	}

	@Override
	public Action getActionType() {

		return Action.saveviewsettings;
	}

}
