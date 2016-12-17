package hwtxtreader.main;

import android.graphics.Paint;
import hwtxtreader.bean.TxtLoadListsner;
import hwtxtreader.bean.Txterror;
import hwtxtreader.bean.Page;
import hwtxtreader.bean.TxtFile;

/**这个主要是暴露给用户用来进行对txtreadview进行相应的设置的，包括字体、背景、进度跳转等等进行各种设置
 * 
 * @author huangwei
 *
 */
public interface TxtManager {
	
	public void CommitSetting();
	
	public TxtReadViewConfig getViewConfig();
	
	public int getTextsize();
	
	public Paint getTextPaint();
	
	public Paint getPageIndexTextPaint();
	
	public int getViewWith();
	
	public int getViewHeigh();
	
	public void setModeTransform(Transformer t);
	
	public int getLinesNums();
		
	public void LoadFile();
	
	public TxtFile getTxtFile();
	
	public void jumptopage(int pageindex);
	
	public void setOnTxtLoadListsner(TxtLoadListsner t);
	
	public void onTxtLoaderror(Txterror txterror);

	public void separatepage();
	
	public void refreshBitmapText();
	
	public void refreshBitmapBackground();
	
}
