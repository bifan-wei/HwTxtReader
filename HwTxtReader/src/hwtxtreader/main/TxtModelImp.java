package hwtxtreader.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import hwtxtreader.bean.Page;
import hwtxtreader.bean.TxtErrorCode;
import hwtxtreader.bean.Txterror;
import hwtxtreader.pipeline.TxtPipeline;
import hwtxtreader.pipeline.TxtPipelineImp;
import hwtxtreader.utils.BitmapUtil;
import hwtxtreader.utils.FileCharsetDetector;

public class TxtModelImp implements TxtModel {
	private TxtManager txtManager;
	private TxtPipeline txtPipeline;
	private TxtBitmapCache bitmapCache;
	private TxtPageMsgDB mTxtPageMsgDB;
	private File txtfile;
	private Context context;
	private Page prepage;
	private Page midpage;
	private Page nextpage;
	private Boolean pausesaparatethread = false;
	private Boolean stopsaparatethread = false;
	private Boolean saparatedone = false;
	private Map<Integer, Page> buffercaches;
	private int pagenums = -1;
	private ModeToViewTransform modeToViewTransform;

	public TxtModelImp(Context context, TxtManager txtManager, String txtfilepath) {
		this.context = context;
		this.txtManager = txtManager;
		Txterror txterror = new Txterror();
		txterror.txterrorcode = TxtErrorCode.LOAD_BOOK_EXCEPTION;
		txtManager.setModeTransform(new ManagerToModelTransformImp());

		try {
			this.txtfile = new File(txtfilepath);
		} catch (Exception e) {
			e.printStackTrace();
			txterror.message = "文件路径为空";
			txtManager.onTxtLoaderror(txterror);
			return;
		}

		if (!this.txtfile.exists()) {
			txterror.message = "文件不存在";
			txtManager.onTxtLoaderror(txterror);
			Log.e("==============", "文件不存在");
			return;
		}

		txtPipeline = new TxtPipelineImp();
		bitmapCache = new TxtBitmapCache();
		buffercaches = new HashMap<>();
		refrespageBitmap();

	}

	private void refrespageBitmap() {

		bitmapCache.setPagebitmap(hwtxtreader.utils.BitmapUtil.CreateBitmapWitThisBg(context.getResources(),
				txtManager.getViewConfig().getBackBroundColor(), txtManager.getViewWith(), txtManager.getViewHeigh()));

	}

	@Override
	public void LoadTxtFile(Transformer t) throws FileNotFoundException, IOException {
		String charset = getchartset();
		txtPipeline.LoadTxtFile(txtfile, charset, t);
	}

	private String getchartset() throws FileNotFoundException, IOException {
		String chartset = new FileCharsetDetector().guessFileEncoding(txtfile);
		if (chartset.contains("windows") || chartset.contains("WINDOWS")) {
			chartset = "unicode";
		}
		return chartset;
	}

	private Bitmap getpageBitmap() {

		if (bitmapCache.getPagebitmap() == null) {
			bitmapCache.setPagebitmap(BitmapUtil.CreateBitmapWitThisBg(context.getResources(),
					txtManager.getViewConfig().getBackBroundColor(), txtManager.getViewWith(),
					txtManager.getViewHeigh()));
		}

		return bitmapCache.getPagebitmap();
	}

	@Override
	public void loadFirstPage() {
		loadFromChar(0, -1, 1);
		if (prepage == null) {
			modeToViewTransform.onloadfirstpage(true);
		} else if (midpage == null) {
			midpage = prepage;
			getBitmapCache().setMidbitmap(getBitmapCache().getPrebitmap());
			modeToViewTransform.onloadfirstpage(true);
		} else {
			nextpage = midpage;
			midpage = prepage;
			getBitmapCache().setNextbitmap(getBitmapCache().getMidbitmap());
			getBitmapCache().setMidbitmap(getBitmapCache().getPrebitmap());
			modeToViewTransform.onloadfirstpage(false);
		}
	}

	@Override
	public void loadnextpage() {

		prepage = midpage;
		midpage = nextpage;
		Boolean islastpage = false;

		int nextpageindex = midpage.getPageindex() + 1;

		if (midpage != null && !PageinNodataState(midpage)) {
			if (buffercaches.containsKey(nextpageindex)) {
				nextpage = buffercaches.get(nextpageindex);
			} else {
				nextpage = getPageFromPosition(midpage.lastElementParagraphIndex, midpage.lastElementCharindex,
						txtManager.getTextPaint(), txtManager.getViewWith(), txtManager.getLinesNums());
			}
			if (nextpage != null && !PageinNodataState(nextpage)) {
				nextpage.setPageindex(nextpageindex);

			} else {
				islastpage = true;
			}
		} else {

			islastpage = true;
		}
		savenextBitmapCache();
		modeToViewTransform.onloadnextpage(islastpage);

	}

