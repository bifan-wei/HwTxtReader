package com.hw.menus;

import com.example.hwreaderui.R;
import com.hw.txtreader.TxtReaderContex;
import com.hw.txtreader.TxtReaderViewSettings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author 黄威 2016年11月1日上午10:18:50 主页：http://blog.csdn.net/u014614038
 */
public class TextMenu extends PopupWindow {
	private static final String TEXT_SORT_FILE1 = "fonts/font2.ttf";
	private static final String TEXT_SORT_FILE2 = "fonts/font1.ttf";
	private static final String TEXT_SORT_FILE3 = "fonts/font3.ttf";

	private final int MIN_TEXTSIZE = 30;
	private final int MAX_TEXTSIZE = 70;
	private TxtReaderContex mReaderContext;
	private int mWindow_With;
	private int mWindow_Heigh;
	private int TextSize = MIN_TEXTSIZE;
	private onTxtTextChangeListener mListener;

	public TextMenu(TxtReaderContex txtReaderContex) {
		this.mReaderContext = txtReaderContex;
		TextSize = (int) txtReaderContex.mViewSetting.TextSize;
		init();
	}

	public void setonTxtTextChangeListener(onTxtTextChangeListener listener) {
		mListener = listener;
	}

	private void init() {
		WindowManager m = (WindowManager) mReaderContext.mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;

		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		LinearLayout layout = (LinearLayout) LinearLayout.inflate(mReaderContext.mContext,
				R.layout.hwreader_textmenu_layout, null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setContentView(layout);
		this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));

		initviews(layout);
	}

	private void initviews(View layout) {
		Button decreasetextsize = (Button) layout.findViewById(R.id.txttextmenu_textsize_decrease);
		Button add = (Button) layout.findViewById(R.id.txttextmenu_textsize_add);
		RadioGroup group = (RadioGroup) layout.findViewById(R.id.txttextmenu_texsort_radiogroup);
		CheckBox checkBox = (CheckBox) layout.findViewById(R.id.txttextmenu_text_bold);
		// 获取历史记录数据
		TxtReaderViewSettings viewSettings = mReaderContext.mViewSetting;
		decreasetextsize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextSize = TextSize - 2;

				if (mListener != null && TextSize >= MIN_TEXTSIZE) {
					mListener.onTextSizeDec();
				}

				TextSize = TextSize < MIN_TEXTSIZE ? MIN_TEXTSIZE : TextSize;
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextSize = TextSize + 2;

				if (mListener != null && TextSize <= MAX_TEXTSIZE) {
					mListener.onTextSizeAdd();
				}
				TextSize = TextSize > MAX_TEXTSIZE ? MAX_TEXTSIZE : TextSize;
			}
		});

		Boolean bold = viewSettings.MakeBoldText;
		checkBox.setChecked(bold);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mListener != null) {
					mListener.onTextBold(isChecked);
				}
			}
		});
		
		String textfile = viewSettings.TexttTypeFile;
		
		if (textfile == null) {
			group.check(R.id.txttextmenu_texsort_1);
		} else if (textfile.equals(TEXT_SORT_FILE1)) {
			group.check(R.id.txttextmenu_texsort_1);
		} else if (textfile.equals(TEXT_SORT_FILE2)) {
			group.check(R.id.txttextmenu_texsort_2);
		} else if (textfile.equals(TEXT_SORT_FILE3)) {
			group.check(R.id.txttextmenu_texsort_3);
		}

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (mListener != null) {
					switch (checkedId) {
					case R.id.txttextmenu_texsort_1:
						mListener.onTextSortChange(TEXT_SORT_FILE1);
						break;
					case R.id.txttextmenu_texsort_2:
						mListener.onTextSortChange(TEXT_SORT_FILE2);
						break;
					case R.id.txttextmenu_texsort_3:
						mListener.onTextSortChange(TEXT_SORT_FILE3);
						break;
					default:
						break;
					}

				}

			}

		});

		TextSize = (int) viewSettings.TextSize;

	}

	public interface onTxtTextChangeListener {

		public void onTextSizeAdd();

		public void onTextSizeDec();

		public void onTextBold(Boolean isBold);

		public void onTextSortChange(String textsort);
	}

}
