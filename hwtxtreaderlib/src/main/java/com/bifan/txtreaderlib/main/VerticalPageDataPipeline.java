package com.bifan.txtreaderlib.main;

import android.graphics.Paint;

import com.bifan.txtreaderlib.bean.EnChar;
import com.bifan.txtreaderlib.bean.NumChar;
import com.bifan.txtreaderlib.bean.Page;
import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.bean.TxtLine;
import com.bifan.txtreaderlib.interfaces.IPage;
import com.bifan.txtreaderlib.interfaces.IPageDataPipeline;
import com.bifan.txtreaderlib.interfaces.IParagraphData;
import com.bifan.txtreaderlib.interfaces.ITxtLine;
import com.bifan.txtreaderlib.utils.FormatUtil;
import com.bifan.txtreaderlib.utils.TextBreakUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by ： bifan-wei
 */
public class VerticalPageDataPipeline implements IPageDataPipeline{
    private TxtReaderContext readerContext;

    public VerticalPageDataPipeline(TxtReaderContext readerContext) {
        this.readerContext = readerContext;
    }
    @Override
    public IPage getPageStartFromProgress(int paragraphIndex, int charIndex) {
        IParagraphData data = readerContext.getParagraphData();
        PageParam param = readerContext.getPageParam();

        if (data == null) return null;

        int PageLineNum = param.PageLineNum;
        int PageWidth = param.PageWidth;
        int LineWidth = (int) param.LineWidth;
        float lineHeight = readerContext.getPageParam().LineHeight;
        float textPadding = readerContext.getPageParam().TextPadding;

        int CurrentPaIndex = paragraphIndex;
        int startIndex = charIndex;
        int ParagraphNum = data.getParagraphNum();

        int paragraphMargin = param.ParagraphMargin;

        if (CurrentPaIndex >= ParagraphNum || CurrentPaIndex < 0) return null;//超过返回null

        IPage page = new Page();

        //获取页面数据
        while (page.getLineNum() < PageLineNum && CurrentPaIndex < ParagraphNum) {
            String paragraphStr = data.getParagraphStr(CurrentPaIndex);
            if (paragraphStr != null && paragraphStr.length() > 0) {
                List<ITxtLine> lines = getLineFromParagraphStartOnBeginning(paragraphStr, CurrentPaIndex, startIndex, lineHeight, textPadding, readerContext.getPaintContext().textPaint);
                for (ITxtLine line : lines) {
                    page.addLine(line);
                    if (page.getLineNum() >= PageLineNum) {
                        page.setFullPage(true);
                        break;
                    }
                }

                startIndex = 0;
            }
            CurrentPaIndex++;
        }

        //尝试识别是否需要加段落间距数据
        int textWidth = readerContext.getTxtConfig().textSize;
        if (paragraphMargin > 0 && LineWidth > 0 && PageWidth > LineWidth && page.getLineNum() > 0) {
            int width = param.PaddingLeft;
            List<ITxtLine> lines = new ArrayList<>();
            for (int i = 0; i < page.getLineNum(); i++) {
                ITxtLine line = page.getLine(i);
                width = width + LineWidth;
                if (width > PageWidth) {
                    page.setFullPage(true);
                    if (width - LineWidth + textWidth <= PageWidth) {
                        lines.add(line);
                    }
                    break;
                } else {
                    lines.add(line);
                }
                if (line.isParagraphEndLine()) {
                    width = width + paragraphMargin;//说明还有行数据
                }
            }
            if (width >= PageWidth) {
                page.setFullPage(true);
            }
            page.setLines(lines);
        }

        return page;
    }

    @Override
    public IPage getPageEndToProgress(int paragraphIndex, int charIndex) {

        IParagraphData data = readerContext.getParagraphData();
        PageParam param = readerContext.getPageParam();

        if(data==null) return null;

        int PageLineNum = param.PageLineNum;
        int PageWidth = param.PageWidth;
        int LineHeight = (int) param.LineHeight;
        int LineWidth = (int) param.LineWidth;
        int CurrentPaIndex = paragraphIndex;
        int startIndex = charIndex;
        int ParagraphNum = data.getParagraphNum();
        float textPadding = readerContext.getPageParam().TextPadding;

        if (charIndex == 0) {//说明上页开始是段落开始位置，段落左移
            CurrentPaIndex--;//
            startIndex = 0;
        }
        if (CurrentPaIndex >= ParagraphNum || CurrentPaIndex < 0) return null;//超过返回null

        IPage page = new Page();
        int paragraphMargin = param.ParagraphMargin;
        while (page.getLineNum() < PageLineNum && CurrentPaIndex >= 0) {
            String paragraphStr = data.getParagraphStr(CurrentPaIndex);

            if (paragraphStr != null && paragraphStr.length() > 0) {
                if (startIndex == 0) {//说明上页开始是段落开始位置
                    startIndex = paragraphStr.length();
                }
                List<ITxtLine> lines = getLineFromParagraphOnEnd(paragraphStr, CurrentPaIndex, startIndex, LineHeight, textPadding, readerContext.getPaintContext().textPaint);
                if (lines.size() > 0) {
                    for (int i = lines.size() - 1; i >= 0; i--) {
                        ITxtLine line = lines.get(i);
                        page.addLine(line);
                        if (page.getLineNum() >= PageLineNum) {
                            page.setFullPage(true);
                            break;
                        }
                    }
                }
            }
            CurrentPaIndex--;
            startIndex = 0;
        }


        if (page.HasData()) {
            Collections.reverse(page.getLines());
        }

        if (paragraphMargin > 0 && LineHeight > 0 && PageWidth > LineHeight && page.getLineNum() > 0) {
            int width = param.PaddingLeft;
            int textWidth = readerContext.getTxtConfig().textSize;
            List<ITxtLine> lines = new ArrayList<>();
            for (int i = page.getLineNum() - 1; i >= 0; i--) {
                ITxtLine line = page.getLine(i);
                if (i == page.getLineNum() - 1) {//底部那个不添加偏移量
                    lines.add(line);
                    width = width + textWidth;
                } else {
                    if (width + LineWidth > PageWidth) {
                        page.setFullPage(true);
                        if (width + textWidth <= PageWidth) {
                            lines.add(line);
                        }
                        break;
                    } else {
                        lines.add(line);
                        width = width + LineWidth;
                        if (line.isParagraphEndLine()) {
                            width = width + paragraphMargin;//说明还有行数据
                        }
                    }
                }
            }
            if (width >= PageWidth) {
                page.setFullPage(true);
            }
            page.setLines(lines);
            if (page.HasData()) {
                Collections.reverse(page.getLines());
            }
        }
        return page;
    }

