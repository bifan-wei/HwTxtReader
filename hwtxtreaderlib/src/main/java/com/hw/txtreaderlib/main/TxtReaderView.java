package com.hw.txtreaderlib.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hw.txtreaderlib.bean.TxtChar;
import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.interfaces.IChapter;
import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.IPage;
import com.hw.txtreaderlib.interfaces.IReaderViewDrawer;
import com.hw.txtreaderlib.interfaces.ITextSelectListener;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.tasks.DrawPrepareTask;
import com.hw.txtreaderlib.tasks.TxtConfigInitTask;
import com.hw.txtreaderlib.tasks.TxtPageLoadTask;
import com.hw.txtreaderlib.utils.ELogger;
import com.hw.txtreaderlib.utils.TxtBitmapUtil;

import java.util.List;

/**
 * Created by bifa-wei
 * on 2017/11/28.
 */

public class TxtReaderView extends TxtReaderBaseView {
    private String tag = "TxtReaderView";

    public TxtReaderView(Context context) {
        super(context);
    }

    public TxtReaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private IReaderViewDrawer drawer = null;


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawLineText(Canvas canvas) {
        if (isPagePre() || isPageNext()) {
            if (isPagePre()) {
                if (getTopPage() != null) {
                    drawPagePreTopPage(canvas);
                }
                if (getBottomPage() != null) {
                    drawPagePreBottomPage(canvas);
                }
                drawPagePrePageShadow(canvas);
            } else {

                if (getTopPage() != null) {
                    drawPageNextTopPage(canvas);
                }
                if (getBottomPage() != null) {
                    drawPageNextBottomPage(canvas);
                }
                drawPageNextPageShadow(canvas);

            }

        } else {
            //说明没有触碰移动屏幕
            if (getTopPage() != null) {
                canvas.drawBitmap(getTopPage(), 0, 0, null);
            }
        }
    }

    private void drawPageNextPageShadow(Canvas canvas) {
        getDrawer().drawPageNextPageShadow(canvas);

    }

    private void drawPageNextBottomPage(Canvas canvas) {
        //绘制执行下一页时的下面页部分
        getDrawer().drawPageNextBottomPage(canvas);


    }

    private void drawPageNextTopPage(Canvas canvas) {
        //绘制执行下一页时的上面页部分
        getDrawer().drawPageNextTopPage(canvas);

    }

    private void drawPagePrePageShadow(Canvas canvas) {
        //绘制执行上一页的页边阴影
        getDrawer().drawPagePrePageShadow(canvas);

    }

    private void drawPagePreBottomPage(Canvas canvas) {
        //绘制执行上一页时的下面页部分
        getDrawer().drawPagePreBottomPage(canvas);

    }

    private void drawPagePreTopPage(Canvas canvas) {
        //绘制执行上一页时的上面页部分
        getDrawer().drawPagePreTopPage(canvas);
    }

    @Override
    protected void startPageStateBackAnimation() {
        getDrawer().startPageStateBackAnimation();
    }

    @Override
    protected void startPageNextAnimation() {
        getDrawer().startPageNextAnimation();
    }

    @Override
    protected void startPagePreAnimation() {
        getDrawer().startPagePreAnimation();
    }


    @Override
    protected void onPageMove(MotionEvent event) {
        mTouch.x = event.getX();
        mTouch.y = event.getY();

        checkMoveState();

        if (isPagePre() && isFirstPage()) {
            ELogger.log(tag, "是第一页了");
            return;
        }

        if (isPageNext() && isLastPage()) {
            ELogger.log(tag, "是最后一页了");
            return;
        }

        invalidate();
    }

    protected void onActionUp(MotionEvent event) {
        super.onActionUp(event);
        if (CurrentMode == Mode.SelectMoveBack) {
            if (textSelectListener != null) {
                textSelectListener.onTextSelected(getCurrentSelectedText());
            }
        } else if (CurrentMode == Mode.SelectMoveForward) {
            if (textSelectListener != null) {
                textSelectListener.onTextSelected(getCurrentSelectedText());
            }
        }

    }

    @Override
    protected void drawNote(Canvas canvas) {
        getDrawer().drawNote(canvas);
    }

    @Override
    protected void drawSelectedText(Canvas canvas) {
        getDrawer().drawSelectedText(canvas);
    }

    @Override
    protected void onTextSelectMoveForward(MotionEvent event) {
        getDrawer().onTextSelectMoveForward(event);
        if (textSelectListener != null) {
            textSelectListener.onTextChanging(getCurrentSelectedText());
        }
    }