	@Override
	public void loadprepage() {

		nextpage = midpage;
		midpage = prepage;
		int prepageindex = midpage.getPageindex() - 1;
		if (buffercaches.containsKey(prepageindex)) {
			prepage = buffercaches.get(prepageindex);
		} else {
			prepage = getPage(prepageindex);
		}

		Boolean isfirstpage = false;

		if (prepage == null) {
			prepage = new Page();
			prepage.setIstheFirstpage(true);
			isfirstpage = true;
		}
		prepage.setPageindex(prepageindex);
		savepreBitmapCache();
		modeToViewTransform.onloadprepage(isfirstpage);

	}

	@Override
	public void loadpage(int pageindex) {

		if (pageindex > 0 && pageindex <= pagenums) {

			if (pageindex == 1) {
				loadFirstPage();
				return;
			}

			Page p = mTxtPageMsgDB.getPageByInedx(pageindex - 1);

			if (p != null) {
				loadFromChar(p.firstElementParagraphIndex, p.firstElementCharindex, pageindex - 1);
				if (pageindex >= pagenums) {

					modeToViewTransform.onloadpagefromindex(false, true);
					return;
				}

				modeToViewTransform.onloadpagefromindex(false, false);
			}

		}
	}

	private void savenextBitmapCache() {
		bitmapCache.setPrebitmap(bitmapCache.getMidbitmap());
		bitmapCache.setMidbitmap(bitmapCache.getNextbitmap());
		bitmapCache.setNextbitmap(getBitmapFrompagedata(nextpage));

	}

	@Override
	public void loadFromChar(int paragraphindex, int charindex, int markpageindex) {

		prepage = getPageFromPosition(paragraphindex, charindex, txtManager.getTextPaint(), txtManager.getViewWith(),
				txtManager.getLinesNums());

		if (prepage == null) {
			midpage = null;
			nextpage = null;
			return;
		}
		prepage.setPageindex(markpageindex);
		midpage = getPageFromPosition(prepage.lastElementParagraphIndex, prepage.lastElementCharindex,
				txtManager.getTextPaint(), txtManager.getViewWith(), txtManager.getLinesNums());

		if (midpage == null) {
			nextpage = null;
			return;
		}

		midpage.setPageindex(prepage.getPageindex() + 1);
		midpage.setIstheFirstpage(false);
		nextpage = getPageFromPosition(midpage.lastElementParagraphIndex, midpage.lastElementCharindex,
				txtManager.getTextPaint(), txtManager.getViewWith(), txtManager.getLinesNums());
		if (nextpage != null) {
			nextpage.setPageindex(midpage.getPageindex() + 1);
		}
		saveBitmapCache();

	}

	private void savepreBitmapCache() {
		bitmapCache.setNextbitmap(bitmapCache.getMidbitmap());
		bitmapCache.setMidbitmap(bitmapCache.getPrebitmap());
		bitmapCache.setPrebitmap(getBitmapFrompagedata(prepage));

	}

	private void saveBitmapCache() {

		bitmapCache.setPrebitmap(getBitmapFrompagedata(prepage));
		bitmapCache.setMidbitmap(getBitmapFrompagedata(midpage));
		bitmapCache.setNextbitmap(getBitmapFrompagedata(nextpage));

	}

	private Boolean PageinNodataState(Page p) {
		return p.getLinesdata() == null || p.getLinesdata().size() == 0;

	}

	/**
	 * @param pindexi
	 * @param cindex
	 * @param textPaint
	 * @param viewWidth
	 * @param linesNums
	 * @return 如果传入的开始段落位置超出了数据源本身，返回的是null
	 */
	private Page getPageFromPosition(int pindexi, int cindex, Paint textPaint, int viewWidth, int linesNums) {
		int linewidth = viewWidth - txtManager.getViewConfig().getPaddingleft()
				- txtManager.getViewConfig().getPaddingright();

		return txtPipeline.getPageFromPosition(pindexi, cindex, textPaint, linewidth, linesNums);
	}

	private Bitmap getBitmapFrompagedata(Page page) {
		if (page == null || page.getLinesdata() == null)
			return null;
		return BitmapUtil.getPageBitmapWithLinse(page.getPageindex(), pagenums, page.getLinesdata(), txtManager,
				getpageBitmap());
	}

