package com.hw.readermain;

import java.util.ArrayList;
import java.util.List;

import com.hw.action.Action;
import com.hw.action.ReaderAction;

import android.content.Context;

/**
 * 阅读器上下文基类
 * 
 * @author 黄威 2016年10月19日下午3:47:40 主页：http://blog.csdn.net/u014614038
 */
public class ReaderContex {

	public ReaderView mReaderView;
	public Context mContext;

	public Book mBook;
	public BookParagraphCacheCenter mCacheCenter;
	protected List<ReaderAction> mReaderActions = new ArrayList<ReaderAction>();

	public ReaderContex() {
	}

	public ReaderContex(Context context, ReaderView readerView) {
		if (readerView == null) {
			throw new NullPointerException("readerView cant not be null");
		}
		mReaderView = readerView;
		mContext = context;
	}

	public ReaderView getReaderView() {
		return mReaderView;

	}

	/**
	 * @param readerAction
	 */
	public void AddAction(ReaderAction readerAction) {
		if (!mReaderActions.contains(readerAction)) {
			mReaderActions.add(readerAction);
		}
	}

	/**
	 * @param actiontype
	 */
	public void doAction(Action actiontype) {

		for (ReaderAction a : mReaderActions) {
			if (a.getActionType() == actiontype) {
				a.Run();
			}
		}
	}

	public ReaderAction getAction(Action actiontype) {
		for (ReaderAction a : mReaderActions) {
			if (a.getActionType() == actiontype) {
				return a;
			}
		}
		return null;
	}

}
