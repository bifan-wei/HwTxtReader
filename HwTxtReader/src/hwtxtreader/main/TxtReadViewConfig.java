package hwtxtreader.main;

import com.example.hwtxtreader.R;

import android.graphics.Color;

/**
 * @author huangwei
 * 2016下午4:55:11
 * 主页：http://blog.csdn.net/u014614038
 * 阅读器相关设置信息类
 */  
public class TxtReadViewConfig { 
  
	private int DEFAULT_TEXTCOLOR = Color.BLACK;
	private int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	private String TextSort = null;// 字体类型
	private Boolean FakeBoldText = false;// 字体是否需要加粗
	private int TextSize = 30; // 字体大小     
	private int TextColor = DEFAULT_TEXTCOLOR;// 字体颜色
	private int BackBroundColor = R.drawable.reading__reading_themes_vine_white;// 背景
	private int LinesPadding = 10;// 行间距
	private int Padingtop = 10;// 距离顶部距离
	private int paddingleft = 10;// 距离左边的距离  
	private int paddingright = 5;// 距离右边的距离
	private int Padingbottom = 15;// 距离底部的距离
	private int PAGE_DIVIDE_PADDING = 0;// 两页之间的距离	
    private int pageindextextcolor = Color.parseColor("#AAAAAA");    
    private int pageindextextsize = 25;
    private boolean hidestatebar = false;// 是否隐藏状态栏
    
	public int getPageindextextcolor() {
		return pageindextextcolor;
	}

	public void setPageindextextcolor(int pageindextextcolor) {
		this.pageindextextcolor = pageindextextcolor;
	}

	public int getPageindextextsize() {
		return pageindextextsize;
	}

	public void setPageindextextsize(int pageindextextsize) {
		this.pageindextextsize = pageindextextsize;
	}

	public int getTextSize() {

		return TextSize;
	}

	public void setTextSize(int textSize) {
		TextSize = textSize;
	}

	public int getTextColor() {
		return TextColor;
	}

	public void setTextColor(int textColor) {
		TextColor = textColor;
	}

	public int getBackBroundColor() {
		return BackBroundColor;
	}

	public void setBackBroundColor(int backBroundColor) {
		BackBroundColor = backBroundColor;
	}

	public int getLinesPadding() {
		return LinesPadding;
	}

	public void setLinesPadding(int linesPadding) {
		LinesPadding = linesPadding;
	}

	public int getPadingtop() {
		return Padingtop;
	}

	public void setPadingtop(int padingtop) {
		Padingtop = padingtop;
	}

	public int getPadingbottom() {
		return Padingbottom;
	}

	public void setPadingbottom(int padingbottom) {
		Padingbottom = padingbottom;
	}

	public int getPaddingleft() {
		return paddingleft;
	}

	public void setPaddingleft(int paddingleft) {
		this.paddingleft = paddingleft;
	}

	public int getPaddingright() {
		return paddingright;
	}

	public void setPaddingright(int paddingright) {
		this.paddingright = paddingright;
	}

	public int getPAGE_DIVIDE_PADDING() {
		return PAGE_DIVIDE_PADDING;
	}

	public void setPAGE_DIVIDE_PADDING(int pAGE_DIVIDE_PADDING) {
		PAGE_DIVIDE_PADDING = pAGE_DIVIDE_PADDING;
	}

	public String getTextSort() {
		return TextSort;
	}

	public void setTextSort(String textSort) {
		TextSort = textSort;
	}

	public Boolean getFakeBoldText() {
		return FakeBoldText;
	}

	public void setFakeBoldText(Boolean fakeBoldText) {
		FakeBoldText = fakeBoldText;
	}

	public boolean isHidestatebar() {
		return hidestatebar;
	}

	public void setHidestatebar(boolean hidestatebar) {
		this.hidestatebar = hidestatebar;
	}

}
