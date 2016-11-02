package com.hw.txtreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.hw.readermain.Book;
import com.hw.readermain.BookDataLoadProcessor;
import com.hw.readermain.BookParagraphCacheCenter;
import com.hw.readermain.CallBack;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderException.Type;

import android.util.Log;

public class TxtDataLoadProcessor extends BookDataLoadProcessor {

	@Override
	public void Process(CallBack<Type> callBack, ReaderContex readerContex) {

		Book book = readerContex.mBook;

		BookParagraphCacheCenter cacheCenter = new BookParagraphCacheCenter();

		if (book == null) {
			throw new NullPointerException();
		}

		if (book.BookPath == null || book.BookCode == null) {
			throw new NullPointerException();
		}

		Boolean iscuesss = readBook(callBack, book.BookPath, book.BookCode, cacheCenter);
		if (iscuesss) {
			readerContex.mCacheCenter = cacheCenter;
			TxtPageProcessor pageProcessor = new TxtPageProcessor();
			pageProcessor.Process(callBack, readerContex);
		}

	}

	/**
	 * 返回是否成功
	 * 
	 * @param callBack
	 * @param bookurl
	 * @param bookcode
	 * @param cacheCenter
	 * @return
	 */
	private Boolean readBook(CallBack<Type> callBack, String bookurl, String bookcode,
			BookParagraphCacheCenter cacheCenter) {
		BufferedReader bufferedReader = null;
		try {
			Log.e("bookcode", bookcode+"-----------");
			bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(bookurl)), bookcode));
			String data = "";

			try {
				while ((data = bufferedReader.readLine()) != null) {
					cacheCenter.addParagraphStr(data);
				}
				return true;
			} catch (IOException e) {
				callBack.onBack(Type.loadbookioexception);
			}
		} catch (UnsupportedEncodingException e) {
			callBack.onBack(Type.bookcodeunsupport);
			Log.e("bookcode:", bookcode + "");
			return false;
		} catch (FileNotFoundException e) {
			callBack.onBack(Type.bookfilenofound);
			Log.e("bookurl:", bookurl + "");
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		return false;
	}

}
