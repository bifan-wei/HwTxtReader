package hwtxtreader.bean;

import java.util.ArrayList;
import java.util.List;

public class Page {
	public int firstElementCharindex;
	public int firstElementParagraphIndex;
	public int lastElementCharindex;
	public int lastElementParagraphIndex;
	public int pageindex;
	private boolean istheFirstpage = false;
	private Boolean isTheLsatPage = false;
	private List<LineChar> linesdata;

	public Page() {
		init();
	}

	public Page(int pageindex) {
		this.pageindex = pageindex;
		init();
	}


	public int getPageindex() {
		return pageindex;
	}

	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}

	private void init() {

		linesdata = new ArrayList<>();
	}

	public boolean isIstheFirstpage() {
		return istheFirstpage;
	}

	public void setIstheFirstpage(boolean istheFirstpage) {
		this.istheFirstpage = istheFirstpage;
	}

	public Boolean getIsTheLsatPage() {
		return isTheLsatPage;
	}

	public void setIsTheLsatPage(Boolean isTheLsatPage) {
		this.isTheLsatPage = isTheLsatPage;
	}

	public void addLine(LineChar lineChar) {
		linesdata.add(lineChar);

	}

	public void clearLine() {
		linesdata.clear();
	}

	public List<LineChar> getLinesdata() {
		return linesdata;
	}

	public int getLinesSize() {
		return linesdata.size();
	}

	public Boolean HasData() {
		return getLinesSize() != 0;
	}

	public String getPageString() {
		String str = "";
		for (LineChar l : linesdata) {
			str = str + l.getLineString();
		}
		return str;
	}

	public void setLinesdata(List<LineChar> linesdata) {
		this.linesdata = linesdata;
	}

	
	
}
