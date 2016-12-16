package hwtxtreader.main;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import hwtxtreader.bean.Page;
import hwtxtreader.bean.Txterror;

/**
 * 阅读器界面view
 * 
 * @author huangwei 2016下午4:54:01 主页：http://blog.csdn.net/u014614038
 * 
 */
public class TxtReadView extends View {

	public static final int PAGE_NONE = 0x00;
	private static final int PAGE_DOWN = 0x02;
	private static final int PAGE_UP = 0x01;

	private int pagedragstate = PAGE_NONE;
	private int prepagedragstate = pagedragstate;
	private int shadowwith;
	private TxtManager mManager;
	private TxtModel txtModel;
	private Paint shadowPaint;

	private Bitmap preDrawBitmap;
	private Bitmap behiDrawBitmap;

	private float divider_position;
	private float clickX0, MoveX0;// 点击时最开始位置、移动时最开始位置

	private Boolean isOnAnimation = false;// 是否在执行动画
	private Boolean tounchforbitdden = false;// 是否允许tounch事件
	private Boolean menushowing = false;
	private Boolean currentisfirstpage = true;// 是否是在第一页
	private Boolean currentislastpage = false;// 是否在最后一页
	private Boolean actiondowndone = false;
	private Boolean showshadow = true;
	private int pageindex = 1;
	private TxtPageChangeListsner txtPageChangeListsner;
	private TxtViewCenterAreaTouchListener txtViewCenterAreaTouchListener;
	private PageSeparateListener pageSeparateListener;

	public TxtReadView(Context context) {
		super(context);

	}

