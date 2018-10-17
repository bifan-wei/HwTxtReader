package com.bifan.txtreaderlib.utils;


import android.graphics.Paint;

public class TextBreakUtil {

    /**
     * @param cs
     * @param measureWidth
     * @param textPadding
     * @param paint
     * @return is[0] 为个数 is[1] 为是否满一行,其余为字符宽度
     */
    private static float[] BreakText(char[] cs, float measureWidth, float textPadding, Paint paint) {
        float width = 0;
        int index = 2;
        float[] is = new float[index + cs.length];

        for (int i = 0, size = cs.length; i < size; i++) {
            String measureStr = String.valueOf(cs[i]);
            float charWidth = paint.measureText(measureStr);
            if ((width + textPadding + charWidth) >= measureWidth) {
                is[0] = i;
                is[1] = 1;
                return is;
            } else {
                is[index++] = charWidth;
            }

            width += charWidth + textPadding;
        }
        is[0] = cs.length;
        is[1] = 0;
        return is;
    }

    /**
     * @param cs
     * @param measureHeight
     * @param textPadding
     * @param paint
     * @return
     */
    private static float[] BreakTextVertical(char[] cs, float measureHeight, float textPadding, Paint paint) {
        float height = 0;
        int index = 2;
        float[] is = new float[index + cs.length];
        for (int i = 0, size = cs.length; i < size; i++) {
            float charHeight =  paint.getTextSize();
            if ((height + textPadding + charHeight) >= measureHeight) {
                is[0] = i;
                is[1] = 1;
                return is;
            } else {
                is[index++] = charHeight;
            }

            height += charHeight + textPadding;
        }
        is[0] = cs.length;
        is[1] = 0;
        return is;
    }


    /**
     * @param text
     * @param measureHeight
     * @param textPadding
     * @param paint
     * @return
     */
    public static float[] BreakTextVertical(String text, float measureHeight, float textPadding, Paint paint) {
        if (text == null || text.length() == 0) {
            float[] is = new float[2];
            is[0] = 0;
            is[1] = 0;
            return is;
        }
        return BreakTextVertical(text.toCharArray(), measureHeight, textPadding, paint);

    }

    /**
     * @param text
     * @param measureWidth
     * @param textPadding
     * @param paint
     * @return
     */
    public static float[] BreakText(String text, float measureWidth, float textPadding, Paint paint) {
        if (text == null || text.length() == 0) {
            float[] is = new float[2];
            is[0] = 0;
            is[1] = 0;
            return is;
        }
        return BreakText(text.toCharArray(), measureWidth, textPadding, paint);

    }

    /**
     * @param text
     * @param textPadding
     * @param paint
     * @return
     */
    public static float measureText(String text, float textPadding, Paint paint) {
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
