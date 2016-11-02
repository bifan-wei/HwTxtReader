package com.hw.utils;

import java.util.List;

import com.hw.beans.CharElement;
import com.hw.beans.ILine;
import com.hw.beans.PageStyle;
import com.hw.txtreader.TxtReaderContex;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 *
 * HwReader阅读器是由黄威开发 创建时间：2016年10月29日下午7:08:10
 * 主页：http://blog.csdn.net/u014614038/
 */
public class BitmapUtil {
	private static Bitmap getTxtPageBitmapWithLinse(int pageindex, int pagenums, List<ILine<CharElement>> linesdata,
			int showviewwidth, int showviewheight, Bitmap pagebgbitmap, int linemuns, float textsizeinpx,
			float viewtoppadding, float viewleftpadding, float linepadding, Paint textpaint, Paint pageindexpaing) {

		if (linesdata == null || linesdata.size() == 0)
			return null;

		int viewwidth = (int) showviewwidth;
		int viewheigh = (int) showviewheight;
		Rect srcRect = new Rect(0, 0, viewwidth, viewheigh);// 截取bmp1中的矩形区域
		Rect dstRect = new Rect(0, 0, viewwidth, viewheigh);// bmp1在目标画布中的位置

		if (pagebgbitmap == null) {
			Log.e("pagebgbitmap==null", "0000000000");
		}
		Bitmap mThispage = pagebgbitmap.copy(Bitmap.Config.RGB_565, true);

		if (mThispage == null) {

			Log.e("pagebgbitmap==null", "mThispage");
			System.gc();
			System.gc();
			mThispage = pagebgbitmap.copy(Bitmap.Config.RGB_565, true);
		}
		Canvas nextcavan = new Canvas(mThispage);

		float y = (textsizeinpx + viewtoppadding);

		int nums = linemuns > linesdata.size() ? linesdata.size() : linemuns;

		float linesheigh = textsizeinpx + linepadding;

		for (int i = 0; i < nums; i++) {
			nextcavan.drawText(linesdata.get(i).toString(), viewleftpadding, y + i * linesheigh, textpaint);

		}

		String indexmsg = "";

		if (pagenums == -1) {// 页数为-1时说明还没有完成分页
			indexmsg = "-" + pageindex + "-";
		} else {
			indexmsg = pageindex + "/" + pagenums;
		}

		// 画出页码
		nextcavan.drawText(indexmsg, viewleftpadding, viewheigh - 10, pageindexpaing);

		nextcavan.drawBitmap(mThispage, srcRect, dstRect, textpaint);

		return mThispage;
	}

	private static Bitmap getTxtVeiticalPageBitmapWithLinse(int pageindex, int pagenums,
			List<ILine<CharElement>> linesdata, int showviewwidth, int showviewheight, Bitmap pagebgbitmap,
			int linemuns, float textsizeinpx, float viewtoppadding, float viewleftpadding, float linepadding,
			Paint textpaint, Paint pageindexpaing, float viewbottompadding, float viewrightpadding, float charpadding) {

		if (linesdata == null || linesdata.size() == 0)
			return null;

		int viewwidth = (int) showviewwidth;
		int viewheigh = (int) showviewheight;
		Rect srcRect = new Rect(0, 0, viewwidth, viewheigh);// 截取bmp1中的矩形区域
		Rect dstRect = new Rect(0, 0, viewwidth, viewheigh);// bmp1在目标画布中的位置

		Bitmap mThispage = pagebgbitmap.copy(Bitmap.Config.RGB_565, true);

		Canvas nextcavan = new Canvas(mThispage);
		float x = viewleftpadding;
		float singlex = (viewwidth - viewleftpadding - viewrightpadding) / linemuns;
		x = singlex / 2;
		float y;
		int nums = linemuns > linesdata.size() ? linesdata.size() : linemuns;

		for (int i = 0; i < nums; i++) {
			y = 0;
			String str = linesdata.get(i).toString();
			char[] chars = str.toCharArray();
			for (int a = 0; a < chars.length; a++) {
				float charheight = CharUtil.getRealShowCharHeight(chars[a], textpaint);
				y = y + charheight + charpadding;
				nextcavan.drawText(chars, a, 1, x, y + viewtoppadding, textpaint);

			}
			x = x + singlex;

		}
		nextcavan.drawBitmap(mThispage, srcRect, dstRect, textpaint);
		return mThispage;
	}

