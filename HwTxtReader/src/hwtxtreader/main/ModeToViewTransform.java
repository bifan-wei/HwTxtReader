package hwtxtreader.main;

public interface ModeToViewTransform extends Transformer {
	public void ReFreshView();

	public void onloadfirstpage(Boolean islastpage);

	public void onloadnextpage(Boolean islastpage);

	public void onloadprepage(Boolean isfirstpage);

	public void onloadpagefromindex(Boolean isfirstpage, Boolean islastpage);

	public void onloadFileException();

	public void onNoData();

	public void onPageSeparateStart();

	public void onPageSeparateDone();

}
