package hwtxtreader.bean;

import java.util.ArrayList;
import java.util.List;

public class PageCacheImp implements PageCache {

	private List<Page> pages;

	public PageCacheImp() {
		pages = new ArrayList<>();
	}

	@Override
	public void addPage(Page p) {
		pages.add(p);
	}

	@Override
	public Page getFirestPage() {

		return getPagesize() == 0 ? null : pages.get(0);
	}

	@Override
	public Page getLastPage() {

		return getPagesize() == 0 ? null : pages.get(getPagesize() - 1);
	}

	@Override
	public Page getPage(int PageIndex) {

		if (PageIndex < 0 && PageIndex > pages.size()) {
			throw new NullPointerException("cause in  getPage(int PageIndex)");
		}
		return pages.get(PageIndex);
	}

	@Override
	public void clear() {
		pages.clear();
		System.gc();
		System.gc();
	}

	@Override
	public int getPagesize() {

		return pages.size();
	}

	@Override
	public Page searClosestePage(CharElement element) {

		if (isHasData() && element != null) {
			return getClosestPage(element);
		}
		return null;
	}

	private Page getClosestPage(CharElement element) {

		Page tagetPage = getFirestPage();
		for (Page comparapage : pages) {

			int comparaanswer = element.paragraphindex - tagetPage.getPageindex();
			int comparaanswer1 = element.paragraphindex - comparapage.getPageindex();
			if (comparaanswer > comparaanswer1) {
				tagetPage = comparapage;
			}

		}

		return tagetPage;
	}

	@Override
	public Boolean isHasData() {

		return getPagesize() != 0;
	}

}
