package com.bifan.txtreaderlib.main;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bifan.txtreaderlib.bean.FileReadRecordBean;
import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.bean.TxtFileMsg;
import com.bifan.txtreaderlib.interfaces.IChapter;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.IPage;
import com.bifan.txtreaderlib.interfaces.IReaderViewDrawer;
import com.bifan.txtreaderlib.interfaces.ITextSelectListener;
import com.bifan.txtreaderlib.interfaces.ITxtTask;
import com.bifan.txtreaderlib.tasks.DrawPrepareTask;
import com.bifan.txtreaderlib.tasks.TxtConfigInitTask;
import com.bifan.txtreaderlib.tasks.TxtPageLoadTask;
import com.bifan.txtreaderlib.utils.ELogger;
import com.bifan.txtreaderlib.utils.FileHashUtil;
import com.bifan.txtreaderlib.utils.TxtBitmapUtil;

import java.io.File;
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
                //当前是第一页，只显示第一页
                if (isFirstPage()) {
                    if (getTopPage() != null) {
                        canvas.drawBitmap(getTopPage(), 0, 0, null);
                    }
                } else {
                    //绘制上一页
                    if (getTopPage() != null) {
                        drawPagePreTopPage(canvas);
                    }
                    //绘制下一页
                    if (getBottomPage() != null) {
                        drawPagePreBottomPage(canvas);
                    }
                    //绘制阴影线
                    drawPagePrePageShadow(canvas);
                }
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

        if (getMoveDistance() > 0 && isFirstPage()) {
            ELogger.log(tag, "是第一页了");
            return;
        }

        if (getMoveDistance() < 0 && isLastPage()) {
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
            textSelectListener.onTextChanging(FirstSelectedChar,LastSelectedChar);
            textSelectListener.onTextChanging(getCurrentSelectedText());
        }
    }

    @Override
    protected void onTextSelectMoveBack(MotionEvent event) {
        getDrawer().onTextSelectMoveBack(event);
        if (textSelectListener != null) {
            textSelectListener.onTextChanging(FirstSelectedChar,LastSelectedChar);
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
        TxtConfig.saveTextColor(getContext(), textColor);
        TxtConfig.saveBackgroundColor(getContext(), backgroundColor);
        readerContext.getTxtConfig().textColor = textColor;
        readerContext.getTxtConfig().backgroundColor = backgroundColor;
        if (readerContext.getBitmapData().getBgBitmap() != null) {
            readerContext.getBitmapData().getBgBitmap().recycle();
        }
        int width = readerContext.getPageParam().PageWidth;
        int height = readerContext.getPageParam().PageHeight;
        readerContext.getBitmapData().setBgBitmap(TxtBitmapUtil.createBitmap(backgroundColor, width, height));
        refreshCurrentView();
    }


    /**
     * 从指定进度加载
     *
     * @param progress 0~100
     */
    public void loadFromProgress(float progress) {
        if (readerContext == null || readerContext.getParagraphData() == null) return;
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        float p = progress / 100;
        int charNum = readerContext.getParagraphData().getCharNum();
        int charIndex = (int) (p * charNum);
        int paragraphNum = readerContext.getParagraphData().getParagraphNum();
        int paragraphIndex = readerContext.getParagraphData().findParagraphIndexByCharIndex(charIndex);

        if (progress == 100 || paragraphIndex >= paragraphNum) {
            paragraphIndex = paragraphNum - 1;
        }

        if (paragraphIndex < 0) {
            paragraphIndex = 0;
        }

        ELogger.log(tag, "loadFromProgress ,progress:" + progress + "/paragraphIndex:" + paragraphIndex + "/paragraphNum:" + paragraphNum);
        loadFromProgress(paragraphIndex, 0);
    }

    /**
     * 根据字符的准确位置跳转进度
     *
     * @param paragraphIndex
     * @param charIndex
     */
    public void loadFromProgress(final int paragraphIndex, final int charIndex) {
        refreshTag(1, 1, 1);
        TxtPageLoadTask txtPageLoadTask = new TxtPageLoadTask(paragraphIndex, charIndex);
        txtPageLoadTask.Run(new LoadListenerAdapter() {
            @Override
            public void onSuccess() {
                if (readerContext != null) {
                    checkMoveState();
                    postInvalidate();
                    post(new Runnable() {
                        @Override
                        public void run() {
                            //IPage midPage = readerContext.getPageData().MidPage();
                            //onPageProgress(midPage);
                            onProgressCallBack(getProgress(paragraphIndex, charIndex));
                            tryFetchFirstPage();
                        }
                    });
                }
            }
        }, readerContext);
    }


    private void tryFetchFirstPage() {
        IPage midPage = readerContext.getPageData().MidPage();
        IPage firstPage = null;
        onPageProgress(midPage);
        if (midPage != null && midPage.HasData()) {
            if (midPage.getFirstChar().ParagraphIndex == 0 && midPage.getFirstChar().CharIndex == 0) {
            } else {
                firstPage = readerContext.getPageDataPipeline().getPageEndToProgress(midPage.getFirstChar().ParagraphIndex, midPage.getFirstChar().CharIndex);
            }
        }
        if (firstPage != null && firstPage.HasData()) {
            if (!firstPage.isFullPage()) {
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
            ELogger.log(tag, "jumpToPreChapter false chapters == null or currentChapter == null");
            return false;
        }

        int index = currentChapter.getIndex();

        if (index == 0 || chapters.size() == 0) {
            ELogger.log(tag, "jumpToPreChapter false index == 0 or chapters.size() == 0");
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
            ELogger.log(tag, "jumpToNextChapter false chapters == null or currentChapter == null");
            return false;
        }
        int index = currentChapter.getIndex();
        if (index >= chapters.size() - 1 || chapters.size() == 0) {
            ELogger.log(tag, "jumpToNextChapter false  < chapters.size() - 1 or chapters.size() == 0");
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
        if (currentPage != null && currentPage.HasData() && readerContext.getFileMsg() != null) {
            TxtChar firstChar = currentPage.getFirstChar();
            readerContext.getFileMsg().PreParagraphIndex = firstChar.ParagraphIndex;
            readerContext.getFileMsg().PreCharIndex = firstChar.CharIndex;
            readerContext.getFileMsg().CurrentParagraphIndex = firstChar.ParagraphIndex;
            readerContext.getFileMsg().CurrentCharIndex = firstChar.CharIndex;
        }
    }


    /**
     * @param isBold 字体否加粗
     */
    public void setTextBold(boolean isBold) {
        TxtConfig.saveIsBold(getContext(), isBold);
        getTxtReaderContext().getTxtConfig().Bold = isBold;
        refreshCurrentView();
    }

    /**
     * 平移切换页面
     */
    public void setPageSwitchByTranslate() {
        TxtConfig.saveSwitchByTranslate(getContext(), true);
        getTxtReaderContext().getTxtConfig().Page_Switch_Mode = TxtConfig.PAGE_SWITCH_MODE_SERIAL;
        drawer = new SerialPageDrawer(this, readerContext, mScroller);
    }

    /**
     * 剪切切换页面
     */
    public void setPageSwitchByShear() {
        TxtConfig.saveSwitchByTranslate(getContext(), true);
        getTxtReaderContext().getTxtConfig().Page_Switch_Mode = TxtConfig.PAGE_SWITCH_MODE_SHEAR;
        drawer = new ShearPageDrawer(this, readerContext, mScroller);
    }
    /**
     * 滑盖切换页面
     */
    public void setPageSwitchByCover() {
        TxtConfig.saveSwitchByTranslate(getContext(), false);
        getTxtReaderContext().getTxtConfig().Page_Switch_Mode = TxtConfig.PAGE_SWITCH_MODE_COVER;
        drawer = new NormalPageDrawer(this, readerContext, mScroller);
    }

    /**
     * 保存当前进度到数据库，建议退出时调用
     */
    public void saveCurrentProgress() {
        TxtFileMsg fileMsg = getTxtReaderContext().getFileMsg();
        if (getTxtReaderContext().InitDone() && fileMsg != null) {
            String path = fileMsg.FilePath;
            if (path != null && new File(path).exists()) {
                //说明当前是有数据的
                IPage midPage = getTxtReaderContext().getPageData().MidPage();
                if (midPage != null && midPage.HasData()) {
                    FileReadRecordDB readRecordDB = new FileReadRecordDB(readerContext.getContext());
                    readRecordDB.createTable();
                    FileReadRecordBean r = new FileReadRecordBean();
                    r.fileName = fileMsg.FileName;
                    r.filePath = fileMsg.FilePath;
                    try {
                        r.fileHashName = FileHashUtil.getMD5Checksum(path);
                    } catch (Exception e) {
                        ELogger.log(tag, "saveCurrentProgress Exception:" + e.toString());
                        readRecordDB.closeTable();
                        return;
                    }

                    r.paragraphIndex = midPage.getFirstChar().ParagraphIndex;
                    r.chartIndex = midPage.getFirstChar().CharIndex;
                    readRecordDB.insertData(r);
                    readRecordDB.closeTable();
                } else {
                    ELogger.log(tag, "saveCurrentProgress midPage is false empty");
                }
            }

        }
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


    /**
     * @param progress 获取指定进度的章节信息
     * @return
     */
    public IChapter getChapterFromProgress(int progress) {
        List<IChapter> chapters = getChapters();
        if (chapters != null && chapters.size() > 0) {
            int paragraphNum = getTxtReaderContext().getParagraphData().getParagraphNum();
            int terminalParagraphIndex = progress * paragraphNum / 100;

            if (terminalParagraphIndex == 0) {
                return chapters.get(0);
            }
            for (IChapter chapter : chapters) {
                int startIndex = chapter.getStartParagraphIndex();
                int endIndex = chapter.getEndParagraphIndex();
                ELogger.log("getChapterFromProgress",startIndex+","+endIndex);
                if (terminalParagraphIndex >= startIndex && terminalParagraphIndex < endIndex) {
                    return chapter;
                }
            }
        }
        return null;
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
