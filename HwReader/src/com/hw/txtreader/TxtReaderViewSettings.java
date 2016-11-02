package com.hw.txtreader;

import com.hw.beans.ReaderStyle;
import com.hw.readermain.ReaderViewSetting;
import com.hw.txtreader.readerstyle.TxtReaderNormalStyle;

import android.graphics.Color;

/**
 * @author 黄威 2016年10月23日下午4:12:24 主页：http://blog.csdn.net/u014614038
 */
public class TxtReaderViewSettings extends ReaderViewSetting {

	/**
	 * 屏幕亮度百分比
	 */
	public float ScreenLight = 100;
	/**
	 * 页面左距离
	 */
	public float Paddingleft = 15;
	/**
	 *
	 */
	public float Paddingright = 10;
	/**
	 *
	 */
	public float Paddingtop = 10;
	/**
	 *
	 */
	public float Paddingbottom = 10;
	/**
	 * 行距
	 */
	public float LinePadding = 15;
	/**
	 * 是否有页面分界线
	 */
	public Boolean HasDiverder = true;
	/**
	 * 页面分界线宽度
	 */
	public float DeviderWidth = 5;

	/**
	 * 是否隐藏状态栏
	 */
	public Boolean Hidestatebar = false;
	/**
	 * 页面背景
	 */
	public ReaderStyle mReaderStyle = new TxtReaderNormalStyle();// 页面背景

	/**
	 * 页面字体加粗
	 */
	public Boolean MakeBoldText = false;// 字体是否需要加粗

	/**
	 * 页码字体加粗
	 */
	public Boolean MakePageIndexBoldText = false;// 字体是否需要加粗

	/**
	 * 页码字体大小
	 */
	public float PageIndextextSize = 14f;// 页码字体大小
	/**
	 * 页码字体类型
	 */
	public String PageIndextestTypeFile;// 页码字体类型、

	/**
	 * 页面字体颜色
	 */
	public int TextColor = Color.parseColor("#000000");

	/**
	 * 页码字体验收
	 */
	public int PageIndexTextColor = Color.parseColor("#000000");

	/**
	 * 
	 */
	public float VerticalCharPadding = 5;

	public float getScreenLight() {
		return ScreenLight;
	}

	public void setScreenLight(float screenLight) {
		ScreenLight = screenLight;
	}

	public float getPaddingleft() {
		return Paddingleft;
	}

	public void setPaddingleft(float paddingleft) {
		Paddingleft = paddingleft;
	}

	public float getPaddingright() {
		return Paddingright;
	}

	public void setPaddingright(float paddingright) {
		Paddingright = paddingright;
	}

	public float getPaddingtop() {
		return Paddingtop;
	}

	public void setPaddingtop(float paddingtop) {
		Paddingtop = paddingtop;
	}

	public float getPaddingbottom() {
		return Paddingbottom;
	}

	public void setPaddingbottom(float paddingbottom) {
		Paddingbottom = paddingbottom;
	}

	public float getLinePadding() {
		return LinePadding;
	}

	public void setLinePadding(float linePadding) {
		LinePadding = linePadding;
	}

	public Boolean getHasDiverder() {
		return HasDiverder;
	}

	public void setHasDiverder(Boolean hasDiverder) {
		HasDiverder = hasDiverder;
	}

	public float getDeviderWidth() {
		return DeviderWidth;
	}

	public void setDeviderWidth(float deviderWidth) {
		DeviderWidth = deviderWidth;
	}

	public Boolean getHidestatebar() {
		return Hidestatebar;
	}

	public void setHidestatebar(Boolean hidestatebar) {
		Hidestatebar = hidestatebar;
	}

	public ReaderStyle getPageBackground() {
		return mReaderStyle;
	}

	public void setPageBackground(ReaderStyle pageBackground) {
		mReaderStyle = pageBackground;
	}

	public Boolean getMakeBoldText() {
		return MakeBoldText;
	}

	public void setMakeBoldText(Boolean makeBoldText) {
		MakeBoldText = makeBoldText;
	}

	public Boolean getMakePageIndexBoldText() {
		return MakePageIndexBoldText;
	}

	public void setMakePageIndexBoldText(Boolean makePageIndexBoldText) {
		MakePageIndexBoldText = makePageIndexBoldText;
	}

	public float getPageIndextextSize() {
		return PageIndextextSize;
	}

	public void setPageIndextextSize(float pageIndextextSize) {
		PageIndextextSize = pageIndextextSize;
	}

	public String getPageIndextestTypeFile() {
		return PageIndextestTypeFile;
	}

	public void setPageIndextestTypeFile(String pageIndextestTypeFile) {
		PageIndextestTypeFile = pageIndextestTypeFile;
	}

	public int getTextColor() {
		return TextColor;
	}

	public void setTextColor(int textColor) {
		TextColor = textColor;
	}

	public int getPageIndexTextColor() {
		return PageIndexTextColor;
	}

	public void setPageIndexTextColor(int pageIndexTextColor) {
		PageIndexTextColor = pageIndexTextColor;
	}

	public float getVerticalCharPadding() {
		return VerticalCharPadding;
	}

	public void setVerticalCharPadding(float verticalCharPadding) {
		VerticalCharPadding = verticalCharPadding;
	}

}
