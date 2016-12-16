package hwtxtreader.main;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import hwtxtreader.bean.TxtFile;
import hwtxtreader.bean.TxtLoadListsner;
import hwtxtreader.bean.Txterror;
import hwtxtreader.utils.DisPlayUtil;

public class TxtManagerImp implements TxtManager {

	private TxtReadViewConfig viewconfig;
	private Context context;
	private int txtsize;
	private Paint textpaint;
	private Paint pageindextextpaint;
	private int viewwith;
	private int viewheigh;
	private int linesnums;
	private TxtFile txtFile;
	private ManagerToModelTransform modeltransform;
	private TxtLoadListsner errorlistener;

	public TxtManagerImp(Context context, TxtFile txtFile, TxtLoadListsner txtLoadListsner) {
		this.context = context;
		this.txtFile = txtFile;
		setOnTxtLoadListsner(txtLoadListsner);
		viewconfig = new TxtReadViewConfig();
		textpaint = new Paint();
		pageindextextpaint = new Paint();
		initeViewWith();
		committextsize();
		CommitSetting();
	}

	private void initeViewWith() {

		WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);
		viewwith = metrics.widthPixels;
		viewheigh = metrics.heightPixels;
		if (!getViewConfig().isHidestatebar()) {// 减去状态栏的高度
			int barheigh = getBarheigh();
			viewheigh = viewheigh - barheigh;
		}
	}

	/**
	 * TODO 下午3:10:32
	 * 
	 * @return 返回状态栏高度
	 */
	private int getBarheigh() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return 0;
	}

	@Override
	public void CommitSetting() {
		getTextPaint().setAntiAlias(true);
		getTextPaint().setFakeBoldText(getViewConfig().getFakeBoldText());
		getTextPaint().setTextSize(getViewConfig().getTextSize());
		getTextPaint().setColor(getViewConfig().getTextColor());
		getPageIndexTextPaint().setColor(getViewConfig().getPageindextextcolor());
		getPageIndexTextPaint().setTextSize(getViewConfig().getPageindextextsize());

		if (getViewConfig().getTextSort() != null) {
			getTextPaint().setTypeface(Typeface.createFromAsset(context.getAssets(), getViewConfig().getTextSort()));
			getPageIndexTextPaint()
					.setTypeface(Typeface.createFromAsset(context.getAssets(), getViewConfig().getTextSort()));
		}
		if (modeltransform != null) {
			modeltransform.PostResult(true);
		}

		int textpxheigh = getTextsize();
		linesnums = (getViewHeigh() - getViewConfig().getPadingtop() - getViewConfig().getPadingbottom())
				/ (textpxheigh + getViewConfig().getLinesPadding());
	}

	@Override
	public TxtReadViewConfig getViewConfig() {

		return viewconfig;
	}

	@Override
	public int getTextsize() {

		return txtsize;
	}

	private void committextsize() {
		txtsize = DisPlayUtil.sp2px(context, getViewConfig().getTextSize());

	}

	@Override
	public Paint getTextPaint() {

		return textpaint;
	}

	@Override
	public int getViewWith() {

		return viewwith;
	}

	@Override
	public int getViewHeigh() {

		return viewheigh;
	}

	@Override
	public void setModeTransform(Transformer t) {
		modeltransform = (ManagerToModelTransform) t;
	}

	@Override
	public int getLinesNums() {

		return linesnums;
	}

	@Override
	public void LoadFile() {

		modeltransform.loadTxtbook(new Transformer() {

			@Override
			public void PostResult(Boolean t) {
				if (t) {
					System.out.println("加载文件成功1");
					if (errorlistener != null) {
						errorlistener.onLoadSucess();
					}
				}

			}

			@Override
			public void PostError(Txterror txterror) {

				onerrortransform(txterror);
			}

		});
	}

	@Override
	public TxtFile getTxtFile() {

		return txtFile;
	}

	@Override
	public Paint getPageIndexTextPaint() {

		return pageindextextpaint;
	}

	@Override
	public void jumptopage(int pageindex) {
		modeltransform.jumptopage(pageindex);
	}

	@Override
	public void setOnTxtLoadListsner(TxtLoadListsner t) {
		errorlistener = t;

	}

	private void onerrortransform(Txterror error) {
		if (errorlistener != null) {
			errorlistener.onError(error);
		}

	}

	@Override
	public void onTxtLoaderror(Txterror txterror) {
		onerrortransform(txterror);
	}

	@Override
	public void separatepage() {
		if (modeltransform != null) {
			modeltransform.separatepage();
		}
	}

	@Override
	public void refreshBitmapText() {
		if (modeltransform != null) {
			CommitSetting();
			modeltransform.refreshbitmaptext();
		}
	}

	@Override
	public void refreshBitmapBackground() {
		if (modeltransform != null) {			
			modeltransform.refreshbitmapbackground();
		}
	}

}
