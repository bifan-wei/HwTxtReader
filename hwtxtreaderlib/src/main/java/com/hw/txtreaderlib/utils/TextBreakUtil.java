package com.hw.txtreaderlib.utils;


import android.graphics.Paint;

public class TextBreakUtil {

    /**
     * @param cs
     * @param measureWidth
     * @param textPadding
     * @param paint
     * @return is[0] 为个数 is[1] 为是否满一行,其余为字符宽度
     */
    public static float[] BreakText(char[] cs, float measureWidth, float textPadding, Paint paint) {
        float width = 0;
        int index = 2;
        float[] is = new float[index+cs.length];

        for (int i = 0, size = cs.length; i < size; i++) {
            String measureStr = String.valueOf(cs[i]);
            float charWidth = paint.measureText(measureStr);
            if (width <= measureWidth && (width + textPadding + charWidth) > measureWidth) {
                is[0] = i;
                is[1] = 1;
                return is;
            }else{
                is[index++] = charWidth;
            }

            width += charWidth + textPadding;
        }
        is[0] = cs.length;
        return is;
    }

    public static float[] BreakText(String text, float measureWidth, float textPadding, Paint paint) {
        if (text == null || text.length() == 0) {
            float[] is = new float[2];
            is[0] = 0;
            is[1] = 0;
            return is;
        }
        return BreakText(text.toCharArray(), measureWidth, textPadding, paint);

    }

    public static float MeasureText(String text, float textPadding, Paint paint) {
        if (text == null || text.length() == 0) return 0;
        char[] cs = text.toCharArray();
        float width = 0;
        for (int i = 0, size = cs.length; i < size; i++) {
            String measureStr = String.valueOf(cs[i]);
            float charWidth = paint.measureText(measureStr);
            width += charWidth + textPadding;
        }
        return width;
    }
}
