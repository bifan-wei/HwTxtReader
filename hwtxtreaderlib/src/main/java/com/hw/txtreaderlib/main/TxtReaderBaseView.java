package com.hw.txtreaderlib.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.hw.txtreaderlib.bean.Slider;
import com.hw.txtreaderlib.bean.TxtChar;
import com.hw.txtreaderlib.bean.TxtLine;
import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.interfaces.IPage;
import com.hw.txtreaderlib.interfaces.IPageChangeListener;
import com.hw.txtreaderlib.interfaces.ITxtLine;
import com.hw.txtreaderlib.interfaces.ITxtTask;
import com.hw.txtreaderlib.tasks.BitmapProduceTask;
import com.hw.txtreaderlib.tasks.TxtFileLoader;
import com.hw.txtreaderlib.utils.DisPlayUtil;
import com.hw.txtreaderlib.utils.ELogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bifa-wei
 * on 2017/11/28.
 */

public abstract class TxtReaderBaseView extends View implements GestureDetector.OnGestureListener {
    private String tag = "TxtReaderBaseView";
    protected static final int SliderWidth = 30;
    protected static int PageChangeMinMoveDistance = 40;//页面切换需要的最小滑动距离
    protected TxtReaderContext readerContext;
    protected Scroller mScroller;
    private GestureDetector mGestureDetector;
    protected PointF mTouch = new PointF();//滑动坐标
    protected PointF mDown = new PointF();//点下的坐标
    protected TxtChar FirstSelectedChar = null;//第一个选中的字符
    protected TxtChar LastSelectedChar = null;//最后一个选中的字符
    protected Slider mLeftSlider = new Slider();
    protected Slider mRightSlider = new Slider();
    protected Bitmap TopPage = null;
    protected Bitmap BottomPage = null;
    protected Mode CurrentMode = Mode.Normal;

    public TxtReaderBaseView(Context context) {
        super(context);
        init();
    }

