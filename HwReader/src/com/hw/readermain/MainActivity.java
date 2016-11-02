package com.hw.readermain;

import com.hw.action.Action;
import com.hw.action.TxtTextSizeAddAction;
import com.hw.action.TxtTextSizeReduceAction;
import com.hw.hwtxtreader.R;
import com.hw.readermain.ReaderException.Type;
import com.hw.txtreader.TxtFileInitProcessor;
import com.hw.txtreader.TxtReaderContex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

public class MainActivity extends Activity {
	final TxtReaderContex readerContex = new TxtReaderContex();
	public ReaderView readerview;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		readerview = (ReaderView) findViewById(R.id.hwreadview);
		readerview.setReaderContex(readerContex);
		readerContex.setReaderView(readerview);
		readerContex.mContext = MainActivity.this;
		readerContex.AddAction(new TxtTextSizeAddAction(readerContex));
		readerContex.AddAction(new TxtTextSizeReduceAction(readerContex));
		new Thread(new Runnable() {
			public void run() {

				Book book = new Book();
				book.BookPath = Environment.getExternalStorageDirectory() + "/test5.txt";
				book.BookName = "测试书籍";
				readerContex.mBook = book;
				BookProcessor<Type> processor = new TxtFileInitProcessor();

				processor.Process(new CallBack<ReaderException.Type>() {

					@Override
					public void onBack(Type t) {
						Log.e("--------", ReaderException.getExceptionMsg(t));
						Log.e("--------", readerContex.mPaintContex.getPageLineNums() + "");

						Log.e("---getView_width-", readerContex.mPaintContex.getViewwidth() + "");
						Log.e("---getView_height()", readerContex.mPaintContex.getViewheight() + "");
						readerview.postInvalidate();
						
					}

				}, readerContex);

			}
		}).start();
	}

	private void doAction() {
		new Thread(new Runnable() {
			public void run() {

				try {
					Thread.sleep(5000);
					readerContex.doAction(Action.addtextsize);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		readerContex.release();
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {

		super.onResume();

	}
}