	public TxtReadView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public TxtReadView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public TxtReadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);

	}

	public TxtManager getTxtMAnager() {
		if (mManager == null) {
			throw new NullPointerException("没有传入txtmanager----");
		}
		return mManager;
	}

	public void setTxtManager(TxtManager txtManager) {
		this.mManager = txtManager;
		init();
	}

	public void init() {
		shadowPaint = new Paint();
		mManager = getTxtMAnager();
		shadowwith = 5;
		txtModel = new TxtModelImp(getContext(), mManager, mManager.getTxtFile().getFilepath());
		txtModel.setModeToViewTransform(new ModeToViewTransform() {

			@Override
			public void PostResult(Boolean t) {
			}

			@Override
			public void PostError(Txterror txterror) {
			}

			@Override
			public void ReFreshView() {

				if (pagedragstate == PAGE_UP) {
					onloadprepage(currentisfirstpage);
				} else if (pagedragstate == PAGE_DOWN) {
					onloadnextpage(currentislastpage);
				} else {
					onloadfirstpage(currentislastpage);
				}
 
			}

			@Override
			public void onloadfirstpage(Boolean islastpage) {
				preDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
				behiDrawBitmap = txtModel.getBitmapCache().getNextbitmap();
				divider_position = getViewWith();
				pagedragstate = PAGE_NONE;
				currentisfirstpage = true;
				currentislastpage = islastpage;
				showshadow = !islastpage;
				postInvalidate();
			}

			@Override
			public void onloadnextpage(Boolean islastpage) {
				preDrawBitmap = txtModel.getBitmapCache().getPrebitmap();
				behiDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
				currentisfirstpage = false;
				divider_position = 0;
				currentislastpage = islastpage;
				showshadow = !islastpage;
				postInvalidate();
			}

			@Override
			public void onloadprepage(Boolean isfirsttpage) {
				preDrawBitmap = txtModel.getBitmapCache().getPrebitmap();
				behiDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
				currentisfirstpage = isfirsttpage;
				currentislastpage = false;
				divider_position = 0;
				postInvalidate();
			}

			@Override
			public void onNoData() {
				currentisfirstpage = true;
				currentislastpage = true;
				showshadow = false;

			}

			@Override
			public void onloadFileException() {
				currentisfirstpage = true;
				currentislastpage = true;
				showshadow = false;
			}

			@Override
			public void onloadpagefromindex(Boolean isfirstpage, Boolean islastpage) {

				preDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
				behiDrawBitmap = txtModel.getBitmapCache().getNextbitmap();
				divider_position = getViewWith();
				pagedragstate = PAGE_NONE;
				currentisfirstpage = isfirstpage;
				currentislastpage = islastpage;
				showshadow = !islastpage;
				postInvalidate();

			}

			@Override
			public void onPageSeparateStart() {
				if (pageSeparateListener != null) {
					pageSeparateListener.onSeparateStart();
				}
			}

			@Override
			public void onPageSeparateDone() {
				if (pageSeparateListener != null) {
					pageSeparateListener.onSeparateDone();

				}

				if (txtPageChangeListsner != null) {

					txtPageChangeListsner.onCurrentPage(pageindex, txtModel.getPageNums());
				}
			}

		});

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Bitmap preBitmap = getPreBrawPageBitmap();

		if (preBitmap != null) {
			Rect srcRect = new Rect(0, 0, getViewWith(), preBitmap.getHeight());// 截取bmp1中的矩形区域
			Rect dstRect = new Rect(0, 0, getViewWith(), getViewHeigh());// bmp1在目标画布中的位置
			canvas.drawBitmap(preBitmap, srcRect, dstRect, getTextPaint());
			canvas.save();
		}

		if (showshadow) {
			int left = (int) (divider_position - shadowwith);
			left = left > 0 ? left : 0;
			int top = 0;
			int right = (int) divider_position;
			int bottom = getViewHeigh();
			canvas.drawRect(left, top, right, bottom, getBgLightPaint());
			canvas.save();
		}
		Bitmap behideBitmap = getBehideBrawPageBitmap();

		if (behideBitmap != null) {
			int behi_x = 0;
			int behi_y = 0;

			float behi_w = getViewWith() - divider_position;

			int behi_h = behideBitmap.getHeight();
			Rect srcRect1 = new Rect(behi_x, behi_y, (int) behi_w, behi_h);// 截取bmp1中的矩形区域

			float behi_leftX = divider_position;
			int behi_rigthX = getViewWith();

			Rect dstRect1 = new Rect((int) behi_leftX, 0, behi_rigthX, getViewHeigh());// bmp1在目标画布中的位置

			canvas.drawBitmap(behideBitmap, srcRect1, dstRect1, getTextPaint());
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 执行动画过程中不允许手势操作
		if (isOnAnimation) {
			return true;
		}

		float x0 = event.getX();
		float y0 = event.getY();

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			if (!menushowing) {

				if (inShowMeneArea(x0, y0)) {

					if (txtViewCenterAreaTouchListener != null) {
						txtViewCenterAreaTouchListener.onAreaTouch();
						menushowing = true;
						return true;
					}

				}

			} else {

				if (txtViewCenterAreaTouchListener != null) {
					txtViewCenterAreaTouchListener.onOutSideAreaTouch();
					menushowing = false;
					return true;
				}

			}

			clickX0 = x0;
			MoveX0 = x0;
			actiondowndone = true;
			pagedragstate = PAGE_NONE;

			break;

		case MotionEvent.ACTION_MOVE:

			if (!menushowing && actiondowndone && !tounchforbitdden) {
				doMove(event);
			}

			break;

		case MotionEvent.ACTION_UP:
			clickX0 = MoveX0;

			if (!menushowing && actiondowndone && !tounchforbitdden) {
				doActionUp();
				actiondowndone = false;
			}

			break;
		default:
			break;
		}

		return true;

	}

	public Bitmap getPageBitmap() {

		return txtModel.getBitmapCache().getPagebitmap();
	}

	public Paint getTextPaint() {
		return getTxtMAnager().getTextPaint();
	}

	private Paint getBgLightPaint() {

		// int shadowwith1 = shadowwith;
		LinearGradient gradient = new LinearGradient(divider_position - shadowwith, 0, divider_position, 0,
				new int[] { Color.parseColor("#00666666"), Color.parseColor("#11666666"), Color.parseColor("#33666666"),
						Color.parseColor("#44666666"), Color.parseColor("#88666666"), Color.parseColor("#ee666666") },
				null, LinearGradient.TileMode.CLAMP);
		shadowPaint.setShader(gradient);
		return shadowPaint;
	}

	private Bitmap getBehideBrawPageBitmap() {

		return behiDrawBitmap;

	}

	private Bitmap getPreBrawPageBitmap() {

		return preDrawBitmap;

	}

	private void doActionUp() {

		switch (pagedragstate) {

		case PAGE_UP:

			if (!currentisfirstpage) {

				DoPageUpAnimation();
			}

			break;

		case PAGE_DOWN:

			if (!currentislastpage) {

				DoPageDownAnimation();
			}

			break;

		default:
			if (clickX0 < getViewWith() / 2) {// 执行上一页点击事件

				if (!currentisfirstpage) {

					if (divider_position >= getViewWith()) {
						divider_position = 0;
						justslidtoleft();

					}

					DoPageUpAnimation();
				}

			} else {// 下一页点击事件

				if (!currentislastpage) {

					if (divider_position <= 0) {
						divider_position = getViewWith();
						justslidtoright();
					}

					DoPageDownAnimation();
				}
			}
			break;
		}

	}

	private void doMove(MotionEvent event) {

		float movex = event.getX() - MoveX0;
		MoveX0 = event.getX();

		if (pagedragstate == PAGE_UP && MoveX0 < clickX0) {
			pagedragstate = PAGE_NONE;

			return;
		}

		if (pagedragstate == PAGE_DOWN && MoveX0 > clickX0) {
			pagedragstate = PAGE_NONE;

			return;
		}

		if (pagedragstate == PAGE_NONE) {

			if (event.getX() - clickX0 > 1) {// 右滑
												// ，就是翻上一页,大于-5或者小于-5是为了避免只是点击而不滑动的情况
				pagedragstate = PAGE_UP;

			} else if (event.getX() - clickX0 < -1) {
				pagedragstate = PAGE_DOWN;
				// 如果分界线在最左，还是进行右移动，那么就是执行滑动到上一页状态

			} else {
				pagedragstate = PAGE_NONE;
			}
		}

		if ((pagedragstate == PAGE_UP || pagedragstate == PAGE_NONE) && currentisfirstpage) {// 第一页不允许执行前一页操作
			pagedragstate = PAGE_NONE;
			System.out.println("this is the firstpage");
			return;
		}

		if ((pagedragstate == PAGE_DOWN || pagedragstate == PAGE_NONE) && currentislastpage) {// 最好一页不允许执行下一页操作
			pagedragstate = PAGE_NONE;
			System.out.println("this is the lastpage");
			return;
		}

		// 如果状态为执行下一页时，通过边界判断来改变页面，并且改变边界，确保 movex < 0，就是确定执行的是
		// 执行下一页操作，因为有种情况是忽然改变手势方向，就是movex可能为>0,这样画面就变了
		if (pagedragstate == PAGE_DOWN && divider_position <= 0 && movex < 0) {
			divider_position = getViewWith();
			justslidtoright();
			return;
		}

		if (pagedragstate == PAGE_UP && divider_position >= getViewWith() && movex > 0) {
			divider_position = 0;
			justslidtoleft();
			return;
		}

		divider_position = (int) (divider_position + movex);
		divider_position = divider_position <= 0 ? 0 : divider_position;
		divider_position = divider_position >= getViewWith() ? getViewWith() : divider_position;

		postInvalidate();

	}

	/**
	 * TODO执行显示切换上一页 下午2:40:03
	 */
	private void justslidtoleft() {

		preDrawBitmap = txtModel.getBitmapCache().getPrebitmap();
		behiDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
	}

	/**
	 * TODO 执行显示切换下一页 下午2:39:14
	 */
	private void justslidtoright() {
		preDrawBitmap = txtModel.getBitmapCache().getMidbitmap();
		behiDrawBitmap = txtModel.getBitmapCache().getNextbitmap();

	}

	private void DoPageDownAnimation() {
		isOnAnimation = true;

		ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
		animator.setDuration(8000);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float f = 1 - animation.getAnimatedFraction();
				float p = divider_position * f;

				if (p > 0) {
					divider_position = (int) p;
					postInvalidate();
				} else {
					divider_position = 0;
					postInvalidate();

					animation.cancel();
					pagedragstate = PAGE_NONE;
					if (!currentislastpage) {
						txtModel.loadnextpage();
					}

					if (txtPageChangeListsner != null) {
						Page midpage = txtModel.getMidPage();
						pageindex = midpage == null ? 0 : midpage.getPageindex();
						txtPageChangeListsner.onCurrentPage(pageindex, txtModel.getPageNums());
					}

					isOnAnimation = false;

				}
			}
		});

		animator.start();

	}

	private void DoPageUpAnimation() {
		isOnAnimation = true;

		ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
		animator.setDuration(5000);
		animator.setInterpolator(new LinearInterpolator());
		final float leftwith = getViewWith() - divider_position;
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float f = animation.getAnimatedFraction();
				float ls = leftwith * f;

				if (divider_position < getViewWith() - 5) {
					divider_position = divider_position + ls;
					postInvalidate();
				} else {
					divider_position = getViewWith();
					postInvalidate();
					animation.cancel();
					pagedragstate = PAGE_NONE;
					if (!currentisfirstpage) {
						txtModel.loadprepage();
					}

					if (txtPageChangeListsner != null) {
						Page predpage = txtModel.getPrePage();
						pageindex = predpage == null ? 1 : predpage.getPageindex();

						txtPageChangeListsner.onCurrentPage(pageindex, txtModel.getPageNums());
					}
					isOnAnimation = false;
				}

			}

		});

		animator.start();

	}

	private boolean inShowMeneArea(float x, float y) {

		return InXArea(x) && InYArea(y);
	}

	private boolean InYArea(float yposition) {
		return yposition > getBorderTop() && yposition < getBorderBottom();
	}

	private boolean InXArea(float xposition) {
		return xposition > getBordeleft() && xposition < getBorderRight();
	}

	private int getBorderBottom() {

		return getViewHeigh() * 3 / 5;
	}

	private int getBorderTop() {

		return getViewHeigh() * 2 / 5;
	}

	private float getBorderRight() {

		return getViewWith() * 3 / 5 + 5;
	}

	private float getBordeleft() {

		return getViewWith() * 2 / 5 - 5;
	}

	private int getViewWith() {

		return mManager.getViewWith();
	}

	private int getViewHeigh() {
		return mManager.getViewHeigh();
	}

	public void setOnTxtPageChangeListener(TxtPageChangeListsner txtPageChangeListsner) {

		this.txtPageChangeListsner = txtPageChangeListsner;
	}

	public void setOnTxtViewCenterAreaTouchListener(TxtViewCenterAreaTouchListener txtViewCenterAreaTouchListener) {
		this.txtViewCenterAreaTouchListener = txtViewCenterAreaTouchListener;
	}

	public void setOnPageSeparateListener(PageSeparateListener pageSeparateListener) {
		this.pageSeparateListener = pageSeparateListener;
	}

}
