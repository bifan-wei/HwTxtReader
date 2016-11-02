package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;
import com.hw.utils.BitmapUtil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log; 

/**
 * @author 黄威 2016年10月25日下午8:44:29 主页：http://blog.csdn.net/u014614038
 */
public class TxtBitmapManager {
	private Bitmap pagebgbitmap;
	private Bitmap prepagebitmap;
	private Bitmap midpagebitmap;
	private Bitmap nexpagebitmap;

	public Bitmap predrawbitmap;
	public Bitmap nexdrawbitmap;
	public TxtReaderContex readerContex;

	public TxtBitmapManager(TxtReaderContex readerContex) {
		this.readerContex = readerContex;
	}

	public void ChangePageBitmapColor(Resources resources, int colorresource, int bitmapwith, int bitmapheigh) {
		pagebgbitmap = BitmapUtil.CreateBitmapWitThisBg(resources, colorresource, bitmapwith, bitmapheigh);
	}

	public void CommitDatatoBitmap() {
		prepagebitmap = null;
		midpagebitmap = null;
		nexpagebitmap = null;
		IPage<CharElement> prepage = readerContex.mPageManager.getPrePage();
		IPage<CharElement> currentpage = readerContex.mPageManager.getCurrentPage();
		IPage<CharElement> nextpage = readerContex.mPageManager.getNexPage();

		int showpagenums = countPagenums();

		if (prepage != null && prepage.HasData()) {
			prepage.setPageIndex(getPageindex(prepage));
			prepage.setPageNmus(showpagenums);
			prepagebitmap = BitmapUtil.getTxtPageBitmap(prepage.getPageIndex(), showpagenums, readerContex,
					prepage.getLines(),readerContex.mPageStyle);
		}

		if (currentpage != null && currentpage.HasData()) {
			currentpage.setPageIndex(getPageindex(currentpage));
			currentpage.setPageNmus(showpagenums);
			midpagebitmap = BitmapUtil.getTxtPageBitmap(currentpage.getPageIndex(), showpagenums, readerContex,
					currentpage.getLines(),readerContex.mPageStyle);
		}

		if (nextpage != null && nextpage.HasData()) {
			nextpage.setPageIndex(getPageindex(nextpage));
			nextpage.setPageNmus(showpagenums);
			nexpagebitmap = BitmapUtil.getTxtPageBitmap(currentpage.getPageIndex(), showpagenums, readerContex,
					nextpage.getLines(),readerContex.mPageStyle);
		}

	}

	public void initDraw() {
		predrawbitmap = midpagebitmap;
	}

	public void doPre() {
		nexdrawbitmap = prepagebitmap;
	}

	public void doNext() {
		nexdrawbitmap = nexpagebitmap;
	}

	public Boolean isFirstPage() {
		return prepagebitmap == null;
	}

	public Boolean isLastPage() {
		return nexpagebitmap == null;
	}

	private int countPagenums() {
		return readerContex.mPagePipeline.getPageSize();
	}

	private int getPageindex(IPage<CharElement> page) {
		CharElement lastelement = page.getLastElement();
		int charindex = lastelement.indexinparagraph;
		int paragraphindex = lastelement.paragraphindex;
		paragraphindex = (paragraphindex - 1) >= 0 ? paragraphindex - 1 : 0;

		long charnums = readerContex.mCacheCenter.getChartNumsByParagraphindex(paragraphindex) + charindex;
		int index = readerContex.mPagePipeline.getCurrentPageIndex(charnums);
		return index;

	}

	public Bitmap getPagebgbitmap() {
		if(pagebgbitmap==null){
			Log.e("pagebgbitmap==null", "88888888888");
		}
		return pagebgbitmap;
	}

	public void setPagebgbitmap(Bitmap pagebgbitmap) {
		this.pagebgbitmap = pagebgbitmap;
	}

	public void recycle() {
		if (pagebgbitmap != null) {
			pagebgbitmap.recycle();

		}
		if (prepagebitmap != null) {
			pagebgbitmap.recycle();

		}
		if (midpagebitmap != null) {
			pagebgbitmap.recycle();

		}
		if (nexpagebitmap != null) {
			pagebgbitmap.recycle();

		}
		if (predrawbitmap != null) {
			pagebgbitmap.recycle();

		}
		if (nexdrawbitmap != null) {
			pagebgbitmap.recycle();

		}
	}

}
