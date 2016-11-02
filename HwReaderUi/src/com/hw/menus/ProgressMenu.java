package com.hw.menus;

import com.example.hwreaderui.R;
import com.hw.txtreader.TxtReaderContex;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 黄威 2016年11月2日上午11:10:28 主页：http://blog.csdn.net/u014614038
 */
public class ProgressMenu extends PopupWindow {
	private TxtReaderContex mReaderContext;
	private int mWindow_With;
	private int mWindow_Heigh;
	private EditText mEditText;
	private TextView mText;
	private Button mConcern;
	private int pageindex = 1, pagecounts = 1, preindex = -1;
	private onProgressChangeListener mListener;

	public ProgressMenu(TxtReaderContex context) {
		this.mReaderContext = context;
		inite();
	}

	public void setonProgressChangeListener(onProgressChangeListener listener) {
		mListener = listener;
	}

	private void inite() {
		WindowManager m = (WindowManager) mReaderContext.mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;

		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		RelativeLayout layout = (RelativeLayout) LinearLayout.inflate(mReaderContext.mContext,
				R.layout.hwreader_progressmenu_layout, null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setContentView(layout);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));

		initviews(layout);
	}

	private void initviews(View layout) {
		mEditText = (EditText) layout.findViewById(R.id.pregress_edittext);
		mConcern = (Button) layout.findViewById(R.id.txtprogress_concern);
		mText = (TextView) layout.findViewById(R.id.txtprogress_text);

		mConcern.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					int pageindex = getPageIndex();
					if (pageindex >= 0 && pageindex != preindex) {
						preindex = pageindex;
						mListener.onProgressChange(pageindex);
						setPageIndex(pageindex, pagecounts);
					}
				}
			}

			private int getPageIndex() {
				String pageindexstr = mEditText.getText().toString().trim();
				if (pageindexstr.length() == 0) {
					return -1;
				}
				int pageindex = Integer.valueOf(pageindexstr);
				pageindex = pageindex - 1;
				if (pageindex >= 0 && pageindex <= pagecounts) {
					return pageindex;
				}
				return 1;
			}
		});

	}

	public void setPageIndex(int pageindex0, int pagecounts0) {
		this.pagecounts = pagecounts0;
		this.pageindex = pageindex0;
		mText.setText(pageindex + "/" + pagecounts);

	}

	public interface onProgressChangeListener {
		public void onProgressChange(int progress);

	}

	public int getPageindex() {
		return pageindex;
	}

	public int getPagecounts() {
		return pagecounts;
	}

}
