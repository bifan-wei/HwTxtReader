package hwtxtreader.utils;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import hwtxtreader.bean.LineChar;
import hwtxtreader.main.TxtManager;

public class BitmapUtil {

	public static Bitmap getPageBitmapWithLinse(int pageindex, int pagenums, List<LineChar> linesdata,
			TxtManager txtManager, Bitmap pagebitmap) {
		
		if (linesdata == null || linesdata.size() == 0 || txtManager == null || pagebitmap == null)
			return null;
		
		int viewwith = txtManager.getViewWith();
		int viewheigh = txtManager.getViewHeigh();
		Rect srcRect = new Rect(0, 0, viewwith, viewheigh);// 截取bmp1中的矩形区域
		Rect dstRect = new Rect(0, 0, viewwith, viewheigh);// bmp1在目标画布中的位置
		
		Bitmap mThispage = pagebitmap.copy(Bitmap.Config.RGB_565, true);
		
		Canvas nextcavan = new Canvas(mThispage);

		int y = txtManager.getTextsize() + txtManager.getViewConfig().getPadingtop();

		int nums = txtManager.getLinesNums() > linesdata.size() ? linesdata.size() : txtManager.getLinesNums();
		int linesheigh = txtManager.getTextsize() + txtManager.getViewConfig().getLinesPadding();
		
		for (int i = 0; i < nums; i++) {
			nextcavan.drawText(linesdata.get(i).getLineString(), txtManager.getViewConfig().getPaddingleft(),
					y + i * linesheigh, txtManager.getTextPaint());

		}
		
		
		String indexmsg = "";

		if (pagenums == -1) {// 页数为-1时说明还没有完成分页
			indexmsg = "-" + pageindex + "-";
		} else {
			indexmsg = pageindex + "/" + pagenums;
		}

		// 画出页码
		nextcavan.drawText(indexmsg, txtManager.getViewConfig().getPaddingleft(), txtManager.getViewHeigh() - 10,
				txtManager.getPageIndexTextPaint());

		nextcavan.drawBitmap(mThispage, srcRect, dstRect, txtManager.getTextPaint());
		
		return mThispage;
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
