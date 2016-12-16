package menus;

import com.example.testtxtbook.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import hwtxtreader.main.TxtReadViewConfig;

public class TxtTextMenu extends PopupWindow {

	private final int MIN_TEXTSIZE = 30;
	private final int MAX_TEXTSIZE = 50;
	private Context mContext;
	private int mWindow_With;
	private int mWindow_Heigh;
	private int TextSizee = MIN_TEXTSIZE;
	private onTxtTextChangeListener mListener;

	public TxtTextMenu(Context context, int defualttextsize) {
		this.mContext = context;
		TextSizee = defualttextsize;
		inite();
	}

	public void setonTxtTextChangeListener(onTxtTextChangeListener listener) {
		mListener = listener;
	}

	private void inite() {
		WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels; 
 
		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.txttextmenu_layout, null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setContentView(layout);
		ColorDrawable dw = new ColorDrawable(Color.parseColor("#88000000"));
		this.setBackgroundDrawable(dw);

		Button decreasetextsize = (Button) layout.findViewById(R.id.txttextmenu_textsize_decrease);
		Button add = (Button) layout.findViewById(R.id.txttextmenu_textsize_add);
		RadioGroup group = (RadioGroup) layout.findViewById(R.id.txttextmenu_texsort_radiogroup);

		decreasetextsize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextSizee--;

				if (mListener != null && TextSizee >= MIN_TEXTSIZE) {
					mListener.onTextSizeChange(TextSizee);
				}

				TextSizee = TextSizee < MIN_TEXTSIZE ? MIN_TEXTSIZE : TextSizee;
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextSizee++;

				if (mListener != null && TextSizee <= MAX_TEXTSIZE) {
					mListener.onTextSizeChange(TextSizee);
				}
				TextSizee = TextSizee > MAX_TEXTSIZE ? MAX_TEXTSIZE : TextSizee;
			}
		});

		group.check(R.id.txttextmenu_texsort_1);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
		 	public void onCheckedChanged(RadioGroup group, int checkedId) {

	 			if (checkedId == R.id.txttextmenu_texsort_1) {

	 				if (mListener != null) {
			 			mListener.onTextSortChange("fonts/font2.ttf");
					}
				} else if (checkedId == R.id.txttextmenu_texsort_2) {

					if (mListener != null) {
						mListener.onTextSortChange("fonts/font1.ttf");
					}
				} else if (checkedId == R.id.txttextmenu_texsort_3) {

					if (mListener != null) {
						mListener.onTextSortChange("fonts/font3.ttf");
					}
				}
			}

		});

	}

	public interface onTxtTextChangeListener {

		public void onTextSizeChange(int spTextsize);

		public void onTextSortChange(String textsort);
	}

}
