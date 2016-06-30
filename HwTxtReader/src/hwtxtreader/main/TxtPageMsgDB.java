package hwtxtreader.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import hwtxtreader.bean.Page;

/**
 * @author huangwei
 * 2016下午5:17:58
 * 主页：http://blog.csdn.net/u014614038
 * 
 */
public class TxtPageMsgDB extends SQLiteOpenHelper {
	public static final String DB_NAME = "hwreadertxtpagedb";
	public static final String TABLE_NAME = "hwreadertxt";
	public static final String firstElementCharindex = "firstElementCharindex";
	public static final String firstElementParagraphIndex = "firstElementParagraphIndex";
	public static final String lastElementCharindex="lastElementCharindex";
	public static final String lastElementParagraphIndex ="lastElementParagraphIndex";
	private final String sql = "create table " + TABLE_NAME + " (id integer primary key autoincrement," + firstElementCharindex
			+ " integer," + firstElementParagraphIndex + " integer," + lastElementCharindex + " integer," + lastElementParagraphIndex + " integer)";

	public TxtPageMsgDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void CreateTable() {
		getWritableDatabase().execSQL(sql);
	}

	public void closeTable() {
		close();
	}

	public void DelectTable() {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		getWritableDatabase().execSQL(sql);
	}

	public void savePage(int firstCharindex, int firstpindex,int lastcharindex,int lastpindex ) {
		String sql = " insert into " + TABLE_NAME + " (" + firstElementCharindex + "," + firstElementParagraphIndex +"," + lastElementCharindex+"," + lastElementParagraphIndex +") values ('" + firstCharindex
				+ "','" + firstpindex + "','"+lastcharindex+"','"+lastpindex+"')";
		getWritableDatabase().execSQL(sql);
	}

	/**通过页数返回页相关数据，如果查找不到返回空
	 * @param index 页数下标是从1开始的
	 * @return
	 */
	public Page getPageByInedx(int index) {
		Page pageEntity = new Page();

		Cursor cursor = getWritableDatabase().rawQuery("select * from " + TABLE_NAME + " where id =" + index, null);
		cursor.moveToFirst();
		if (index >= 1 && cursor.getCount() > 0) {
			int firstcharindexcoumn = cursor.getColumnIndex(firstElementCharindex);
			int firstpindexcoumn = cursor.getColumnIndex(firstElementParagraphIndex);
			int lastcharindexcoumn = cursor.getColumnIndex(lastElementCharindex);
			int lastpindexcoumn = cursor.getColumnIndex(lastElementParagraphIndex);
			
			int  firstcharindex = cursor.getInt(firstcharindexcoumn);
			int firstpindex = cursor.getInt(firstpindexcoumn);
			int  lastcharindex = cursor.getInt(lastcharindexcoumn);
			int lastpindex = cursor.getInt(lastpindexcoumn);

			cursor.close();
			pageEntity.firstElementCharindex = firstcharindex;
			pageEntity.firstElementParagraphIndex = firstpindex;
			pageEntity.lastElementCharindex = lastcharindex;
			pageEntity.lastElementParagraphIndex = lastpindex;
			pageEntity.setPageindex(index);
			return pageEntity;
		}
		return null;
	}

	public int getLastPageIndex() {

		Cursor cursor = getWritableDatabase().rawQuery("select id from " + TABLE_NAME, null);

		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			int idcoumn = cursor.getColumnIndex("id");

			int index = cursor.getInt(idcoumn);
			return index;
		}
		cursor.close();

		return 0;
	}

}
