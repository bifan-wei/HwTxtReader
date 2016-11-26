package com.hw.hwreaderui;

import com.example.hwreaderui.R;
import com.hw.action.Action;
import com.hw.action.ReaderProgressSaveAction;
import com.hw.action.TxtPageTypesettingChangeToVerticalAction;
import com.hw.action.TxtPageTypesettingChangeTohorizontalAction;
import com.hw.action.TxtProgressAction;
import com.hw.action.TxtStyleChangeAction;
import com.hw.action.TxtTextSizeAddAction;
import com.hw.action.TxtTextSizeReduceAction;
import com.hw.action.TxtTextSortChageAction;
import com.hw.action.ViewSettingSaveAction;
import com.hw.beans.ReaderStyle;
import com.hw.beans.ReaderStyle.Style;
import com.hw.menus.BrightMenu;
import com.hw.menus.CenterAreaTouchListsner;
import com.hw.menus.ProgressMenu;
import com.hw.menus.ProgressMenu.onProgressChangeListener;
import com.hw.menus.ReaderLayerEvenProcessor;
import com.hw.menus.ReaderViewMenu;
import com.hw.menus.ReaderViewMenu.TxtMenuClockListener;
import com.hw.menus.StyleMenu;
import com.hw.menus.StyleMenu.onTxtStyleChangeListener;
import com.hw.menus.TextMenu;
import com.hw.menus.TextMenu.onTxtTextChangeListener;
import com.hw.readermain.Book;
import com.hw.readermain.BookProcessor;
import com.hw.readermain.CallBack;
import com.hw.readermain.ReaderException;
import com.hw.readermain.ReaderException.Type;
import com.hw.readermain.ReaderView;
import com.hw.txtreader.ITxtPageChangeListener;
import com.hw.txtreader.TxtFileInitProcessor;
import com.hw.txtreader.TxtReaderContex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 黄威 2016年11月1日上午10:18:29 主页：http://blog.csdn.net/u014614038
 */
