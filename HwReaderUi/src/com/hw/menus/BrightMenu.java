package com.hw.menus;

import com.example.hwreaderui.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author 黄威 2016年11月1日上午10:18:24 主页：http://blog.csdn.net/u014614038
 */
public class BrightMenu extends PopupWindow {
	private Context mContext;
	private int mWindow_With;
	private int mWindow_Heigh;
	private SeekBar mSeekBar;
	private TextView mText;
	private Button mConcern;

	public BrightMenu(Context context) {
		this.mContext = context;
		init();
	}

	private void init() {
		SaveSystemdefaultBrignhtness(mContext);
		WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		m.getDefaultDisplay().getMetrics(metrics);

		mWindow_With = metrics.widthPixels;
		mWindow_Heigh = metrics.heightPixels;

		int rootwith = mWindow_With;
		int rootheigh = mWindow_Heigh / 7;

		RelativeLayout layout = (RelativeLayout) LinearLayout.inflate(mContext, R.layout.hwreader_lightmenu, null);

		this.setWidth(rootwith);
		this.setHeight(rootheigh);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setContentView(layout);
		ColorDrawable dw = new ColorDrawable(Color.parseColor("#88000000"));
		
		this.setBackgroundDrawable(dw);

		mSeekBar = (SeekBar) layout.findViewById(R.id.txtprogress_seekbar);
		mConcern = (Button) layout.findViewById(R.id.txtprogress_concern);
		mConcern.setVisibility(View.GONE);
		mText = (TextView) layout.findViewById(R.id.txtprogress_text);

		int systembrightness = getSystemdefaultBrignhtness();
		int p = systembrightness * 100 / 255;

		mSeekBar.setProgress(p);
		mText.setText(p + "%");

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

				int tmpInt = arg1;
				mText.setText(tmpInt + "%");

				Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, tmpInt);
				tmpInt = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
				WindowManager.LayoutParams wl = ((Activity) mContext).getWindow().getAttributes();

				float tmpFloat = (float) tmpInt / 100;

				if (tmpFloat > 0 && tmpFloat <= 1) {
					wl.screenBrightness = tmpFloat;
				}

				((Activity) mContext).getWindow().setAttributes(wl);
			}
		});

	}

	private void SaveSystemdefaultBrignhtness(Context mContext) {

		SharedPreferences sharedPreferences = mContext.getSharedPreferences("SYSTEM_BRIGHTNESS", Context.MODE_PRIVATE);
		sharedPreferences.edit().putInt("brightness_value", getScreenBrightness(mContext)).commit();

	}

	public int getSystemdefaultBrignhtness() {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("SYSTEM_BRIGHTNESS", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("brightness_value", 30);
	}

	public int getScreenBrightness(Context activity) {
		int value = 0;
		ContentResolver cr = activity.getContentResolver();
		try {
			value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {

		}

		return value;
	}

	public void setBrightness(int progress) {
		mSeekBar.setProgress(progress);
	}

}
