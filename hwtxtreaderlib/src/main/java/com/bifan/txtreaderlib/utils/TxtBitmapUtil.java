package com.bifan.txtreaderlib.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bifan.txtreaderlib.bean.EnChar;
import com.bifan.txtreaderlib.bean.NumChar;
import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.interfaces.IPage;
import com.bifan.txtreaderlib.interfaces.ITxtLine;
import com.bifan.txtreaderlib.main.PageParam;
import com.bifan.txtreaderlib.main.PaintContext;
import com.bifan.txtreaderlib.main.TxtConfig;

import java.util.List;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class TxtBitmapUtil {
    public static final Bitmap createHorizontalPage(Bitmap bg, PaintContext paintContext, PageParam pageParam, TxtConfig txtConfig, IPage page) {
        if (page == null || !page.HasData() || bg == null || bg.isRecycled()) {
            return null;
        }
        Bitmap bitmap = bg.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmap);
        List<ITxtLine> lines = page.getLines();
        int textHeight = txtConfig.textSize;
        int lineHeight = textHeight + pageParam.LinePadding;
        int topL = (int) (pageParam.PaddingLeft + pageParam.TextPadding) + 3;
        int bottom = pageParam.PaddingTop + textHeight;
        int bomL = bottom;
        int paraMargin = pageParam.ParagraphMargin;
        float CharPadding = pageParam.TextPadding;
        Paint paint = paintContext.textPaint;
        int defaultColor = txtConfig.textColor;

        float x = topL;
        float y = bottom;

        if (!txtConfig.ShowSpecialChar) {
            paint.setColor(defaultColor);
        }
        for (ITxtLine line : lines) {
            if (line.HasData()) {
                for (TxtChar txtChar : line.getTxtChars()) {
                    if (txtConfig.ShowSpecialChar) {
                        if (txtChar instanceof NumChar || txtChar instanceof EnChar) {
                            paint.setColor(txtChar.getTextColor());
                        } else {
                            paint.setColor(defaultColor);
                        }
                    }
                    canvas.drawText(txtChar.getValueStr(), x, y, paint);
                    txtChar.Left = (int) x;
                    txtChar.Right = (int) (x + txtChar.CharWidth);
                    txtChar.Bottom = (int) y + 5;
                    txtChar.Top = txtChar.Bottom - textHeight;
                    x = txtChar.Right + CharPadding;
                }

                x = topL;
                y = y + lineHeight;

                if (line.isParagraphEndLine()) {
                    y = y + paraMargin;

                }
            }
        }

        return bitmap;
    }

    public static final Bitmap createVerticalPage(Bitmap bg, PaintContext paintContext, PageParam pageParam, TxtConfig txtConfig, IPage page) {
        if (page == null || !page.HasData() || bg == null || bg.isRecycled()) {
            return null;
        }
        Bitmap bitmap = bg.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmap);
        List<ITxtLine> lines = page.getLines();
        int textHeight = txtConfig.textSize;
        int lineWidth = (int) pageParam.LineWidth;
        int topL = (int) (pageParam.PaddingLeft + pageParam.TextPadding) + 3;
        int bottom = pageParam.PaddingTop + textHeight;
        int bomL = bottom;
        int paraMargin = pageParam.ParagraphMargin;
        float CharPadding = pageParam.TextPadding;
        Paint paint = paintContext.textPaint;
        int defaultColor = txtConfig.textColor;

        float x = topL;
        float y = bottom;

        if (!txtConfig.ShowSpecialChar) {
            paint.setColor(defaultColor);
        }
        for (ITxtLine line : lines) {
            if (line.HasData()) {
                for (TxtChar txtChar : line.getTxtChars()) {
                    if (txtConfig.ShowSpecialChar) {
                        if (txtChar instanceof NumChar || txtChar instanceof EnChar) {
                            paint.setColor(txtChar.getTextColor());
                        } else {
                            paint.setColor(defaultColor);
                        }
                    }
                    canvas.drawText(txtChar.getValueStr(), x, y, paint);
                    txtChar.Left = (int) x;
                    txtChar.Right = (int) (x + textHeight+5);
                    txtChar.Bottom =  (int) (y + 5);
                    txtChar.Top = (int) (txtChar.Bottom - txtChar.CharWidth);
                    y = y + CharPadding+textHeight;
                }
                x = x + lineWidth;
                y = bottom;
                if (line.isParagraphEndLine()) {
                    x = x + paraMargin;
                }
            }
        }

        return bitmap;
    }

    public static Bitmap createBitmap(int bitmapStyleColor, int bitmapWidth, int bitmapHeight) {
        int[] BitmapColor = getBitmapColor(bitmapStyleColor, bitmapWidth, bitmapHeight);
        return Bitmap.createBitmap(BitmapColor, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    public static Bitmap createBitmap(Resources res, int backgroundResource, int bitmapWidth, int bitmapHeight) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(res, backgroundResource);
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        int[] color = new int[width * height];
        for (int y = 0; y < height; y++) {// use of x,y is legible then // the
            for (int x = 0; x < width; x++) {
                color[y * width + x] = bgBitmap.getPixel(x, y);// the shift
            }
        }
        int[] colors = new int[bitmapWidth * bitmapHeight];
        for (int y = 0, size = bitmapWidth * bitmapHeight, border = width * height, index = 0; y < size; y++) {
            if (index == border) {
                index = 0;
            }
            colors[y] = color[index];
            index++;
        }
        return Bitmap.createBitmap(colors, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    private static int[] getBitmapColor(int color, int with, int height) {
        int[] colors = new int[with * height];
        int STRIDE = height;
        int c = color;
        for (int y = 0; y < with; y++) {// use of x,y is legible then // the //
            for (int x = 0; x < height; x++) {
                colors[y * STRIDE + x] = c;// the shift operation generates
            }
        }
        return colors;
    }


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