public class HwReaderPlayActivity extends Activity {
	// view
	public ReaderView readerview;
	private RelativeLayout mLoadingView;
	private RelativeLayout mNodataView;
	private RelativeLayout mTitlebar;
	private LinearLayout mToucherLayer;
	private TextView mTitle, mNodataText, mLoadingMsg;
	private ReaderViewMenu mReaderViewMenu;
	private TextMenu mTextMenu;
	private StyleMenu mStyleMenu;
	private BrightMenu mBrightMenu;
	private ProgressMenu mProgressMenu;
	// data
	final TxtReaderContex readerContex = new TxtReaderContex();
	private Handler mHander;
	private String mBookPath;
	private String mBookName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hw_reader_play);
		getMyIntent();
		findview();
		initdata();
		registListener();
		loadBook();

	}

	private void getMyIntent() {
		Intent intent = getIntent();
		mBookName = intent.getStringExtra("bookname");
		mBookPath = intent.getStringExtra("bookpath");
	}

	private void registListener() {

		final ReaderLayerEvenProcessor layerEvenProcessor = new ReaderLayerEvenProcessor(readerContex,
				new CenterAreaTouchListsner() {

					@Override
					public void onOutSideTouch() {
						hideReaderMenu();
					}

					@Override
					public void onCenterTouch() {
						showMenu();
					}
				});

		mToucherLayer.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return layerEvenProcessor.Process(event);
			}
		});

		mNodataView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadBook();
			}
		});

		readerContex.setOnTxtPageChangeListener(new ITxtPageChangeListener() {

			@Override
			public void onPageChange(int currentpageindex, int pageunms) {
				mProgressMenu.setPageIndex(currentpageindex, pageunms);
			}
		});

	}

	private void loadBook() {
		ShowLoadingView();
		new Thread(new Runnable() {
			public void run() {

				Book book = new Book();
				// book.BookPath = Environment.getExternalStorageDirectory() +
				// "/test5.txt";
				book.BookPath = mBookPath;
				book.BookName = mBookName;
				readerContex.mBook = book;
				BookProcessor<Type> processor = new TxtFileInitProcessor();

				processor.Process(new CallBack<ReaderException.Type>() {

					@Override
					public void onBack(Type t) {
						if (t == Type.sucess) {
							readerview.postInvalidate();
							ShowReaderView();
							initMenu();
						} else {
							ShowMessageView(ReaderException.getExceptionMsg(t) + ",点击重新加载");
						}

					}

				}, readerContex);

			}
		}).start();
	}

	private void initdata() {
		mHander = new Handler();
		readerview.setReaderContex(readerContex);
		readerContex.setReaderView(readerview);
		readerContex.mContext = HwReaderPlayActivity.this;
		readerContex.AddAction(new TxtTextSizeAddAction(readerContex));
		readerContex.AddAction(new TxtTextSizeReduceAction(readerContex));
		readerContex.AddAction(new TxtTextSortChageAction(readerContex));
		readerContex.AddAction(new ViewSettingSaveAction(readerContex));
		readerContex.AddAction(new ReaderProgressSaveAction(readerContex));
		readerContex.AddAction(new TxtProgressAction(readerContex));
		readerContex.AddAction(new TxtStyleChangeAction(readerContex));
		readerContex.AddAction(new TxtPageTypesettingChangeTohorizontalAction(readerContex));
		readerContex.AddAction(new TxtPageTypesettingChangeToVerticalAction(readerContex));

		ImageView imageView = (ImageView) findViewById(R.id.txtview_loading_img);
		imageView.setBackgroundResource(R.anim.hwreader_loading_annimation);
		final AnimationDrawable mAnimation = (AnimationDrawable) imageView.getBackground();
		imageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});

		
	}

	private void findview() {
		readerview = (ReaderView) findViewById(R.id.hwreadView);
		mTitlebar = (RelativeLayout) findViewById(R.id.hwreader_titlebar);
		mTitle = (TextView) findViewById(R.id.hwreader_title_textview);
		mLoadingView = (RelativeLayout) findViewById(R.id.hwreader__loadingview);
		mNodataView = (RelativeLayout) findViewById(R.id.hwreader_nodataview);
		mNodataText = (TextView) findViewById(R.id.txtview_nodata_text);
		mLoadingMsg = (TextView) findViewById(R.id.txtview_loading_msg);
		mToucherLayer = (LinearLayout) findViewById(R.id.hwreader_toucher_layer);
		mTitle.setText(mBookName);

	}

	private void initMenu() {
		runOnUi(new onUiRun() {

			@Override
			public void Run() {
				mReaderViewMenu = new ReaderViewMenu(readerContex.mContext);
				mTextMenu = new TextMenu(readerContex);
				mProgressMenu = new ProgressMenu(readerContex);
				mStyleMenu = new StyleMenu(readerContex);
				mBrightMenu = new BrightMenu(readerContex.mContext);
				registMenuListsner();
				mStyleMenu.init();
				readerContex.onPageChageListener();
			}
		});

	}

	private void registMenuListsner() {
		mReaderViewMenu.setOnTxtMenuClickListener(new TxtMenuClockListener() {

			@Override
			public void onTextMenuClicked() {
				showTextMenu();
			}

			@Override
			public void onStytleMenuClicked() {
				showStyleMenu();
			}

			@Override
			public void onProgressMenuClicked() {
				showProgressMenu();
			}

			@Override
			public void onLightMenuClicked() {
				showBrightMenu();

			}
		});

		mTextMenu.setonTxtTextChangeListener(new onTxtTextChangeListener() {

			@Override
			public void onTextSortChange(String textsort) {
				readerContex.mViewSetting.TexttTypeFile = textsort;
				readerContex.doAction(Action.changetextsort);
			}

			@Override
			public void onTextSizeAdd() {
				readerContex.doAction(Action.addtextsize);
			}

			@Override
			public void onTextSizeDec() {
				readerContex.doAction(Action.reducetextsize);
			}

			@Override
			public void onTextBold(Boolean isBold) {
				readerContex.mViewSetting.MakeBoldText = isBold;
				readerContex.doAction(Action.changetextsort);
			}
		});

		mProgressMenu.setonProgressChangeListener(new onProgressChangeListener() {

			@Override
			public void onProgressChange(int progress) {
				TxtProgressAction progressAction = (TxtProgressAction) readerContex.getAction(Action.progresschange);
				progressAction.setPageProgress(progress);
				readerContex.doAction(Action.progresschange);
			}
		});

		mStyleMenu.setonTxtStyleChangeListener(new onTxtStyleChangeListener() {

			@Override
			public void onStyleChange(ReaderStyle readerStyle, Style style) {
				readerContex.mViewSetting.setPageBackground(readerStyle);
				readerContex.doAction(Action.backgroundstylechanged);

				if (style == Style.ancientry2) {
					readerContex.doAction(Action.TypesettingChangetovertical);
				}
				readerContex.doAction(Action.saveviewsettings);
			}
		});
	}

	public void finishactivity(View v) {
		finish();

	}
	// ----------------------------------------------------------------------------------------//

	private void ShowMessageView(final String msg) {
		runOnUi(new onUiRun() {
			@Override
			public void Run() {
				mNodataText.setText(msg);
				mNodataView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				readerview.setVisibility(View.GONE);
				mToucherLayer.setVisibility(View.GONE);

			}
		});
	}

	private void ShowReaderView() {
		runOnUi(new onUiRun() {
			@Override
			public void Run() {
				mTitlebar.setVisibility(View.GONE);
				mNodataView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.GONE);
				readerview.setVisibility(View.VISIBLE);
				mToucherLayer.setVisibility(View.VISIBLE);
			}
		});
	}

	private void ShowLoadingView() {
		runOnUi(new onUiRun() {
			@Override
			public void Run() {
				mNodataView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.VISIBLE);
				readerview.setVisibility(View.GONE);
				mToucherLayer.setVisibility(View.GONE);

			}
		});
	}

	@SuppressWarnings("unused")
	private void setLoadingMsg(final String meesage) {
		runOnUi(new onUiRun() {

			@Override
			public void Run() {
				mLoadingMsg.setText(meesage);
			}
		});
	}

	private void showMenu() {

		mReaderViewMenu.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
		mTitlebar.setVisibility(View.VISIBLE);

	}

	private View getParentView() {
		return HwReaderPlayActivity.this.getWindow().getDecorView();
	}

	private void hideReaderMenu() {
		mReaderViewMenu.dismiss();
		mTitlebar.setVisibility(View.GONE);
	}

	private void showTextMenu() {
		hideReaderMenu();
		mTextMenu.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);
	}

	private void showProgressMenu() {
		hideReaderMenu();
		mProgressMenu.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);

	}

	private void showStyleMenu() {
		hideReaderMenu();
		mStyleMenu.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);

	}

	private void showBrightMenu() {
		hideReaderMenu();
		mBrightMenu.showAtLocation(getParentView(), Gravity.BOTTOM, 0, 0);

	}

	// -----------------------------------------------------------------------------------------//
	private void runOnUi(final onUiRun uirun) {
		getHander().post(new Runnable() {

			@Override
			public void run() {
				uirun.Run();
			}
		});
	}

	private Handler getHander() {
		return mHander;
	}

	private interface onUiRun {
		public void Run();
	}

	@Override
	protected void onDestroy() {
		if (readerContex != null) {
			readerContex.doAction(Action.saveviewsettings);
			readerContex.doAction(Action.savereaderprogress);
			if (readerContex.mCacheCenter != null)
				readerContex.mCacheCenter.clear();
			if (readerContex.mBitmapManager != null)
				readerContex.mBitmapManager.recycle();
		}
		super.onDestroy();
	}
}
