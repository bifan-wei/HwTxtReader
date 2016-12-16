package hwtxtreader.main;

public interface ManagerToModelTransform extends Transformer {

	public void loadTxtbook(Transformer t);

	public void jumptopage(int pageindex);

	public void separatepage();

	public void refreshbitmaptext();
	
	public void refreshbitmapbackground();
}
