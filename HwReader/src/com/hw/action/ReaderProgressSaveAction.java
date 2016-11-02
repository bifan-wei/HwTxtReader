package com.hw.action;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;
import com.hw.data.BookMsgDB;
import com.hw.readermain.Book;
import com.hw.readermain.ReaderContex;
import com.hw.txtreader.TxtReaderContex;

/**
 * @author 黄威
 * 2016年11月2日上午10:42:38
 * 主页：http://blog.csdn.net/u014614038
 */
public class ReaderProgressSaveAction extends ReaderAction {

	public ReaderProgressSaveAction(ReaderContex readerContex) {
		super(readerContex);

	}

	@Override
	public void Run() {
		TxtReaderContex Contex = ((TxtReaderContex) readerContex);

		IPage<CharElement> page = Contex.getPageManager().getCurrentPage();
		if (page != null && page.HasData()) {
			Book book = Contex.mBook;
			book.PreReadCharIndex = page.getFirstElement().getIndexinparagraph();
			book.PreReadParagraphIndex = page.getFirstElement().getParagraphindex();
			BookMsgDB bookMsgDB = new BookMsgDB(readerContex.mContext);
			bookMsgDB.inserBook(book);
			bookMsgDB.close();
		}

	}

	@Override
	public Action getActionType() {

		return Action.savereaderprogress;
	}

}