	@Override
	public Page getPage(int pageindex) {

		if (mTxtPageMsgDB != null) {
			Page p = mTxtPageMsgDB.getPageByInedx(pageindex);
			if (p != null) {
				p = getPageFromPosition(p.firstElementParagraphIndex, p.firstElementCharindex,
						txtManager.getTextPaint(), txtManager.getViewWith(), txtManager.getLinesNums());
				return p;
			}
		}

		return null;
	}

	@Override
	public TxtBitmapCache getBitmapCache() {

		return bitmapCache;
	}

	@Override
	public void setModeToViewTransform(Transformer t) {
		modeToViewTransform = (ModeToViewTransform) t;

	}

	@Override
	public void separatebooktopages() {
		stopsaparatethread = true;
		pausesaparatethread = false; 
		if (mTxtPageMsgDB == null) {
			mTxtPageMsgDB = new TxtPageMsgDB(context, TxtPageMsgDB.DB_NAME, null, 1);
		}
		synchronized (mTxtPageMsgDB) {

			pausesaparatethread = false;
			mTxtPageMsgDB.DelectTable();
			mTxtPageMsgDB.CreateTable();
			stopsaparatethread = false;
			pagenums = -1;
			final int paragraphsize = txtPipeline.getParagraphCace().getParagraphSize();
			if (paragraphsize == 0) {
				return;
			}

			new Thread(new Runnable() {

				int lastcharindex = -1;
				int lastpindex = 0; 

				@Override
				public void run() {
					saparatedone = false;
					modeToViewTransform.onPageSeparateStart();
					int index = 1;
					while (!stopsaparatethread && lastpindex < paragraphsize) {
						if (!pausesaparatethread) {
							Page page = getPageFromPosition(lastpindex, lastcharindex, txtManager.getTextPaint(),
									txtManager.getViewWith(), txtManager.getLinesNums());
							lastcharindex = page.lastElementCharindex;
							lastpindex = page.lastElementParagraphIndex;

							if (index < 100) {
								buffercaches.put(index++, page);
							}

							if (lastpindex < paragraphsize && !stopsaparatethread) {
								mTxtPageMsgDB.savePage(page.firstElementCharindex, page.firstElementParagraphIndex,
										page.lastElementCharindex, page.lastElementParagraphIndex);
							}

						}
					}

					pagenums = mTxtPageMsgDB.getLastPageIndex();
					saparatedone = true;
					modeToViewTransform.onPageSeparateDone();

				}
			}).start();
		}
	}

	private class ManagerToModelTransformImp implements ManagerToModelTransform {

		@Override
		public void PostResult(Boolean t) {

		}

		@Override
		public void loadTxtbook(final Transformer tt) {
			final Transformer t1 = new Transformer() {

				@Override
				public void PostResult(Boolean t) {
					tt.PostResult(t);

					if (t) {

						if (txtPipeline.HasCaChedata()) {
							loadFirstPage();// 加载开始页
							separatebooktopages();// 进行分页
							// 加载成功，开始获取显示
							modeToViewTransform.ReFreshView();
						} else {

							modeToViewTransform.onNoData();
						}
					} else {

						modeToViewTransform.onloadFileException();
					}
				}

				@Override
				public void PostError(Txterror txterror) {
					tt.PostError(txterror);
				}
			};

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						LoadTxtFile(t1);
					} catch (Exception e) {

						t1.PostResult(false);
					}
				}
			}).start();

		}

		@Override
		public void jumptopage(int pageindex) {
			loadpage(pageindex);
		}

		@Override
		public void PostError(Txterror txterror) {

		}

		@Override
		public void separatepage() {
			separatebooktopages();
		}

		@Override
		public void refreshbitmaptext() {
			if(modeToViewTransform!=null){
				saveBitmapCache();
				modeToViewTransform.ReFreshView();
			}
		} 

		@Override
		public void refreshbitmapbackground() {
			if(modeToViewTransform!=null){
				refrespageBitmap();
				saveBitmapCache();
				modeToViewTransform.ReFreshView();
			}
		}

	}

	@Override
	public Page getPrePage() {

		return prepage;
	}

	@Override
	public Page getMidPage() {

		return midpage;
	}

	@Override
	public Page getNextPage() {

		return nextpage;
	}

	@Override
	public int getPageNums() {

		return pagenums;
	}

	@Override
	public Boolean issaparatedone() {

		return saparatedone;
	}

}