    @Override
    protected void onTextSelectMoveBack(MotionEvent event) {
        getDrawer().onTextSelectMoveBack(event);
        if (textSelectListener != null) {
            textSelectListener.onTextChanging(getCurrentSelectedText());
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        getDrawer().computeScroll();
    }


    private IReaderViewDrawer getDrawer() {
        if (drawer == null) {
            //  drawer = new SerialPageDrawer(this, readerContext, mScroller);
            drawer = new NormalPageDrawer(this, readerContext, mScroller);
        }
        return drawer;
    }

    private ITextSelectListener textSelectListener;

    public void setOnTextSelectListener(ITextSelectListener textSelectListener) {
        this.textSelectListener = textSelectListener;
    }

    public TxtReaderContext getTxtReaderContext() {
        return readerContext;
    }

    private void Reload() {
        saveProgress();
        readerContext.getPageData().refreshTag[0] = 1;
        readerContext.getPageData().refreshTag[1] = 1;
        readerContext.getPageData().refreshTag[2] = 1;
        TxtConfigInitTask configInitTask = new TxtConfigInitTask();
        configInitTask.Run(actionLoadListener, readerContext);
    }

    /**
     * 设置字体大小
     *
     * @param textSize min 25 max 70 in px
     */
    public void setTextSize(int textSize) {
        readerContext.getTxtConfig().saveTextSize(getContext(), textSize);
        Reload();
    }


    /**
     * 获取字体大小 in px
     *
     * @return
     */
    public int getTextSize() {
        return readerContext.getTxtConfig().getTextSize(getContext());
    }

    /**
     * 获取背景颜色
     *
     * @return
     */
    public int getBackgroundColor() {
        return readerContext.getTxtConfig().getBackgroundColor(getContext());
    }

    /**
     * 设置样式
     *
     * @param backgroundColor
     * @param textColor
     */
    public void setStyle(int backgroundColor, int textColor) {
        saveProgress();
        readerContext.getTxtConfig().saveTextColor(getContext(), textColor);
        readerContext.getTxtConfig().saveBackgroundColor(getContext(), backgroundColor);
        readerContext.getTxtConfig().textColor = textColor;
        readerContext.getTxtConfig().backgroundColor = backgroundColor;

        if (readerContext.getBitmapData().getBgBitmap() != null) {
            readerContext.getBitmapData().getBgBitmap().recycle();
        }
        int width = readerContext.getPageParam().PageWidth;
        int height = readerContext.getPageParam().PageHeight;
        readerContext.getBitmapData().setBgBitmap(TxtBitmapUtil.CreateBitmap(backgroundColor, width, height));
        refreshCurrentView();
    }


    /**
     * 从指定进度加载
     *
     * @param progress 0~100
     */
    public void loadFromProgress(float progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        float p = progress / 100;
        int charNum = readerContext.getParagraphData().getCharNum();
        int charIndex = (int) (p * charNum);
        int paragraphIndex = readerContext.getParagraphData().findParagraphIndexByCharIndex(charIndex);
        if (progress == 100) {
            paragraphIndex = readerContext.getParagraphData().getParagraphNum() - 1;
        }
        if (paragraphIndex < 0) {
            paragraphIndex = 0;
        }
        loadFromProgress(paragraphIndex, 0);
    }

    public void loadFromProgress(int paragraphIndex, int charIndex) {
        refreshTag(1, 1, 1);
        TxtPageLoadTask txtPageLoadTask = new TxtPageLoadTask(paragraphIndex, charIndex);
        txtPageLoadTask.Run(new LoadListenerAdapter() {
            @Override
            public void onSuccess() {
                checkMoveState();
                postInvalidate();
                post(new Runnable() {
                    @Override
                    public void run() {
                        IPage midPage = readerContext.getPageData().MidPage();
                        onPageProgress(midPage);
                        tryFetchFirstPage();
                    }
                });

            }
        }, readerContext);
    }

    private void tryFetchFirstPage() {
        IPage midPage = readerContext.getPageData().MidPage();
        IPage firstPage = null;
        int lineNum = readerContext.getPageParam().PageLineNum;
        onPageProgress(midPage);
        if (midPage != null && midPage.HasData()) {
            if (midPage.getFirstChar().ParagraphIndex == 0 && midPage.getFirstChar().CharIndex == 0) {
            } else {
                firstPage = readerContext.getPageDataPipeline().getPageEndToProgress(midPage.getFirstChar().ParagraphIndex, midPage.getFirstChar().CharIndex);
            }
        }
        if (firstPage != null && firstPage.HasData()) {
            if (firstPage.getLineNum() < lineNum) {
                //说明是开始数据，重新刷新界面
                refreshTag(1, 1, 1);
                loadFromProgress(0, 0);
            } else {
                refreshTag(1, 0, 0);
                readerContext.getPageData().setFirstPage(firstPage);
                ITxtTask task = new DrawPrepareTask();
                task.Run(actionLoadListener, readerContext);//获取上一页数据
            }
        }
    }

    /**
     * 获取当前页章节
     *
     * @return 没有章节返回null, 当前页没有数据返回null
     */
    public IChapter getCurrentChapter() {
        List<IChapter> chapters = readerContext.getChapters();
        IPage midPage = readerContext.getPageData().MidPage();
        if (chapters == null || chapters.size() == 0 || midPage == null || !midPage.HasData()) {
            return null;
        }
        IChapter lastChapter = readerContext.getChapters().get(readerContext.getChapters().size() - 1);
        int currentP = midPage.getFirstChar().ParagraphIndex;
        int lastP = lastChapter.getStartParagraphIndex();
        int pre = 0;
        int next = 0;
        int index = 1;

        for (int i = 0; i < chapters.size(); i++) {
            int startP = chapters.get(i).getStartParagraphIndex();
            if (i == 0) {
                pre = startP;
            } else {
                next = startP;
                if (currentP >= pre && currentP < next) {
                    index = i;
                    break;
                } else {
                    pre = next;
                }
            }
        }
        if (currentP >= lastP) {
            return lastChapter;
        } else {
            return chapters.get(index - 1);
        }
    }

    /**
     * 跳转到上一章
     *
     * @return 是否成功
     */
    public Boolean jumpToPreChapter() {
        IChapter currentChapter = getCurrentChapter();
        List<IChapter> chapters = getChapters();
        if (chapters == null || currentChapter == null) {
            ELogger.log(tag, "chapters == null || currentChapter == null");
            return false;
        }

        int index = currentChapter.getIndex();

        if (index == 0 || chapters.size() == 0) {
            ELogger.log(tag, "index == 0 || chapters.size() == 0");
            return false;
        }
        refreshTag(1, 1, 1);

        IChapter preChapter = chapters.get(index - 1);
        loadFromProgress(preChapter.getStartParagraphIndex(), 0);
        return true;
    }

    /**
     * 跳转到下一章
     *
     * @return 是否成功
     */
    public Boolean jumpToNextChapter() {
        IChapter currentChapter = getCurrentChapter();
        List<IChapter> chapters = getChapters();
        if (chapters == null || currentChapter == null) {
            ELogger.log(tag, "chapters == null || currentChapter == null");
            return false;
        }
        int index = currentChapter.getIndex();
        if (index >= chapters.size() - 1 || chapters.size() == 0) {
            ELogger.log(tag, "index < chapters.size() - 1 || chapters.size() == 0");
            return false;
        }

        refreshTag(1, 1, 1);

        IChapter nextChapter = chapters.get(index + 1);
        loadFromProgress(nextChapter.getStartParagraphIndex(), 0);
        return true;
    }

    /**
     * 保存当前进度
     */
    public void saveProgress() {
        IPage currentPage = readerContext.getPageData().MidPage();
        if (currentPage != null && currentPage.HasData()) {
            TxtChar firstChar = currentPage.getFirstChar();
            readerContext.getFileMsg().PreParagraphIndex = firstChar.ParagraphIndex;
            readerContext.getFileMsg().PreCharIndex = firstChar.CharIndex;
            readerContext.getFileMsg().CurrentParagraphIndex = firstChar.ParagraphIndex;
            readerContext.getFileMsg().CurrentCharIndex = firstChar.CharIndex;
        }
    }


    public void setTextBold(boolean isBold) {
        getTxtReaderContext().getTxtConfig().saveIsBold(getContext(), isBold);
        getTxtReaderContext().getTxtConfig().Bold = isBold;
        refreshCurrentView();
    }

    public void setPageSwitchByTranslate() {
        getTxtReaderContext().getTxtConfig().saveSwitchByTranslate(getContext(), true);
        getTxtReaderContext().getTxtConfig().SwitchByTranslate = true;
        drawer = new SerialPageDrawer(this, readerContext, mScroller);
    }

    public void setPageSwitchByCover() {
        getTxtReaderContext().getTxtConfig().saveSwitchByTranslate(getContext(), false);
        getTxtReaderContext().getTxtConfig().SwitchByTranslate = false;
        drawer = new NormalPageDrawer(this, readerContext, mScroller);
    }

    private void refreshCurrentView() {
        refreshTag(1, 1, 1);
        ITxtTask task = new DrawPrepareTask();
        task.Run(actionLoadListener, readerContext);
    }

    /**
     * @return 获取章节数据
     */
    public List<IChapter> getChapters() {
        return readerContext.getChapters();
    }

    private ILoadListener actionLoadListener = new LoadListenerAdapter() {
        @Override
        public void onSuccess() {
            checkMoveState();
            postInvalidate();
            post(new Runnable() {
                @Override
                public void run() {
                    onPageProgress(readerContext.getPageData().MidPage());
                }
            });

        }
    };
}
