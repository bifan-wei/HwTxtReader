package com.bifan.txtreaderlib.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.interfaces.ITextSelectDrawer;
import com.bifan.txtreaderlib.interfaces.ITxtLine;
import com.bifan.txtreaderlib.utils.ELogger;

import java.util.List;

/**
 * Created by bifan-wei
 * on 2017/12/4.
 */

public class NormalTextSelectDrawer implements ITextSelectDrawer {
    private final Path path = new Path();

    @Override
    public void drawSelectedChar(TxtChar selectedChar, Canvas canvas, Paint paint) {
        if (selectedChar != null) {
            ELogger.log("onPressSelectText","drawSelectedChar");
            path.reset();
            path.moveTo(selectedChar.Left, selectedChar.Top);
            path.lineTo(selectedChar.Right, selectedChar.Top);
            path.lineTo(selectedChar.Right, selectedChar.Bottom);
            path.lineTo(selectedChar.Left, selectedChar.Bottom);
            path.lineTo(selectedChar.Left, selectedChar.Top);
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public void drawSelectedLines(List<ITxtLine> selectedLines, Canvas canvas, Paint paint) {

        for (ITxtLine line : selectedLines) {
             ELogger.log("onPressSelectText",line.getLineStr());
            if (line.getTxtChars() != null && line.getTxtChars().size() > 0) {

                TxtChar fistChar = line.getTxtChars().get(0);
                TxtChar lastChar = line.getTxtChars().get(line.getTxtChars().size() - 1);

                float fw = fistChar.CharWidth;
                float lw = lastChar.CharWidth;

                RectF rect = new RectF(fistChar.Left, fistChar.Top,
                        lastChar.Right, lastChar.Bottom);

                canvas.drawRoundRect(rect, fw / 2,
                        paint.getTextSize() / 2, paint);

            }
        }
    }


}