    private List<ITxtLine> getLineFromParagraphOnEnd(String paragraphData, int paragraphIndex, int endCharIndex,
                                                     float lineHeight, float textPadding, Paint paint) {
        List<ITxtLine> lines = new ArrayList<>();
        int startIndex = 0;
        int paragraphLength = paragraphData.length();
        if (paragraphLength == 0 || endCharIndex <= 0) {
            return lines;
        }
        endCharIndex = endCharIndex >= paragraphData.length() ? paragraphData.length() : endCharIndex;

        if (endCharIndex > 0) {
            String s = paragraphData.substring(startIndex, endCharIndex);//截取要的数据
            while (s.length() > 0) {// is[0] 为个数 is[1] 为是否满一行
                float[] is = TextBreakUtil.BreakTextVertical(s, lineHeight, textPadding, paint);
                ITxtLine line;
                int num = (int) is[0];
                if (is[1] != 1) {//不满一行
                    line = getLineFromString(s, paragraphIndex, startIndex, is);
                    if (endCharIndex == paragraphLength && s.length() + startIndex >= paragraphLength) {
                        line.setParagraphEndLine(true);
                    }
                    lines.add(line);
                    return lines;
                }

                String lineStr = s.substring(0, num);
                line = getLineFromString(lineStr, paragraphIndex, startIndex, is);
                startIndex = startIndex + num;
                if (line != null) {
                    lines.add(line);
                }
                s = s.substring(lineStr.length());
            }
        }
        return lines;
    }


    /**
     * @param paragraphData
     * @param paragraphIndex
     * @param startCharIndex
     * @param lineHeight
     * @param textPadding
     * @param paint
     * @return
     */
    private List<ITxtLine> getLineFromParagraphStartOnBeginning(String paragraphData, int paragraphIndex, int startCharIndex,
                                                                float lineHeight, float textPadding, Paint paint) {
        List<ITxtLine> lines = new ArrayList<>();
        int startIndex = startCharIndex;
        startIndex = startIndex < 0 ? 0 : startIndex;
        int paragraphLength = paragraphData.length();

        if ( startIndex >= paragraphLength) {
            return lines;
        }
        if ( paragraphData.length() > 0) {
            String s = paragraphData.substring(startIndex);//截取要的数据
            while (s.length() > 0) {// is[0] 为个数 is[1] 为是否满一行
                float[] is = TextBreakUtil.BreakTextVertical(s, lineHeight, textPadding, paint);
                ITxtLine line;
                if (is[1] != 1) {//不满一行
                    line = getLineFromString(s, paragraphIndex, startIndex, is);
                    if (s.length() + startIndex >= paragraphLength) {
                        line.setParagraphEndLine(true);
                    }
                    lines.add(line);
                    break;
                }
                int num = (int) is[0];
                String lineStr = s.substring(0, num);
                line = getLineFromString(lineStr, paragraphIndex, startIndex, is);
                startIndex = startIndex + num;
                if (line != null) {
                    lines.add(line);
                }
                s = s.substring(num);
            }
        }
        return lines;
    }
    private ITxtLine getLineFromString(String str, int ParagraphIndex, int charIndex, float[] is) {
        ITxtLine line = null;
        int index = charIndex;
        int cIndex = 2;//找到字符宽度
        if (str != null && str.length() > 0) {
            line = new TxtLine();

            char[] cs = str.toCharArray();
            for (char c : cs) {
                TxtChar Char;
                if (FormatUtil.isDigital(c)) {
                    Char = new NumChar(c);
                } else if (FormatUtil.isLetter(c)) {
                    Char = new EnChar(c);
                } else {
                    Char = new TxtChar(c);
                }
                Char.ParagraphIndex = ParagraphIndex;
                Char.CharIndex = index++;
                Char.CharWidth = is[cIndex++];
                line.addChar(Char);
            }

        }
        return line;
    }
}
