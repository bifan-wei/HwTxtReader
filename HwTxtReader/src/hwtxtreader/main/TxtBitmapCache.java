package hwtxtreader.main;

import android.graphics.Bitmap;

public class TxtBitmapCache {
	private Bitmap pagebitmap;
	private Bitmap prebitmap;
	private Bitmap midbitmap;
	private Bitmap nextbitmap;

	public Bitmap getPagebitmap() {
		
		return pagebitmap;
	}

	public void setPagebitmap(Bitmap pagebitmap) {
		this.pagebitmap = pagebitmap;
	}

	public Bitmap getPrebitmap() {
		return prebitmap;
	}

	public void setPrebitmap(Bitmap prebitmap) {
		this.prebitmap = prebitmap;
	}

	public Bitmap getMidbitmap() {
		return midbitmap;
	}

	public void setMidbitmap(Bitmap midbitmap) {
		this.midbitmap = midbitmap;
	}

	public Bitmap getNextbitmap() {
		return nextbitmap;
	}

	public void setNextbitmap(Bitmap nextbitmap) {
		this.nextbitmap = nextbitmap;
	}
	
	

}
