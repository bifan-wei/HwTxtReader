package com.bifan.txtreaderlib.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.bifan.txtreaderlib.bean.DefaultLeftSlider;
import com.bifan.txtreaderlib.bean.DefaultRightSlider;
import com.bifan.txtreaderlib.bean.Slider;
import com.bifan.txtreaderlib.bean.TxtChar;
import com.bifan.txtreaderlib.bean.TxtLine;
import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.ICenterAreaClickListener;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.interfaces.IPage;
import com.bifan.txtreaderlib.interfaces.IPageChangeListener;
import com.bifan.txtreaderlib.interfaces.IPageEdgeListener;
import com.bifan.txtreaderlib.interfaces.ISliderListener;
import com.bifan.txtreaderlib.interfaces.ITxtLine;
import com.bifan.txtreaderlib.interfaces.ITxtTask;
import com.bifan.txtreaderlib.tasks.BitmapProduceTask;
import com.bifan.txtreaderlib.tasks.TextLoader;
import com.bifan.txtreaderlib.tasks.TxtFileLoader;
import com.bifan.txtreaderlib.utils.DisPlayUtil;
import com.bifan.txtreaderlib.utils.ELogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bifa-wei
 * on 2017/11/28.
 */

public abstract class TxtReaderBaseView extends View implements GestureDetector.OnGestureListener {
    private String tag = "TxtReaderBaseView";

    protected static int SliderWidth = 40;//滑动条宽度
    protected static int PageChangeMinMoveDistance = 40;//页面切换需要的最小滑动距离
    protected TxtReaderContext readerContext;//阅读器上下文
    protected Scroller mScroller;//滑动器
    protected GestureDetector mGestureDetector;//手势检测器
    protected PointF mTouch = new PointF();//滑动坐标
    protected PointF mDown = new PointF();//点下的坐标
    protected TxtChar FirstSelectedChar = null;//第一个选中的字符
    protected TxtChar LastSelectedChar = null;//最后一个选中的字符
    protected Slider mLeftSlider = null;//左侧滑动条
    protected Slider mRightSlider = null;//右侧滑动条
    protected Bitmap TopPage = null;//顶部页
    protected Bitmap BottomPage = null;//底部页
    protected Mode CurrentMode = Mode.Normal;//当前页面模式
    protected boolean hasDown = false;

    public TxtReaderBaseView(Context context) {
        super(context);
        init();
    }

