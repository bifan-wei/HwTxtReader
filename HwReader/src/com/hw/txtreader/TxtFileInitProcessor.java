package com.hw.txtreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.hw.data.BookMsgDB;
import com.hw.readermain.Book;
import com.hw.readermain.BookFileProcessor;
import com.hw.readermain.CallBack;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderException.Type;
import com.hw.readermain.ReaderSettingInitProcessor;
import com.hw.utils.FileCharsetDetector;
import com.hw.utils.FileHashUtil;

public class TxtFileInitProcessor extends BookFileProcessor {

	@Override
	public void Process(CallBack<Type> callBack, ReaderContex readerContex) {

		Book book = readerContex.mBook;
		if (book == null) {
			throw new NullPointerException();
		}

		if (book.BookPath == null) {
			throw new NullPointerException();
		}

		if (!(new File(book.BookPath)).exists()) {
			callBack.onBack(Type.bookfilenofound);
			return;
		}

		initBookFileCode(book);
		initBookLength(book);
		initPreReadIndex(book, readerContex);

		ReaderSettingInitProcessor processor = new TxtReaderSettingInitProcessor();
		processor.Process(callBack, readerContex);
	}

	private void initPreReadIndex(Book book, ReaderContex readerContex) {

		BookMsgDB db = new BookMsgDB(readerContex.mContext);
		db.CreateTable();

		Book history = db.getBook(book.BOOKHashName);

		if (history != null) {
			book.PreReadParagraphIndex = history.PreReadParagraphIndex;
			book.PreReadCharIndex = history.PreReadCharIndex;
			book.PrePagestyle = history.PrePagestyle;
		} else {

			book.PreReadParagraphIndex = 0;
			book.PreReadCharIndex = 0;
		}
		db.close();
	}

	private void initBookLength(Book book) {
		try {
			book.BookLength = (new File(book.BookPath)).getTotalSpace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void initBookFileCode(Book book) {
		String code = "UTF-8";
		 
		try {
			
			code = (new FileCharsetDetector()).guessFileEncoding(new File(book.BookPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			book.BOOKHashName = FileHashUtil.getMD5Checksum(book.BookPath);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		book.BookCode = code;
	}

}