	/**
	 * @param showindex
	 *            要现在是页码
	 * @param showpagenums
	 *            要显示页总数，传入-1的话只显示页码
	 * @param readerContex
	 * @param linesdata如果没有数据，返回null
	 * @return
	 */
	public static Bitmap getTxtPageBitmap(int showindex, int showpagenums, TxtReaderContex readerContex,
			List<ILine<CharElement>> linesdata, PageStyle pageStyle) {

		int showviewwidth = (int) readerContex.mPaintContex.Viewwidth;
		int showviewheight = (int) readerContex.mPaintContex.Viewheight;
		Bitmap pagebgbitmap = readerContex.mBitmapManager.getPagebgbitmap();
		int linemuns = readerContex.mPaintContex.PageLineNums;
		float textsizeinpx = DisPlayUtil.sp2px(readerContex.mContext, readerContex.mViewSetting.TextSize);
		float viewtoppadding = readerContex.mViewSetting.Paddingtop;
		float viewleftpadding = readerContex.mViewSetting.Paddingleft;
		float viewtottompadding = readerContex.mViewSetting.Paddingbottom;
		float viewrightpadding = readerContex.mViewSetting.Paddingright;
		float linepadding = readerContex.mViewSetting.LinePadding;
		float charpadding = readerContex.mViewSetting.VerticalCharPadding;

		Paint textpaint = readerContex.mPaintContex.TextPaint;
		Paint pageindexpaing = readerContex.mPaintContex.PageIndexPaint;
		if (pageStyle == PageStyle.horizontal) {
			return getTxtPageBitmapWithLinse(showindex, showpagenums, linesdata, showviewwidth, showviewheight,
					pagebgbitmap, linemuns, textsizeinpx, viewtoppadding, viewleftpadding, linepadding, textpaint,
					pageindexpaing);
		}

		return getTxtVeiticalPageBitmapWithLinse(showindex, showpagenums, linesdata, showviewwidth, showviewheight,
				pagebgbitmap, linemuns, textsizeinpx, viewtoppadding, viewleftpadding, linepadding, textpaint,
				pageindexpaing, viewtottompadding, viewrightpadding, charpadding);

	}

	public static Bitmap CreateBitmap(int bitmapstylecolor, int bitmapwith, int bitmapheigh) {
		int[] BitmapColor = getBitmapColor(bitmapstylecolor, bitmapwith, bitmapheigh);
		Bitmap pagebitmap = Bitmap.createBitmap(BitmapColor, bitmapwith, bitmapheigh, Bitmap.Config.RGB_565);
		return pagebitmap;
	}

	public static Bitmap CreateBitmapWitThisBg(Resources res, int backgroundresource, int bitmapwith, int bitmapheigh) {
		Bitmap bgbitmap = BitmapFactory.decodeResource(res, backgroundresource);
		int with = bgbitmap.getWidth();
		int heigh = bgbitmap.getHeight();
		int[] color = new int[with * heigh];
		for (int y = 0; y < heigh; y++) {// use of x,y is legible then // the

			for (int x = 0; x < with; x++) {
				color[y * with + x] = bgbitmap.getPixel(x, y);// the shift
																// operation //
																// generates
			}
		}

		int[] colors = new int[bitmapwith * bitmapheigh];
		for (int y = 0, size = bitmapwith * bitmapheigh, border = with * heigh, index = 0; y < size; y++) {

			if (index == border) {
				index = 0;
			}
			colors[y] = color[index];
			index++;

		}

		Bitmap pagebitmap = Bitmap.createBitmap(colors, bitmapwith, bitmapheigh, Bitmap.Config.RGB_565);
		return pagebitmap;

	}

	private static int[] getBitmapColor(int color, int with, int height) {
		int[] colors = new int[with * height];
		int STRIDE = height;
		int c = color;
		for (int y = 0; y < with; y++) {// use of x,y is legible then // the //
										// use of i,j
			for (int x = 0; x < height; x++) {
				colors[y * STRIDE + x] = c;// the shift operation generates
											// the color ARGB
			}
		}
		return colors;
	}

	/**
	 * 读取一张图片的RGB值
	 * 
	 * @throws Exception
	 */
	public int[] getImagePixel(Resources res, int drawable) {

		Bitmap bi = BitmapFactory.decodeResource(res, drawable);
		int with = bi.getWidth();
		int height = bi.getHeight();
		int[] colors = new int[with * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < with; j++) {
				int pixel = bi.getPixel(i, j); // 下面三行代码将一个数字转换为RGB数字
				colors[i * with + j] = pixel;

			}
		}
		return colors;
	}
}