    public TxtReaderBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        readerContext = new TxtReaderContext(getContext());
        mScroller = new TxtReaderScroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), this);
        PageChangeMinMoveDistance = DisPlayUtil.dip2px(getContext(), 30);
        setClickable(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (readerContext.InitDone()) {
            drawLineText(canvas);
            if (readerContext.getTxtConfig().showNote) {
                drawNote(canvas);
            }

            if (readerContext.getTxtConfig().canPressSelect && CurrentMode != Mode.Normal) {
                drawSelectedText(canvas);
            }
        }
    }


    /**
     * @param canvas 绘制页面数据
     */
    protected abstract void drawLineText(Canvas canvas);

    /**
     * @param canvas 绘制笔记
     */
    protected abstract void drawNote(Canvas canvas);

    /**
     * @param canvas 绘制滑动选中的文字
     */
    protected abstract void drawSelectedText(Canvas canvas);


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScroller.computeScrollOffset()) {
            return true;
        }

        Boolean Deal = mGestureDetector.onTouchEvent(event);
        if (Deal) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onActionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
        }
        return true;
    }

    protected void onActionUp(MotionEvent event) {
        if (CurrentMode == Mode.Normal) {
            startPageUpAnimation(event);
        }
    }


    /**
     * @param event
     */
    protected void onActionMove(MotionEvent event) {
        if (CurrentMode == Mode.Normal) {
            onPageMove(event);
        } else {
            if (CurrentMode == Mode.SelectMoveBack) {
                float dx = event.getX() - mDown.x;
                float dy = event.getY() - mDown.y;
                float x = mRightSlider.Left + dx - 5;//为了准确计算到里面，添加5的偏移量
                float y = mRightSlider.Top + dy - 5;

                if (CanMoveBack(x, y)) {

                    TxtChar moveToChar = findCharByPosition(x, y);

                    if (moveToChar != null) {
                        LastSelectedChar = moveToChar;
                        checkSelectedText();
                        onTextSelectMoveBack(event);
                        invalidate();
                    }
                }
            } else if (CurrentMode == Mode.SelectMoveForward) {
                float dx = event.getX() - mDown.x;
                float dy = event.getY() - mDown.y;

                float x = mLeftSlider.Right + dx + 5;//为了准确计算到里面，添加5的偏移量
                float y = mLeftSlider.Top + dy - 5;

                if (CanMoveForward(x, y)) {
                    TxtChar moveToChar = findCharByPosition(x, y);
                    if (moveToChar != null) {
                        FirstSelectedChar = moveToChar;
                        checkSelectedText();
                        onTextSelectMoveForward(event);
                        invalidate();
                    }
                }
            }
        }
    }

    protected void startPageUpAnimation(MotionEvent event) {

        if (getMoveDistance() < -PageChangeMinMoveDistance || getMoveDistance() > PageChangeMinMoveDistance) {
            if (isPagePre()) {
                if (!isFirstPage()) {
                    startPagePreAnimation();
                } else {
                    releaseTouch();
                    invalidate();
                }
            } else if (isPageNext()) {
                if (!isLastPage()) {
                    startPageNextAnimation();
                } else {
                    releaseTouch();
                    invalidate();
                }
            }
        } else {//没有超出距离，自动还原
            startPageStateBackAnimation();
        }

    }

    protected synchronized Boolean isFirstPage() {
        return readerContext.getPageData().FirstPage() == null || getTopPage() == null;
    }

    protected synchronized Boolean isLastPage() {
        return readerContext.getPageData().LastPage() == null || getBottomPage() == null;
    }

    /**
     * 执行恢复到原状态动画
     */
    protected abstract void startPageStateBackAnimation();

    /**
     * 执行滑动到下一页
     */
    protected abstract void startPageNextAnimation();

    /**
     * 执行滑动到上一页
     */
    protected abstract void startPagePreAnimation();

    /**
     * @param event 向前滑动选择文字
     */
    protected abstract void onTextSelectMoveForward(MotionEvent event);

    /**
     * @param event 向后滑动选择文字
     */
    protected abstract void onTextSelectMoveBack(MotionEvent event);

    /**
     * @param event 页面移动
     */
    protected abstract void onPageMove(MotionEvent event);


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        mDown.x = motionEvent.getX();
        mDown.y = motionEvent.getY();
        mTouch.x = motionEvent.getX();
        mTouch.y = motionEvent.getY();

        if (CurrentMode == Mode.PressSelectText || CurrentMode == Mode.SelectMoveForward || CurrentMode == Mode.SelectMoveBack) {

            Path leftSliderPath = getLeftSliderPath();
            Path rightSliderPath = getRightSliderPath();

            if (leftSliderPath != null && rightSliderPath != null) {

                Boolean downOnLeftSlider = computeRegion(getLeftSliderPath()).contains((int) mDown.x, (int) mDown.y);
                Boolean downOnRightSlider = computeRegion(getRightSliderPath()).contains((int) mDown.x, (int) mDown.y);

                if (downOnLeftSlider || downOnRightSlider) {
                    if (downOnLeftSlider) {
                        CurrentMode = Mode.SelectMoveForward;
                        setLeftSlider(FirstSelectedChar);
                    } else {

                        CurrentMode = Mode.SelectMoveBack;
                        setRightSlider(LastSelectedChar);
                    }
                    return true;
                } else {
                    //没有点击到滑动条，释放
                    CurrentMode = Mode.Normal;
                    invalidate();
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (CurrentMode == Mode.Normal) {
            onPressSelectText(e);
        }
    }


    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float MaxVelocityX = 1000;
        if (CurrentMode == Mode.Normal) {//正常情况下快速滑动，执行翻页动作
            if (isPagePre() && !isFirstPage() && velocityX > MaxVelocityX) {
                startPagePreAnimation();
                return true;
            } else if (isPageNext() && !isLastPage() && velocityX < -MaxVelocityX) {
                startPageNextAnimation();
                return true;
            }
        }
        return false;
    }

    private void onPressSelectText(MotionEvent e) {
        TxtChar selectedChar = findCharByPosition(e.getX(), e.getY());
        if (selectedChar != null) {
            FirstSelectedChar = selectedChar;
            LastSelectedChar = selectedChar;
            setLeftSlider(FirstSelectedChar);
            setRightSlider(LastSelectedChar);
            CurrentMode = Mode.PressSelectText;
        } else {
            FirstSelectedChar = null;
            LastSelectedChar = null;
            CurrentMode = Mode.Normal;
        }
        invalidate();

    }

    private void setLeftSlider(TxtChar FirstSelectedChar) {
        mLeftSlider.Left = FirstSelectedChar.Left - SliderWidth * 2;
        mLeftSlider.Right = FirstSelectedChar.Left;
        mLeftSlider.Top = FirstSelectedChar.Bottom;
        mLeftSlider.Bottom = FirstSelectedChar.Bottom + SliderWidth * 2;
    }

    private void setRightSlider(TxtChar LastSelectedChar) {
        mRightSlider.Left = LastSelectedChar.Right;
        mRightSlider.Right = LastSelectedChar.Right + SliderWidth * 2;
        mRightSlider.Top = LastSelectedChar.Bottom;
        mRightSlider.Bottom = LastSelectedChar.Bottom + SliderWidth * 2;
    }


    /**
     * @return 找到长按选中的文字，找不到返回null
     */
    private TxtChar findCharByPosition(float positionX, float positionY) {

        IPage page = readerContext.getPageData().MidPage();
        int offset = readerContext.getPageParam().LinePadding / 2;

        if (page != null && page.HasData()) {
            List<ITxtLine> lines = page.getLines();
            for (ITxtLine line : lines) {
                List<TxtChar> chars = line.getTxtChars();
                if (chars != null && chars.size() > 0) {
                    for (TxtChar c : chars) {
                        if (positionY > c.Top - offset && positionY < c.Bottom + offset) {
                            if (positionX > c.Left && positionX <= c.Right) {
                                return c;
                            }
                        } else {
                            break;//说明在下一行
                        }
                    }
                }
            }
        }
        return null;
    }

    private Path mSliderPath = new Path();

    /**
     * @return 可能返回null
     */
    protected Path getLeftSliderPath() {
        if (FirstSelectedChar != null) {
            Path p = mSliderPath;
            p.reset();
            p.moveTo(FirstSelectedChar.Left, FirstSelectedChar.Bottom);
            p.lineTo(FirstSelectedChar.Left, FirstSelectedChar.Bottom + SliderWidth);
            Rect rect = new Rect(FirstSelectedChar.Left - SliderWidth * 2, FirstSelectedChar.Bottom, FirstSelectedChar.Left, FirstSelectedChar.Bottom + SliderWidth * 2);
            p.addArc(new RectF(rect), 0, 270);
            p.lineTo(FirstSelectedChar.Left, FirstSelectedChar.Bottom);
            return p;
        } else {
            return null;
        }
    }

    protected Path getRightSliderPath() {
        if (LastSelectedChar != null) {
            Path p = mSliderPath;
            p.reset();
            p.moveTo(LastSelectedChar.Right, LastSelectedChar.Bottom + SliderWidth);
            p.lineTo(LastSelectedChar.Right, LastSelectedChar.Bottom);
            p.lineTo(LastSelectedChar.Right + SliderWidth, LastSelectedChar.Bottom);
            Rect rect = new Rect(LastSelectedChar.Right, LastSelectedChar.Bottom, LastSelectedChar.Right + SliderWidth * 2, LastSelectedChar.Bottom + SliderWidth * 2);
            p.addArc(new RectF(rect), -90, 270);
            return p;
        } else {
            return null;
        }
    }

    private final List<ITxtLine> mSelectLines = new ArrayList<>();

    protected synchronized List<ITxtLine> getCurrentSelectTextLine() {
        return mSelectLines;
    }


    protected synchronized void checkSelectedText() {//检测滑动选中的文字
        Boolean Started = false;
        Boolean Ended = false;
        mSelectLines.clear();
        IPage currentPage = readerContext.getPageData().MidPage();
        if (currentPage == null || !currentPage.HasData() || FirstSelectedChar == null || LastSelectedChar == null)
            return;
        List<ITxtLine> lines = currentPage.getLines();
        // 找到选择的字符数据，转化为选择的行，然后将行选择背景画出来
        for (ITxtLine l : lines) {
            ITxtLine selectLine = new TxtLine();
            for (TxtChar c : l.getTxtChars()) {
                if (!Started) {
                    if (c.ParagraphIndex == FirstSelectedChar.ParagraphIndex && c.CharIndex == FirstSelectedChar.CharIndex) {
                        Started = true;
                        selectLine.addChar(c);
                        if (c.ParagraphIndex == LastSelectedChar.ParagraphIndex && c.CharIndex == LastSelectedChar.CharIndex) {
                            Ended = true;
                            break;
                        }
                    }
                } else {
                    if (c.ParagraphIndex == LastSelectedChar.ParagraphIndex && c.CharIndex == LastSelectedChar.CharIndex) {
                        Ended = true;
                        if (selectLine.getTxtChars() == null || !selectLine.getTxtChars().contains(c)) {
                            selectLine.addChar(c);
                        }
                        break;
                    } else {
                        selectLine.addChar(c);
                    }
                }
            }

            if (selectLine.HasData()) {
                mSelectLines.add(selectLine);
            }

            if (Started && Ended) {
                return;
            }
        }
    }

    protected String getCurrentSelectedText() {
        String text = "";
        for (ITxtLine l : mSelectLines) {
            text = text + l.getLineStr();
        }
        return text;
    }

    /**
     * @param path
     * @return 计算区域
     */

    private Region computeRegion(Path path) {
        Region region = new Region();
        RectF f = new RectF();
        path.computeBounds(f, true);
        region.setPath(path, new Region((int) f.left, (int) f.top, (int) f.right, (int) f.bottom));
        return region;
    }

    private boolean CanMoveBack(float TouchX, float TouchY) {
        if (FirstSelectedChar != null) {
            Path p = new Path();
            p.moveTo(FirstSelectedChar.Right, FirstSelectedChar.Top);
            p.lineTo(getWidth(), FirstSelectedChar.Top);
            p.lineTo(getWidth(), getHeight());
            p.lineTo(0, getHeight());
            p.lineTo(0, FirstSelectedChar.Bottom);
            p.lineTo(FirstSelectedChar.Right, FirstSelectedChar.Bottom);
            p.lineTo(FirstSelectedChar.Right, FirstSelectedChar.Top);
            return computeRegion(p).contains((int) TouchX, (int) TouchY);
        } else {
            return false;
        }
    }

    private boolean CanMoveForward(float TouchX, float TouchY) {
        if (LastSelectedChar != null) {
            Path p = new Path();
            p.moveTo(LastSelectedChar.Left, LastSelectedChar.Top);
            p.lineTo(getWidth(), LastSelectedChar.Top);
            p.lineTo(getWidth(), 0);
            p.lineTo(0, 0);
            p.lineTo(0, LastSelectedChar.Bottom);
            p.lineTo(LastSelectedChar.Left, LastSelectedChar.Bottom);
            p.lineTo(LastSelectedChar.Left, LastSelectedChar.Top);
            return computeRegion(p).contains((int) TouchX, (int) TouchY);
        } else {
            return false;
        }
    }


    public void loadTxtFile(final String filePath, final ILoadListener listener) {
        post(new Runnable() {
            @Override
            public void run() {
                initReaderContext();
                loadFile(filePath, listener);
            }

        });
    }

    private void initReaderContext() {
        PageChangeMinMoveDistance = getWidth() / 5;
        PageParam param = new PageParam();
        param.PageWidth = getWidth();
        param.PageHeight = getHeight();

        readerContext.setPageParam(param);
    }

    private void loadFile(final String filePath, final ILoadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TxtFileLoader loader = new TxtFileLoader();
                loader.load(filePath, readerContext, new ILoadListener() {
                    @Override
                    public void onSuccess() {

                        checkMoveState();
                        postInvalidate();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                onPageProgress(readerContext.getPageData().MidPage());
                                listener.onSuccess();
                            }
                        });


                    }

                    @Override
                    public void onFail(TxtMsg txtMsg) {
                        listener.onFail(txtMsg);
                    }

                    @Override
                    public void onMessage(String message) {
                        listener.onMessage(message);
                    }
                });
            }
        }).start();
    }

    protected Boolean isPageNext() {
        //是否是执行下一页
        return getMoveDistance() < 0;
    }

    protected Boolean isPagePre() {
        //是否是执行上一页
        return getMoveDistance() > 0;
    }

    protected synchronized float getMoveDistance() {
        return mTouch.x - mDown.x;
    }

    protected void tryDoPagePre() {
        BottomPage = readerContext.getBitmapData().FirstPage();
    }

    protected void tryDoPageNext() {
        BottomPage = readerContext.getBitmapData().LastPage();
    }

    protected void checkMoveState() {
        //检测页面滑动状态更新显示数据
        if (isPagePre()) {
            tryDoPagePre();
        } else if (isPageNext()) {
            tryDoPageNext();
        } else {
            TopPage = readerContext.getBitmapData().MidPage();
            tryDoPageNext();
        }
    }

    protected void doPagePreDone() {
        //执行获取上一页数据
        IPage firstPage = readerContext.getPageData().FirstPage();
        if (firstPage == null) {//没有上一页数据了
            ELogger.log(tag, "没有上一页数据了");
            return;
        }
        pagePreTask.Run(null, readerContext);
    }

    protected void doPageNextDone() {
        //执行获取下一页数据
        IPage lastPage = readerContext.getPageData().LastPage();
        if (lastPage == null) {//没有下一页数据了
            ELogger.log(tag, "没有下一页数据了");
            return;
        }

        pageNextTask.Run(null, readerContext);
    }

    protected Bitmap getTopPage() {
        return TopPage;
    }

    protected Bitmap getBottomPage() {
        return BottomPage;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (readerContext != null) {
            readerContext.Clear();
        }
    }

    protected void releaseTouch() {
        mTouch.x = 0;
        mDown.x = 0;
    }

    public enum Mode {
        Normal, //正常模式
        PagePreIng,//执行上一页数据获取等相关操作
        PageNextIng,//执行下一页数据获取等相关操作
        PressSelectText,//长按选中文字
        SelectMoveForward, //向前滑动选中文字
        SelectMoveBack//向后滑动选中文字
    }

    protected final ITxtTask pageNextTask = new PageNextTask();
    protected final ITxtTask pagePreTask = new PagePreTask();
    protected final BitmapProduceTask bitmapProduceTask = new BitmapProduceTask();

    private class PageNextTask implements ITxtTask {
        @Override
        public void Run(ILoadListener callBack, final TxtReaderContext readerContext) {
            CurrentMode = Mode.PagePreIng;
            getPageNextData();
            bitmapProduceTask.Run(new ILoadListener() {
                @Override
                public void onSuccess() {
                    releaseTouch();
                    checkMoveState();

                    post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                            CurrentMode = Mode.Normal;
                            onPageProgress(readerContext.getPageData().MidPage());
                        }
                    });

                }

                @Override
                public void onFail(TxtMsg txtMsg) {
                    CurrentMode = Mode.Normal;
                    ELogger.log(tag + "PageNextTask", "PageNextTask onFail" + txtMsg);
                }

                @Override
                public void onMessage(String message) {
                    CurrentMode = Mode.Normal;
                    ELogger.log(tag + "PageNextTask", "PageNextTask onMessage" + message);
                }
            }, readerContext);
        }

        private void getPageNextData() {
            IPage nextMidPage = readerContext.getPageData().LastPage();
            IPage nextFirstPage = readerContext.getPageData().MidPage();

            IPage firstPage = null;
            IPage midPage = null;
            IPage nextPage = null;

            int lineNum = readerContext.getPageParam().PageLineNum;

            if (nextFirstPage != null && nextFirstPage.HasData()) {

                if (nextFirstPage.getLineNum() == lineNum) { //说明,nextFirstPage是完整的页数据，直接获取
                    firstPage = nextFirstPage;
                } else {
                    //说明MidPage完整的页数据，nextPage已经没有数据了firstPage也应该为null
                    firstPage = null;
                }

            }

            if (nextMidPage != null && nextMidPage.getLineNum() == lineNum) {//说明之前的LastPage是完整的数据，直接获取
                midPage = nextMidPage;
            } else {
                midPage = nextMidPage;//说明之前的LastPage不是完整的数据，也直接获取
            }

            if (midPage != null && midPage.getLineNum() == lineNum) {
                //midPage是完整页，说明可能有下一页数据，否则没有下一页数据了
                nextPage = readerContext.getPageDataPipeline().getPageStartFromProgress(midPage.getLastChar().ParagraphIndex, midPage.getLastChar().CharIndex + 1);
            }


            if (firstPage != null && nextFirstPage != null) {
                if (firstPage.HasData() && nextFirstPage.HasData()) {

                    TxtChar midF = firstPage.getFirstChar();
                    TxtChar midL = firstPage.getLastChar();

                    TxtChar nextF = nextFirstPage.getFirstChar();
                    TxtChar nextL = nextFirstPage.getLastChar();

                    int needRefresh = 1;
                    if (midF.equals(nextF) && midL.equals(nextL)) {
                        needRefresh = 0;
                        readerContext.getBitmapData().setFirstPage(readerContext.getBitmapData().MidPage());
                    }
                    readerContext.getPageData().refreshTag[0] = needRefresh;
                }
            }

            if (midPage != null && nextMidPage != null) {
                if (midPage.HasData() && nextMidPage.HasData()) {
                    TxtChar midF = midPage.getFirstChar();
                    TxtChar midL = midPage.getLastChar();

                    TxtChar nextF = midPage.getFirstChar();
                    TxtChar nextL = midPage.getLastChar();

                    int needRefresh = 1;
                    if (midF.equals(nextF) && midL.equals(nextL)) {
                        needRefresh = 0;
                        readerContext.getBitmapData().setMidPage(readerContext.getBitmapData().LastPage());
                    }
                    readerContext.getPageData().refreshTag[1] = needRefresh;
                }
            }
            readerContext.getBitmapData().setLastPage(null);
            readerContext.getPageData().refreshTag[2] = 1;
            readerContext.getPageData().setFirstPage(firstPage);
            readerContext.getPageData().setMidPage(midPage);
            readerContext.getPageData().setLastPage(nextPage);

        }
    }


    private class PagePreTask implements ITxtTask {
        @Override
        public void Run(ILoadListener callBack, final TxtReaderContext readerContext) {
            CurrentMode = Mode.PageNextIng;
            getPagePreData();
            bitmapProduceTask.Run(new ILoadListener() {
                @Override
                public void onSuccess() {
                    releaseTouch();
                    checkMoveState();

                    post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                            CurrentMode = Mode.Normal;
                            onPageProgress(readerContext.getPageData().MidPage());
                        }
                    });
                }

                @Override
                public void onFail(TxtMsg txtMsg) {
                    CurrentMode = Mode.Normal;
                    ELogger.log(tag + "PagePreTask", "PageNextTask onFail" + txtMsg);
                }

                @Override
                public void onMessage(String message) {
                    CurrentMode = Mode.Normal;
                    ELogger.log(tag + "PagePreTask", "PageNextTask onMessage" + message);
                }
            }, readerContext);
        }

        private void getPagePreData() {
            IPage nextMayMidPage = readerContext.getPageData().FirstPage();
            IPage nextMayLastPage = readerContext.getPageData().MidPage();

            IPage firstPage = null;
            IPage midPage = null;
            IPage nextPage = null;

            int lineNum = readerContext.getPageParam().PageLineNum;

            if (nextMayMidPage != null && nextMayMidPage.HasData()) {
                if (nextMayMidPage.getLineNum() == lineNum) {//之前的FirstPage是满页的，直接获取
                    midPage = nextMayMidPage;
                } else {//之前的FirstPage不是是满页的，直接从头开始获取
                    midPage = readerContext.getPageDataPipeline().getPageStartFromProgress(0, 0);
                }
            }

            if (midPage != null && midPage.getLineNum() == lineNum) {//现在说明midPage是满页的，可能有上一页数据与下一页数据
                if (midPage.getFirstChar().ParagraphIndex == 0 && midPage.getFirstChar().CharIndex == 0) {
                    firstPage = null;//之前的FirstPage不是是满页的，直接从头开始获取，没有firstPage
                } else {
                    firstPage = readerContext.getPageDataPipeline().getPageEndToProgress(midPage.getFirstChar().ParagraphIndex, midPage.getFirstChar().CharIndex);
                }
                nextPage = readerContext.getPageDataPipeline().getPageStartFromProgress(midPage.getLastChar().ParagraphIndex, midPage.getLastChar().CharIndex + 1);
            }


            if (nextPage != null && nextPage.HasData() && nextMayLastPage != null && nextMayLastPage.HasData()) {

                TxtChar midF = nextPage.getFirstChar();
                TxtChar midL = nextPage.getLastChar();

                TxtChar nextF = nextMayLastPage.getFirstChar();
                TxtChar nextL = nextMayLastPage.getLastChar();

                int needRefresh = 1;
                if (midF.equals(nextF) && midL.equals(nextL)) {
                    needRefresh = 0;
                    readerContext.getBitmapData().setLastPage(readerContext.getBitmapData().MidPage());
                }
                readerContext.getPageData().refreshTag[2] = needRefresh;
            }

            if (midPage != null && midPage.HasData() && nextMayMidPage != null && nextMayMidPage.HasData()) {
                TxtChar midF = midPage.getFirstChar();
                TxtChar midL = midPage.getLastChar();
                TxtChar nextF = nextMayMidPage.getFirstChar();
                TxtChar nextL = nextMayMidPage.getLastChar();

                int needRefresh = 1;
                if (midF.equals(nextF) && midL.equals(nextL)) {
                    needRefresh = 0;
                    readerContext.getBitmapData().setMidPage(readerContext.getBitmapData().FirstPage());
                }
                readerContext.getPageData().refreshTag[1] = needRefresh;
            }

            readerContext.getBitmapData().setFirstPage(null);
            readerContext.getPageData().refreshTag[0] = 1;
            readerContext.getPageData().setFirstPage(firstPage);
            readerContext.getPageData().setMidPage(midPage);
            readerContext.getPageData().setLastPage(nextPage);
        }
    }

    protected void onPageProgress(IPage page) {
        if (pageChangeListener != null) {
            float progress = 0;
            if (page != null && page.HasData()) {
                TxtChar lastChar = page.getLastChar();

                int index = readerContext.getParagraphData().getParaStartCharIndex(lastChar.ParagraphIndex) + lastChar.CharIndex;
                int num = readerContext.getParagraphData().getCharNum();
                if (num > 0) {
                    if (index > num) {
                        progress = 1;
                    } else {
                        progress = (float) index / (float) num;
                    }
                }
                pageChangeListener.onCurrentPage(progress);
            } else {
                ELogger.log(tag, "onPageProgress !page.HasData()");
            }

        }
    }


    private class TxtReaderScroller extends Scroller {
        public TxtReaderScroller(Context context) {
            super(context);
        }

        @Override
        public void abortAnimation() {
            super.abortAnimation();
            releaseTouch();
        }
    }

    private IPageChangeListener pageChangeListener;

    public void setPageChangeListener(IPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    protected void refreshTag(int r1, int r2, int r3) {
        readerContext.getPageData().refreshTag[0] = r1;
        readerContext.getPageData().refreshTag[1] = r2;
        readerContext.getPageData().refreshTag[2] = r3;
    }

}
