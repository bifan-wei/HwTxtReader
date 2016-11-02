package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;
import com.hw.beans.PageStyle;
import com.hw.readermain.CavansProcessor;
import com.hw.readermain.EvenProcessor;
import com.hw.readermain.IPageSizeCalculator;
import com.hw.readermain.IReaderContex;
import com.hw.readermain.ReaderContex;
import com.hw.readermain.ReaderView;

import android.content.Context;

/**
 * @author 黄威 2016年10月19日下午4:58:03 主页：http://blog.csdn.net/u014614038
 */
public class TxtReaderContex extends ReaderContex implements IReaderContex {
	public TxtReaderViewSettings mViewSetting = null;
	public TxtPaintContex mPaintContex = null;
	public IPageSizeCalculator mPageSizeCalculator = null;
	public TxtPagePipeline mPagePipeline = null;
	public TxtPageCursor mPageCursor = null;

	public TxtPageManager mPageManager = new TxtPageManager();
	public TxtBitmapManager mBitmapManager = new TxtBitmapManager(this);
	private ReaderView mReaderView = null;
	public TxtEvenProcessor txtEvenProcessor = null;
	public TxtCavansProcessor txtCavansProcessor = null;
	public PageStyle mPageStyle = PageStyle.horizontal;
	public ITxtPageChangeListener mPageChangeListener;

	public TxtReaderContex() {

	}

	public TxtReaderContex(Context context, ReaderView readerView) {
		super(context, readerView);

	}

	@Override
	public EvenProcessor getEvenProcessor() {

		return txtEvenProcessor == null ? (txtEvenProcessor = new TxtEvenProcessor(this)) : txtEvenProcessor;
	}

	@Override
	public CavansProcessor getCavansProcessor() {

		return txtCavansProcessor == null ? (txtCavansProcessor = new TxtCavansProcessor(this)) : txtCavansProcessor;
	}

	public void release() {
		if (mCacheCenter != null) {
			mCacheCenter.clear();
		}
	}

	public ReaderView getReaderView() {
		return mReaderView;
	}

	public TxtReaderViewSettings getViewSetting() {
		return mViewSetting;
	}

	public TxtPaintContex getPaintContex() {
		return mPaintContex;
	}

	public TxtPagePipeline getPagePipeline() {
		return mPagePipeline;
	}

	public TxtPageCursor getPageCursor() {
		return mPageCursor;
	}

	public TxtPageManager getPageManager() {
		return mPageManager;
	}

	public TxtBitmapManager getBitmapManager() {
		return mBitmapManager;
	}

	public void setReaderView(ReaderView readerview) {
		mReaderView = readerview;
	}

	public void setOnTxtPageChangeListener(ITxtPageChangeListener iTxtPageChangeListener) {
		this.mPageChangeListener = iTxtPageChangeListener;
	}

	public void onPageChageListener() {
		if (this.mPageChangeListener != null) {
			IPage<CharElement> page = this.mPageManager.getCurrentPage();
			if (page != null && page.HasData()) {
				this.mPageChangeListener.onPageChange(page.getPageIndex(), page.getPageNmus());
			}
		}
	}
}
