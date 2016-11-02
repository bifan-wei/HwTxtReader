package com.hw.txtreader;

import com.hw.beans.CharElement;
import com.hw.beans.IPage;
import com.hw.readermain.IPageCursor;

public class TxtPageCursor implements IPageCursor {
	private TxtReaderContex readerContex;

	public TxtPageCursor(TxtReaderContex readerContex) {
		this.readerContex = readerContex;
		if (readerContex == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public IPage<CharElement> MoveTofirst() {

		return moveToIndex(0, 0);
	}

	/**
	 * 没有数据可能返回null
	 */
	@Override
	public IPage<CharElement> MoveToLast() {
		int lastpindex = readerContex.mCacheCenter.getParagraphSize() - 1;
		lastpindex = lastpindex > 0 ? lastpindex : 0;
		String lsatpstr = readerContex.mCacheCenter.getParagraphString(lastpindex);
		int lastcharindex = 0;
		if (lsatpstr != null) {
			lastcharindex = lsatpstr.length();
		}
		IPage<CharElement> CurrentPage = readerContex.mPagePipeline.getPageFromEnd(lastpindex, lastcharindex);
		IPage<CharElement> prePage = null;
		IPage<CharElement> nextPage = null;

		if (CurrentPage != null && CurrentPage.HasData()) {
			prePage = readerContex.mPagePipeline.getPageFromEnd(CurrentPage.getFirstElement().paragraphindex,
					CurrentPage.getFirstElement().indexinparagraph);
		}

		readerContex.mPageManager.setPrePage(prePage);
		readerContex.mPageManager.setCurrentPage(CurrentPage);
		readerContex.mPageManager.setNexPage(nextPage);
		saveReadIndex();
		return CurrentPage;
	}

	/**
	 * 如果没有数据了可能返回null
	 */
	@Override
	public IPage<CharElement> Next() {
		IPage<CharElement> currentPage = readerContex.mPageManager.getCurrentPage();
		IPage<CharElement> prePage = readerContex.mPageManager.getPrePage();
		IPage<CharElement> nextPage = readerContex.mPageManager.getNexPage();

		prePage = currentPage;
		currentPage = nextPage;
		nextPage = null;

		if (currentPage != null && currentPage.HasData()) {
			nextPage = readerContex.mPagePipeline.getPageFromStart(currentPage.getLastElement().paragraphindex,
					currentPage.getLastElement().indexinparagraph + 1);
		}

		if (nextPage != null && nextPage.HasData()) {
			currentPage = readerContex.mPagePipeline.getPageFromEnd(nextPage.getFirstElement().paragraphindex,
					nextPage.getFirstElement().indexinparagraph);
		}

		if (currentPage != null && currentPage.HasData()) {
			prePage = readerContex.mPagePipeline.getPageFromEnd(currentPage.getFirstElement().paragraphindex,
					currentPage.getFirstElement().indexinparagraph);
		}

		readerContex.mPageManager.setPrePage(prePage);
		readerContex.mPageManager.setCurrentPage(currentPage);
		readerContex.mPageManager.setNexPage(nextPage);
		saveReadIndex();
		return currentPage;
	}

	/**
	 * 如果没有数据了可能返回null
	 */
	@Override
	public IPage<CharElement> Pre() {

		IPage<CharElement> currentPage = readerContex.mPageManager.getCurrentPage();
		IPage<CharElement> prePage = readerContex.mPageManager.getPrePage();
		IPage<CharElement> nextPage = readerContex.mPageManager.getNexPage();

		currentPage = prePage;

		nextPage = null;
		prePage = null;

		if (currentPage != null && currentPage.HasData()) {
			prePage = readerContex.mPagePipeline.getPageFromEnd(currentPage.getFirstElement().paragraphindex,
					currentPage.getFirstElement().indexinparagraph);
		}

		if (prePage != null && prePage.HasData()) {
			currentPage = readerContex.mPagePipeline.getPageFromStart(prePage.getLastElement().paragraphindex,
					prePage.getLastElement().indexinparagraph + 1);
		}

		if (currentPage != null && currentPage.HasData()) {
			nextPage = readerContex.mPagePipeline.getPageFromStart(currentPage.getLastElement().paragraphindex,
					currentPage.getLastElement().indexinparagraph + 1);
		}

		readerContex.mPageManager.setPrePage(prePage);
		readerContex.mPageManager.setCurrentPage(currentPage);
		readerContex.mPageManager.setNexPage(nextPage);
		saveReadIndex();
		return currentPage;
	}

	@Override
	public Boolean isLast() {

		Boolean currentisnull = readerContex.mPageManager.getCurrentPage() == null
				|| !readerContex.mPageManager.getCurrentPage().HasData();
		Boolean nextisnull = readerContex.mPageManager.getNexPage() == null
				|| !readerContex.mPageManager.getNexPage().HasData();
		if (HasData()) {
			return !currentisnull && nextisnull;
		} else {

			return true;
		}

	}

	@Override
	public Boolean isFirst() {
		Boolean preisnull = readerContex.mPageManager.getPrePage() == null
				|| !readerContex.mPageManager.getPrePage().HasData();
		Boolean currentisnull = readerContex.mPageManager.getCurrentPage() == null
				|| !readerContex.mPageManager.getCurrentPage().HasData();

		if (HasData()) {
			return preisnull && !currentisnull;
		} else {
			return true;
		}
	}

	/**
	 * 这个方法没有用的
	 */
	@Override
	public void addElement(IPage<CharElement> t) {

	}

	@Override
	public Boolean HasData() {
		Boolean preisnull = readerContex.mPageManager.getPrePage() == null
				|| !readerContex.mPageManager.getPrePage().HasData();
		Boolean currentisnull = readerContex.mPageManager.getCurrentPage() == null
				|| !readerContex.mPageManager.getCurrentPage().HasData();
		Boolean nextisnull = readerContex.mPageManager.getNexPage() == null
				|| !readerContex.mPageManager.getNexPage().HasData();
		return !(preisnull && currentisnull && nextisnull);
	}

	/**
	 * @param pindex
	 * @param cindex
	 */
	public IPage<CharElement> moveToIndex(int pindex, int cindex) {
		IPage<CharElement> CurrentPage = readerContex.mPagePipeline.getPageFromStart(pindex, cindex);
		IPage<CharElement> prePage = null;
		IPage<CharElement> nextPage = null;

		if (CurrentPage != null && CurrentPage.HasData()) {
			nextPage = readerContex.mPagePipeline.getPageFromStart(CurrentPage.getLastElement().paragraphindex,
					CurrentPage.getLastElement().indexinparagraph + 1);
		}
		if (CurrentPage != null && CurrentPage.HasData()) {
			prePage = readerContex.mPagePipeline.getPageFromEnd(CurrentPage.getFirstElement().paragraphindex,
					CurrentPage.getFirstElement().indexinparagraph);
		}

		readerContex.mPageManager.setPrePage(prePage);
		readerContex.mPageManager.setCurrentPage(CurrentPage);
		readerContex.mPageManager.setNexPage(nextPage);
		saveReadIndex();
		return CurrentPage;
	}

	private void saveReadIndex() {
		if (readerContex.mPageManager.getCurrentPage() != null
				&& readerContex.mPageManager.getCurrentPage().HasData()) {
			readerContex.mBook.PreReadParagraphIndex = readerContex.mPageManager.getCurrentPage().getFirstElement()
					.getParagraphindex();
			readerContex.mBook.PreReadCharIndex = (int) readerContex.mPageManager.getCurrentPage().getFirstElement()
					.getIndexinparagraph();
		}

	}

	public void movePageIndex(int PageIndex) {
		IPage<CharElement> currentpage = readerContex.getPageManager().getCurrentPage();
		int pagenums = currentpage.getPageNmus();
		long charnums = readerContex.mCacheCenter.getAllCharNums();

		long movetonums = 0;
		if (pagenums > 0) {
			movetonums = charnums * PageIndex / pagenums;
		}
		int paragraphindex = readerContex.mCacheCenter.getParagraphIndexByCharNums(movetonums);
		moveToIndex(paragraphindex, 0);

	}

}
