package menus;

import com.example.testtxtbook.R;

import android.annotation.SuppressLint;
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

public class TxtViewMenu extends PopupWindow {
	private Context mContext;
	private int mWindow_With;
	private int mWindow_Heigh;

	private TxtMenuClockListener mListener;

	public TxtViewMenu(Context context) {
		this.mContext = context;
		inite();
	}

	public void setOnTxtMenuClickListener(TxtMenuClockListener listener) {
		mListener = listener;
	}

	@SuppressLint("NewApi")
	private void inite() {
		WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;

		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.txtmenu_layout, null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(false);
		this.setOutsideTouchable(false);
		this.setContentView(layout);
		ColorDrawable dw = new ColorDrawable(Color.parseColor("#88000000"));
		this.setBackgroundDrawable(dw);

		RelativeLayout text = (RelativeLayout) layout.findViewById(R.id.txtmenu_textlayout);
		RelativeLayout progress = (RelativeLayout) layout.findViewById(R.id.txtmenu_progresslayout);
		RelativeLayout stytle = (RelativeLayout) layout.findViewById(R.id.txtmenu_stytlelayout);
		RelativeLayout light = (RelativeLayout) layout.findViewById(R.id.txtmenu_lightlayout);

		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mListener != null)
					mListener.onTextMenuClicked();
			}
		});

		progress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mListener != null)
					mListener.onProgressMenuClicked();
			}
		});

		stytle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mListener != null)
					mListener.onStytleMenuClicked();
			}
		});

		light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mListener != null)
					mListener.onLightMenuClicked();
			}
		});

	}

	public interface TxtMenuClockListener {

		public void onTextMenuClicked();

		public void onProgressMenuClicked();

		public void onStytleMenuClicked();

		public void onLightMenuClicked();

	}

}