    public TxtReaderBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        if (mLeftSlider == null) {
            mLeftSlider = new DefaultLeftSlider();
        }
        if (mRightSlider == null) {
            mRightSlider = new DefaultRightSlider();
        }
        SliderWidth = DisPlayUtil.dip2px(getContext(), 13);
        mLeftSlider.SliderWidth = SliderWidth;
        mRightSlider.SliderWidth = SliderWidth;
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
        //初始化完成才允许绘制
        if (readerContext.InitDone()) {
            //绘制页面行数据
            drawLineText(canvas);
            //是否显示笔记  该功能未完成
            if (readerContext.getTxtConfig().showNote) {
                //绘制笔记
                drawNote(canvas);
            }
            //不是正常模式，可能需要绘制长按选择的文字
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
        //检查是否在滑动中
        if (mScroller.computeScrollOffset()
                || CurrentMode == Mode.PageNextIng
                || CurrentMode == Mode.PagePreIng) {
            if (hasDown) {
                hasDown = false;
            }
            return true;
        }

        //手势处理完成，捕捉了的话，拦截它
        Boolean Deal = mGestureDetector.onTouchEvent(event);
        //判断是否已经被处理了
        if (Deal) {
            return true;
        }

        if (!hasDown) {
            //如果在滑动中，拦截了ACTION_DOWN事件，在滑动结束
            //事件不会被Deal，出现没有初始化的情况，可能出现翻下一页却是在翻上一页的情况
            return true;
        }
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

    /**
     * @param event onActionUp
     */
    protected void onActionUp(MotionEvent event) {
        //默认正常模式下才能响应up事件
        if (CurrentMode == Mode.Normal) {
            startPageUpAnimation(event);
        }
    }


    /**
     * @param event onActionMove
     */
    protected void onActionMove(MotionEvent event) {
        if (CurrentMode == Mode.Normal) {
            //正常模式，执行页面滑动
            onPageMove(event);
        } else {
            //当前需要执行向后滑动选择文字
            if (CurrentMode == Mode.SelectMoveBack) {
                float dx = event.getX() - mDown.x;
                float dy = event.getY() - mDown.y;
                float x = mRightSlider.getX(dx);
                float y = mRightSlider.getY(dy);

                if (CanMoveBack(x, y)) {
                    TxtChar moveToChar = findCharByPositionWhileMove(x, y);
                    if (FirstSelectedChar != null && moveToChar != null) {
                        if (moveToChar.Top > FirstSelectedChar.Top
                                || (moveToChar.Top == FirstSelectedChar.Top
                                && moveToChar.Left >= FirstSelectedChar.Left)) {
                            LastSelectedChar = moveToChar;
                            checkSelectedText();
                            onTextSelectMoveBack(event);
                            invalidate();
                        }
                    }
                }

            } else if (CurrentMode == Mode.SelectMoveForward) { //当前需要执行向前滑动选择文字
                float dx = event.getX() - mDown.x;
                float dy = event.getY() - mDown.y;

                float x = mLeftSlider.getX(dx);
                float y = mLeftSlider.getY(dy);

                if (CanMoveForward(x, y)) {
                    TxtChar moveToChar = findCharByPositionWhileMove(x, y);
                    if (LastSelectedChar != null && moveToChar != null) {
                        if (moveToChar.Bottom < LastSelectedChar.Bottom
                                || (moveToChar.Bottom == LastSelectedChar.Bottom
                                && moveToChar.Right <= LastSelectedChar.Right)) {
                            FirstSelectedChar = moveToChar;
                            checkSelectedText();
                            onTextSelectMoveForward(event);
                            invalidate();
                        }
                    }
                }
            } else if (CurrentMode == Mode.PressUnSelectText) {

            }
        }
    }

    /**
     * 执行上一页动画
     *
     * @param event
     */
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
        } else {
            //没有超出距离，自动还原
            if ((getMoveDistance() > 0 && isFirstPage()) || (getMoveDistance() < 0 && isLastPage())) {
                //这种情况不执行
            } else {
                //如果只是移动一点点，释放即可，不需要恢复
                if ((getMoveDistance() > 0 && getMoveDistance() < 5) || (getMoveDistance() <= 0 && getMoveDistance() > -5)) {
                    releaseTouch();
                    invalidate();
                } else {//ss
                    startPageStateBackAnimation();
                }
            }

        }

    }


    /**
     * @return 当前页是否是第一页
     */
    protected synchronized Boolean isFirstPage() {
        return readerContext.getPageData().FirstPage() == null || getTopPage() == null;
    }

    /**
     * @return 当前页是否是最后一页
     */
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
        hasDown = true;

        if (CurrentMode == Mode.PressSelectText
                || CurrentMode == Mode.SelectMoveForward
                || CurrentMode == Mode.SelectMoveBack) {
            CurrentMode = Mode.PressSelectText;
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
                }
            }
        } else {
            if (CurrentMode == Mode.PagePreIng || CurrentMode == Mode.PageNextIng) {//执行动画的使用不允许刷新状态
            } else {
                CurrentMode = Mode.Normal;
                invalidate();
            }
            return true;
        }
        return true;
    }


    /**
     * 释放了滑动条
     */
    protected void onReleasedSlider() {
        //已经释放了滑动选择
        if (sliderListener != null) {
            sliderListener.onReleaseSlider();
        }
    }

    /**
     * 显示了滑动条
     */
    protected void onShownSlider() {
        //开始显示滑动选择
        if (sliderListener != null) {
            sliderListener.onShowSlider(FirstSelectedChar);
            sliderListener.onShowSlider(FirstSelectedChar.getValueStr());
        }
    }


    /**
     * 轻击翻页
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //当前是长按滑动事件的话，没有点击到滑动条，应该释放
        if (CurrentMode == Mode.PressSelectText
                || CurrentMode == Mode.SelectMoveForward
                || CurrentMode == Mode.SelectMoveBack) {
            //没有点击到滑动条，释放
            CurrentMode = Mode.Normal;
            onReleasedSlider();
            invalidate();
            return true;
        }

        Boolean dealCenterClickAndDonChangePage = dealCenterClickAndDoChangePage(e);
        if (dealCenterClickAndDonChangePage) {
            return true;
        }
        return false;
    }

    /**
     * 处理中间区域点击事件并且可能执行点击翻页效果
     */
    private boolean dealCenterClickAndDoChangePage(MotionEvent e) {

        if (CurrentMode == Mode.Normal && readerContext.InitDone()) {
            float widthPercent = readerContext.getTxtConfig().CenterClickArea;
            //捕捉异常情况
            widthPercent = widthPercent < 0 ? 0 : widthPercent;
            widthPercent = widthPercent > 1 ? 1 : widthPercent;

            int width = (int) (getWidth() * widthPercent);
            int left = getWidth() / 2 - width / 2;
            int top = getHeight() / 2 - width;
            int bottom = top + width + width;
            int right = left + width;

            int x = (int) e.getX();
            int y = (int) e.getY();

            boolean needPagePre = x < left;
            boolean needPageNext = x > right;
            boolean inCenter = x > left && x < right && y > top && y < bottom;
            boolean deal = false;

            if (inCenter) {
                //点击中间区域
                if (centerAreaClickListener != null) {
                    deal = centerAreaClickListener.onCenterClick(widthPercent);
                }
            } else {
                if (centerAreaClickListener != null) {
                    deal = centerAreaClickListener.onOutSideCenterClick(widthPercent);
                }
            }
            //getMoveDistance() < -PageChangeMinMoveDistance || getMoveDistance() > PageChangeMinMoveDistance
            // if ((getMoveDistance() > 0 && isFirstPage()) || (getMoveDistance() < 0 && isLastPage())) {} 这样的情况才不执行

            //如果这个事件没有被处理，将可能会执行翻页事件
            if (!deal) {
                if (needPagePre && !isFirstPage()) {
                    // mTouch.x - mDown.x>10
                    //模拟滑动执行翻上一页手势
                    mDown.x = 0;
                    mTouch.x = mDown.x + 15;
                    tryDoPagePre();
                    startPagePreAnimation();
                    return true;
                }

                if (needPageNext && !isLastPage()) {
                    //mTouch.x - mDown.x<-10
                    ///模拟滑动执行翻下一页手势
                    mDown.x = getWidth();
                    mTouch.x = mDown.x - 15;
                    tryDoPageNext();
                    startPageNextAnimation();
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 长按事件，执行检测长按文字
     */
    @Override
    public void onLongPress(MotionEvent e) {
        if (CurrentMode == Mode.Normal) {
            onPressSelectText(e);
        }
    }

    /**
     * @param motionEvent motionEvent
     */
    @Override
    public void onShowPress(MotionEvent motionEvent) {
        ELogger.log(tag, "onShowPress ,CurrentMode:" + CurrentMode);
    }

    /**
     * @param motionEvent  motionEvent
     * @param motionEvent1 motionEvent1
     * @param v            v
     * @param v1           v1
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * 快速滑动翻页
     *
     * @param e1        e1
     * @param e2        e2
     * @param velocityX velocityX
     * @param velocityY velocityY
     * @return
     */
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

    /**
     * 长按获取按着的文字，这时 FirstSelectedChar =  LastSelectedChar
     */
    protected void onPressSelectText(MotionEvent e) {
        TxtChar selectedChar = findCharByPosition(e.getX(), e.getY());
        if(selectedChar!=null)
        ELogger.log("onPressSelectText",selectedChar.toString());
        else
            ELogger.log("onPressSelectText","is null"+e.getX()+","+e.getY());
        if (selectedChar != null) {
            FirstSelectedChar = selectedChar;
            LastSelectedChar = selectedChar;
            setLeftSlider(FirstSelectedChar);
            setRightSlider(LastSelectedChar);
            CurrentMode = Mode.PressSelectText;
            onShownSlider();
        } else {
            CurrentMode = Mode.PressUnSelectText;
            FirstSelectedChar = null;
            LastSelectedChar = null;
            onReleasedSlider();
        }
        releaseTouch();
        postInvalidate();

    }

    /**
     * @param FirstSelectedChar 设置左滑动条数据
     */
    private void setLeftSlider(TxtChar FirstSelectedChar) {
        mLeftSlider.Left = FirstSelectedChar.Left - SliderWidth * 2;
        mLeftSlider.Right = FirstSelectedChar.Left;
        mLeftSlider.Top = FirstSelectedChar.Bottom;
        mLeftSlider.Bottom = FirstSelectedChar.Bottom + SliderWidth * 2;
    }

    /**
     * @param LastSelectedChar 设置右滑动条数据
     */
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
        boolean isVerticalMode = false;
        if(readerContext!=null&&readerContext.getTxtConfig()!=null){
            isVerticalMode = readerContext.getTxtConfig().VerticalPageMode;
        }
       if(isVerticalMode){
            return  findCharByPositionOfVerticalMode(positionX,positionY);
       }else{
           return  findCharByPositionOfHorizontalMode(positionX,positionY);
       }
    }

    private TxtChar findCharByPositionOfVerticalMode(float positionX, float positionY) {
        IPage page = readerContext.getPageData().MidPage();
        int offset = readerContext.getPageParam().LinePadding / 2;
        //当前页面有数据才执行查找
        if (page != null && page.HasData()) {
            List<ITxtLine> lines = page.getLines();
            for (ITxtLine line : lines) {
                List<TxtChar> chars = line.getTxtChars();
                if (chars != null && chars.size() > 0) {
                    for (TxtChar c : chars) {
                        if (positionX > (c.Left - offset) && positionX < (c.Right + offset)) {
                            if (positionY > c.Top && positionY <= c.Bottom) {
                                return c;
                            }
                        } else {
                            break;//说明在下一行
                        }
                    }
                }
            }
        } else {
            ELogger.log(tag, "page not null and page hasData()");
        }
        return null;
    }

    private TxtChar findCharByPositionOfHorizontalMode(float positionX, float positionY) {
        IPage page = readerContext.getPageData().MidPage();
        int offset = readerContext.getPageParam().LinePadding / 2;
        //当前页面有数据才执行查找
        if (page != null && page.HasData()) {
            List<ITxtLine> lines = page.getLines();
            for (ITxtLine line : lines) {
                List<TxtChar> chars = line.getTxtChars();
                if (chars != null && chars.size() > 0) {
                    for (TxtChar c : chars) {
                        if (positionY > (c.Top - offset) && positionY < (c.Bottom + offset)) {
                            if (positionX > c.Left && positionX <= c.Right) {
                                return c;
                            }
                        } else {
                            break;//说明在下一行
                        }
                    }
                }
            }
        } else {
            ELogger.log(tag, "page not null and page hasData()");
        }
        return null;
    }

    /**
     * @return 找到长按选中的文字，找不到返回null
     */
    private TxtChar findCharByPositionWhileMove(float positionX, float positionY) {
        IPage page = readerContext.getPageData().MidPage();
        int offset = readerContext.getPageParam().LinePadding / 2;
        //当前页面有数据才执行查找
        if (page != null && page.HasData()) {
            List<ITxtLine> lines = page.getLines();
            for (ITxtLine line : lines) {
                List<TxtChar> chars = line.getTxtChars();
                if (chars != null && chars.size() > 0) {
                    for (TxtChar c : chars) {
                        if (positionY > (c.Top - offset) && positionY < (c.Bottom + offset)) {
                            if (positionX > c.Left && positionX < c.Right) {
                                return c;
                            } else {//说明在行的左边或者右边啊
                                TxtChar first = chars.get(0);
                                TxtChar last = chars.get(chars.size() - 1);
                                if (positionX < first.Left) {
                                    return first;
                                } else if (positionX > last.Right) {
                                    return last;
                                }
                            }
                        } else {
                            break;//说明在下一行
                        }
                    }
                }
            }
        } else {
            ELogger.log(tag, "page not null and page hasData()");
        }
        return null;
    }

    private Path mSliderPath = new Path();

    /**
     * @return 可能返回null
     */
    protected Path getLeftSliderPath() {
        return mLeftSlider.getPath(FirstSelectedChar, mSliderPath);

    }

    /**
     * @return 可能返回null
     */
    protected Path getRightSliderPath() {
        return mRightSlider.getPath(LastSelectedChar, mSliderPath);
    }

    //当前滑动选择的数据
    private final List<ITxtLine> mSelectLines = new ArrayList<>();

    /**
     * @return 获取当前选中的行文字
     */
    protected synchronized List<ITxtLine> getCurrentSelectTextLine() {
        return mSelectLines;
    }


    /**
     * 检测滑动选中的文字
     */
    protected synchronized void checkSelectedText() {
        Boolean Started = false;
        Boolean Ended = false;
        //清空之前选择的数据
        mSelectLines.clear();
        IPage currentPage = readerContext.getPageData().MidPage();
        //当前页面没有数据或者没有选择或者已经释放了长按选择事件，不执行
        if (currentPage == null || !currentPage.HasData() || FirstSelectedChar == null || LastSelectedChar == null) {
            return;
        }
        //获取当前页面行数据
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

    /**
     * @return 获取当前选中的文字，不会返回null
     */
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

    /**
     * @param TouchX
     * @param TouchY
     * @return 是否可以向后滑动
     */
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

    /**
     * @param TouchX
     * @param TouchY
     * @return 是否可以向前滑动
     */
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


    protected Boolean isPageNext() {
        //是否是执行下一页
        return getMoveDistance() < -10;
    }

    protected Boolean isPagePre() {
        //是否是执行上一页
        return getMoveDistance() > 10;
    }

    /**
     * @return 获取当前滑动距离
     */
    protected synchronized float getMoveDistance() {
        int i = (int) (mTouch.x - mDown.x);
        float m = mTouch.x - mDown.x;
        if (i < m) {
            return i + 1;
        }
        return m;
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
            CurrentMode = Mode.Normal;
            return;
        }
        pagePreTask.Run(null, readerContext);
    }

    protected void doPageNextDone() {
        //执行获取下一页数据
        IPage lastPage = readerContext.getPageData().LastPage();
        if (lastPage == null) {//没有下一页数据了
            CurrentMode = Mode.Normal;
            return;
        }

        pageNextTask.Run(null, readerContext);
    }

    protected Bitmap getTopPage() {
        if (TopPage != null && TopPage.isRecycled()) {
            TopPage = null;
        }
        return TopPage;
    }

    protected Bitmap getBottomPage() {
        if (BottomPage != null && BottomPage.isRecycled()) {
            BottomPage = null;
        }
        return BottomPage;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (readerContext != null) {
            readerContext.Clear();
        }
    }

    /**
     * 释放触摸事件
     */
    protected void releaseTouch() {
        mTouch.x = 0;
        mDown.x = 0;
        hasDown = false;
    }

    public enum Mode {
        Normal, //正常模式
        PagePreIng,//执行上一页数据获取等相关操作
        PageNextIng,//执行下一页数据获取等相关操作
        PressSelectText,//长按选中文字
        PressUnSelectText,//长按但是未选中文字
        SelectMoveForward, //向前滑动选中文字
        SelectMoveBack//向后滑动选中文字
    }


    //---------------------------获取上一页、获取下一页数据--------------------------------------

    protected final ITxtTask pageNextTask = new PageNextTask();
    protected final ITxtTask pagePreTask = new PagePreTask();
    protected final BitmapProduceTask bitmapProduceTask = new BitmapProduceTask();

    private class PageNextTask implements ITxtTask {
        @Override
        public void Run(ILoadListener callBack, final TxtReaderContext readerContext) {
            CurrentMode = Mode.PageNextIng;
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
            IPage midPage;
            IPage nextPage = null;

            if (nextFirstPage != null && nextFirstPage.HasData()) {
                if (nextFirstPage.isFullPage()) { //说明,nextFirstPage是完整的页数据，直接获取
                    firstPage = nextFirstPage;
                }
            }

            if (nextMidPage != null && nextMidPage.isFullPage()) {//说明之前的LastPage是完整的数据，直接获取
                midPage = nextMidPage;
            } else {
                midPage = nextMidPage;//说明之前的LastPage不是完整的数据，也直接获取
            }

            if (midPage != null && midPage.isFullPage()) {
                //midPage是完整页，说明可能有下一页数据，否则没有下一页数据了
                nextPage = readerContext.getPageDataPipeline().getPageStartFromProgress(midPage.getLastChar().ParagraphIndex, midPage.getLastChar().CharIndex + 1);
            }


            if (firstPage != null && nextFirstPage != null) {
                readerContext.getBitmapData().setFirstPage(readerContext.getBitmapData().MidPage());
                readerContext.getPageData().refreshTag[0] = 0;

            }

            if (midPage != null && midPage.HasData()) {
                readerContext.getBitmapData().setMidPage(readerContext.getBitmapData().LastPage());
                readerContext.getPageData().refreshTag[1] = 0;
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
            CurrentMode = Mode.PagePreIng;
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

            if (nextMayMidPage != null && nextMayMidPage.HasData()) {
                if (nextMayMidPage.isFullPage()) {//之前的FirstPage是满页的，直接获取
                    midPage = nextMayMidPage;
                } else {//之前的FirstPage不是是满页的，直接从头开始获取
                    midPage = readerContext.getPageDataPipeline().getPageStartFromProgress(0, 0);
                }
            }

            if (midPage != null && midPage.isFullPage()) {//现在说明midPage是满页的，可能有上一页数据与下一页数据
                if (midPage.getFirstChar().ParagraphIndex == 0 && midPage.getFirstChar().CharIndex == 0) {
                    firstPage = null;//之前的FirstPage不是是满页的，直接从头开始获取，没有firstPage
                } else {

                    firstPage = readerContext.getPageDataPipeline().getPageEndToProgress(midPage.getFirstChar().ParagraphIndex, midPage.getFirstChar().CharIndex);
                }
                nextPage = readerContext.getPageDataPipeline().getPageStartFromProgress(midPage.getLastChar().ParagraphIndex, midPage.getLastChar().CharIndex + 1);
            }


            //判断是否是相同数据然后进行判断是否需要进行刷新
            int needRefresh = 1;
            if (isSamePageData(nextPage, nextMayLastPage)) {
                needRefresh = 0;
                readerContext.getBitmapData().setLastPage(readerContext.getBitmapData().MidPage());
                nextPage = nextMayLastPage;
            }
            readerContext.getPageData().refreshTag[2] = needRefresh;

            //判断是否是相同数据然后进行判断是否需要进行刷新
            needRefresh = 1;
            if (isSamePageData(midPage, nextMayMidPage)) {
                needRefresh = 0;
                readerContext.getBitmapData().setMidPage(readerContext.getBitmapData().FirstPage());
                midPage = nextMayMidPage;
            }
            readerContext.getPageData().refreshTag[1] = needRefresh;
            readerContext.getBitmapData().setFirstPage(null);
            readerContext.getPageData().refreshTag[0] = 1;
            readerContext.getPageData().setFirstPage(firstPage);
            readerContext.getPageData().setMidPage(midPage);
            readerContext.getPageData().setLastPage(nextPage);
        }
    }


    private boolean isSamePageData(IPage f, IPage to) {
        if (f != null && to != null && f.HasData() && to.HasData()) {
            TxtChar fF = f.getFirstChar();
            TxtChar fL = f.getLastChar();
            TxtChar toF = to.getFirstChar();
            TxtChar toL = to.getLastChar();
            return fF.equals(toF) && fL.equals(toL);
        }
        return false;
    }

    protected void onPageProgress(IPage page) {
        if (page != null && page.HasData()) {
            TxtChar lastChar = page.getLastChar();
            onProgressCallBack(getProgress(lastChar.ParagraphIndex, lastChar.CharIndex));
        } else {
            ELogger.log(tag, "onPageProgress ,page data may be empty");
        }

    }

    protected float getProgress(int ParagraphIndex, int chartIndex) {
        float progress = 0;
        int pN = readerContext.getParagraphData().getParagraphNum();
        if (pN > 0 && pN > ParagraphIndex) {
            int index = readerContext.getParagraphData().getParaStartCharIndex(ParagraphIndex) + chartIndex;
            int num = readerContext.getParagraphData().getCharNum();
            if (num > 0) {
                if (index > num) {
                    progress = 1;
                } else {
                    progress = (float) index / (float) num;
                }
            }
        }
        return progress;
    }

    protected void onProgressCallBack(float progress) {
        if (pageChangeListener != null) {
            pageChangeListener.onCurrentPage(progress);
        }
        if (pageEdgeListener != null) {
            if (isFirstPage()) {
                pageEdgeListener.onCurrentFirstPage();
            }
            if (isLastPage()) {
                pageEdgeListener.onCurrentLastPage();
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

    /**
     * 刷新数据标签，int r1, int r2, int r3决定了页面数据是否出现刷新
     * ，不需要的话直接使用缓存bitmap
     *
     * @param r1
     * @param r2
     * @param r3
     */
    protected void refreshTag(int r1, int r2, int r3) {
        readerContext.getPageData().refreshTag[0] = r1;
        readerContext.getPageData().refreshTag[1] = r2;
        readerContext.getPageData().refreshTag[2] = r3;
    }


    /**
     * @param filePath
     * @param listener
     */
    private void loadFile(final String filePath, final ILoadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TxtFileLoader loader = new TxtFileLoader();
                loader.load(filePath, readerContext, new DataLoadListener(listener));
            }
        }).start();
    }

    /**
     * @param text
     * @param listener
     */
    private void loadTextStr(final String text, final ILoadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TextLoader loader = new TextLoader();
                loader.load(text, readerContext, new DataLoadListener(listener));
            }
        }).start();
    }

    /**
     * 数据加载监听
     */
    private class DataLoadListener implements ILoadListener {
        ILoadListener listener;

        public DataLoadListener(ILoadListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess() {
            checkMoveState();
            postInvalidate();
            post(new Runnable() {
                @Override
                public void run() {
                    onPageProgress(readerContext.getPageData().MidPage());
                    if(listener!=null) {
                        listener.onSuccess();
                    }
                }
            });

        }

        @Override
        public void onFail(TxtMsg txtMsg) {
            if(listener!=null) {
                listener.onFail(txtMsg);
            }
        }

        @Override
        public void onMessage(String message) {
            if(listener!=null) {
                listener.onMessage(message);
            }
        }
    }

    private void initReaderContext() {
        PageChangeMinMoveDistance = getWidth() / 5;
        PageParam param = new PageParam();
        param.PageWidth = getWidth();
        param.PageHeight = getHeight();
        readerContext.setPageParam(param);
    }


    //-------------------------------------------------------------
    private IPageEdgeListener pageEdgeListener;
    private IPageChangeListener pageChangeListener;
    private ISliderListener sliderListener;
    private ICenterAreaClickListener centerAreaClickListener;


    /**
     * @param pageEdgeListener 当前页是首页获取尾页监听,注意如果文本只有一页，那边首页与尾页都会回调
     */
    public void setOnPageEdgeListener(IPageEdgeListener pageEdgeListener) {
        this.pageEdgeListener = pageEdgeListener;
    }

    /**
     * @param pageChangeListener 页面进度改变监听
     */
    public void setPageChangeListener(IPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    /**
     * @param sliderListener 长按选择出现与消失监听
     */
    public void setOnSliderListener(ISliderListener sliderListener) {
        this.sliderListener = sliderListener;
    }

    /**
     * @param centerAreaClickListener 中间区域点击监听
     */
    public void setOnCenterAreaClickListener(ICenterAreaClickListener centerAreaClickListener) {
        this.centerAreaClickListener = centerAreaClickListener;
    }

    /**
     * 通过文件路径加载文件
     *
     * @param filePath 文件路径
     * @param listener 监听
     */
    public void loadTxtFile(final String filePath, final ILoadListener listener) {
        post(new Runnable() {
            @Override
            public void run() {
                initReaderContext();
                loadFile(filePath, listener);
            }

        });
    }

    /**
     * 通过字符串加载
     *
     * @param text     文本字符串数据
     * @param listener 监听
     */
    public void loadText(final String text, final ILoadListener listener) {
        post(new Runnable() {
            @Override
            public void run() {
                initReaderContext();
                loadTextStr(text, listener);
            }
        });
    }

    /**
     * @return 获取当前页的第一个字符, 当前页每页数据，返回null
     */
    public TxtChar getCurrentFirstChar() {
        IPage page = readerContext.getPageData().MidPage();
        //当前页面有数据才执行查找
        if (page != null && page.HasData()) {
            return page.getFirstChar();
        }
        return null;
    }


    /**
     * @return 获取当前页的第一行, 当前页每页数据，返回null
     */
    public ITxtLine getCurrentFirstLines() {
        IPage page = readerContext.getPageData().MidPage();
        //当前页面有数据才执行查找
        if (page != null && page.HasData()) {
            return page.getFirstLine();
        }
        return null;
    }

    /**
     * @param leftSlider 设置左侧滑动条
     */
    public void setLeftSlider(Slider leftSlider) {
        this.mLeftSlider = leftSlider;
        this.mLeftSlider.SliderWidth = SliderWidth;
    }

    /**
     * @param rightSlider 设置右侧滑动条
     */
    public void setRightSlider(Slider rightSlider) {
        this.mRightSlider = rightSlider;
        this.mRightSlider.SliderWidth = SliderWidth;

    }

    /**
     * 调用该方法释放长按选择模式
     */
    public void releaseSelectedState() {
        CurrentMode = Mode.Normal;
        postInvalidate();
    }


}
