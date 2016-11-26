package com.hw.menus;

import java.util.ArrayList;
import java.util.List;

import com.example.hwreaderui.R;
import com.hw.beans.ReaderStyle;
import com.hw.beans.ReaderStyle.Style;
import com.hw.txtreader.TxtReaderContex;
import com.hw.txtreader.readerstyle.TxtReaderNiceStyle;
import com.hw.txtreader.readerstyle.TxtReaderAncientry2Style;
import com.hw.txtreader.readerstyle.TxtReaderNightStyle;
import com.hw.txtreader.readerstyle.TxtReaderNormalStyle;
import com.hw.txtreader.readerstyle.TxtReaderSoftStyle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * @author 黄威 2016年11月1日上午10:18:46 主页：http://blog.csdn.net/u014614038
 */
public class StyleMenu extends PopupWindow {

	private TxtReaderContex mContext;
	private int mWindow_With;
	private int mWindow_Heigh;
	private int mSelectedposition;
	private View SelectedTag;
	private onTxtStyleChangeListener mListener;
	private List<ReaderStyle> mStyles = new ArrayList<ReaderStyle>();

	public StyleMenu(TxtReaderContex readerContex) {
		this.mContext = readerContex;
		
	}

	public void setonTxtStyleChangeListener(onTxtStyleChangeListener listener) {
		mListener = listener;
	}

	public void init() {
		WindowManager m = (WindowManager) mContext.mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;

		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext.mContext, R.layout.hwreader_stylemenu_layout,
				null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setContentView(layout);
		this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));

		mStyles.add(new TxtReaderNormalStyle());
		mStyles.add(new TxtReaderSoftStyle());
		mStyles.add(new TxtReaderNightStyle());
		mStyles.add(new TxtReaderNiceStyle());
		mStyles.add(new TxtReaderAncientry2Style());

		RelativeLayout s_layout1 = (RelativeLayout) layout.findViewById(R.id.txtstyle1_layout);
		RelativeLayout s_layout2 = (RelativeLayout) layout.findViewById(R.id.txtstyle2_layout);
		RelativeLayout s_layout3 = (RelativeLayout) layout.findViewById(R.id.txtstyle3_layout);
		RelativeLayout s_layout4 = (RelativeLayout) layout.findViewById(R.id.txtstyle4_layout);
		RelativeLayout s_layout5 = (RelativeLayout) layout.findViewById(R.id.txtstyle5_layout);

		///TextView view1 = (TextView) layout.findViewById(R.id.txtstyle1);
		//TextView view2 = (TextView) layout.findViewById(R.id.txtstyle2);
		//TextView view3 = (TextView) layout.findViewById(R.id.txtstyle3);
		//TextView view4 = (TextView) layout.findViewById(R.id.txtstyle4);
		//TextView view5 = (TextView) layout.findViewById(R.id.txtstyle5);

		final View slid1 = layout.findViewById(R.id.txtstyle1_tag);
		final View slid2 = layout.findViewById(R.id.txtstyle2_tag);
		final View slid3 = layout.findViewById(R.id.txtstyle3_tag);
		final View slid4 = layout.findViewById(R.id.txtstyle4_tag);
		final View slid5 = layout.findViewById(R.id.txtstyle5_tag);

		// view1.setBackgroundResource(getReaderSyleByStyle(Style.normal).getBrawable());
		// view2.setBackgroundResource(getReaderSyleByStyle(Style.sorft).getBrawable());
		// view3.setBackgroundResource(getReaderSyleByStyle(Style.night).getBrawable());
		// view4.setBackgroundResource(getReaderSyleByStyle(Style.ancientry1).getBrawable());
		// view5.setBackgroundResource(getReaderSyleByStyle(Style.ancientry2).getBrawable());

		mSelectedposition = 1;
		SelectedTag = slid1;

		s_layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mSelectedposition != 1 && mListener != null) {
					ReaderStyle readerStyle = getReaderSyleByStyle(Style.normal);
					mListener.onStyleChange(readerStyle, Style.normal);

					hideSlidtag(SelectedTag);
					SelectedTag = slid1;
					mSelectedposition = 1;
					showSlidTag(SelectedTag);
				}

			}
		});

		s_layout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mSelectedposition != 2 && mListener != null) {
					ReaderStyle readerStyle = getReaderSyleByStyle(Style.sorft);
					mListener.onStyleChange(readerStyle, Style.sorft);

					hideSlidtag(SelectedTag);
					SelectedTag = slid2;
					mSelectedposition = 2;
					showSlidTag(SelectedTag);
				}

			}
		});

		s_layout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSelectedposition != 3 && mListener != null) {
					ReaderStyle readerStyle = getReaderSyleByStyle(Style.night);
					mListener.onStyleChange(readerStyle, Style.night);

					hideSlidtag(SelectedTag);
					SelectedTag = slid3;
					mSelectedposition = 3;
					showSlidTag(SelectedTag);
				}

			}
		});

		s_layout4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSelectedposition != 4 && mListener != null) {
					ReaderStyle readerStyle = getReaderSyleByStyle(Style.nice);
					mListener.onStyleChange(readerStyle, Style.nice);

					hideSlidtag(SelectedTag);
					SelectedTag = slid4;
					mSelectedposition = 4;
					showSlidTag(SelectedTag);
				}

			}
		});

		s_layout5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSelectedposition != 5 && mListener != null) {
					ReaderStyle readerStyle = getReaderSyleByStyle(Style.ancientry2);
					mListener.onStyleChange(readerStyle, Style.ancientry2);
					hideSlidtag(SelectedTag);
					SelectedTag = slid5;
					mSelectedposition = 5;
					showSlidTag(SelectedTag);
				}

			}
		});

		ReaderStyle historyStyle = mContext.mViewSetting.getPageBackground();
		switch (historyStyle.getStyle()) {
		case normal:
			mSelectedposition = -1;
			SelectedTag = slid1;
			s_layout1.performClick();
			break;
		case sorft:
			mSelectedposition = -1;
			SelectedTag = slid2;
			s_layout2.performClick();
			break;
		case night:
			mSelectedposition = -1;
			SelectedTag = slid3;
			s_layout3.performClick();
			break;
		case nice:
			mSelectedposition = -1;
			SelectedTag = slid4;
			s_layout4.performClick();
			break;
		case ancientry2:
			mSelectedposition = -1;
			SelectedTag = slid5;
			s_layout5.performClick();
			break;

		default:
			break;
		}

		

	}

	private ReaderStyle getReaderSyleByStyle(ReaderStyle.Style style) {

		for (ReaderStyle s : mStyles) {
			if (s.getStyle() == style) {
				return s;
			}
		}
		return null;

	}

	private void hideSlidtag(View mSelectedTag) {
		mSelectedTag.setVisibility(View.INVISIBLE);
	}

	private void showSlidTag(View mSelectedTag) {
		mSelectedTag.setVisibility(View.VISIBLE);

	}

	public interface onTxtStyleChangeListener {
		public void onStyleChange(ReaderStyle readerStyle, ReaderStyle.Style style);

	}

}
