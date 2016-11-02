package com.hw.data;

import com.google.gson.Gson;
import com.hw.readermain.Book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 黄威
 * 2016年10月23日下午2:24:22
 * 主页：http://blog.csdn.net/u014614038
 */
public class BookMsgDB extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "hwreaderdb";
	public static final String TABLE_NAME = "bookmessage";

	public static final String BOOK_name = "bookname";
	public static final String BOOK_Hashname = "bookhashname";
	public static final String BOOK_Msg = "bookmessage";
	
private final String sql = "create table if not exists " + TABLE_NAME + " (id integer primary key autoincrement," + BOOK_name
			+ " varchar(50)," + BOOK_Hashname + " varchar(100)," + BOOK_Msg + " varchar(500))";

	public BookMsgDB(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public BookMsgDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}
	
	public void CreateTable() {
		getWritableDatabase().execSQL(sql);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void closeTable() {
		close();
	}

	public void DelectTable() {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		getWritableDatabase().execSQL(sql);
	}

	private void inserBook(String bookname, String bookhashname, String bookmsg) {
		String sql = " insert into " + TABLE_NAME + " (" + BOOK_name + "," + BOOK_Hashname + "," + BOOK_Msg
				+ ") values ('" + bookname + "','" + bookhashname + "','" + bookmsg + "')";
		getWritableDatabase().execSQL(sql);
	}

	public void inserBook(String bookname, String bookhashname, Book bookmsg) {
		Gson gson = new Gson();
		inserBook(bookname, bookhashname, gson.toJson(bookmsg));
	}

	public void inserBook(Book bookmsg) {
		if(IsHasData(BOOK_Hashname, bookmsg.BOOKHashName)){
			delete(BOOK_Hashname, bookmsg.BOOKHashName);
		}
		Gson gson = new Gson();
		inserBook(bookmsg.BookName, bookmsg.BOOKHashName, gson.toJson(bookmsg));
	}

	/**
	 * 找不到时返回null
	 * 
	 * @param bookhashname
	 */
	public Book getBook(String bookhashname) {
		if (IsHasData(BOOK_Hashname, bookhashname)) {
			Cursor cursor = getCursorByKeyValue(BOOK_Hashname, bookhashname);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int index = cursor.getColumnIndex(BOOK_Msg);
				String BookMsg = cursor.getString(index);
				try {
					Gson gson = new Gson();
					Book book = gson.fromJson(BookMsg, Book.class);
					return book;
				} catch (Exception e) {

					e.printStackTrace();
				}finally {
					cursor.close();
				}
			}
		}

		return null;
	}

	public void deleteBookByHashName(String bookhasnmane) {
		if (IsHasData(BOOK_Hashname, bookhasnmane)) {
			delete(BOOK_Hashname, bookhasnmane);
		}

	}

	private Boolean IsHasData(String key, String value) {
		Cursor cursor = getCursorByKeyValue(key, value);
		if (cursor != null && cursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	private void delete(String key, String value) {
		String sql = "delete from " + TABLE_NAME + " where " + key + " = " + "'"+value+ "'";
		getWritableDatabase().execSQL(sql);
	}

	private Cursor getCursorByKeyValue(String key, String value) {
		Cursor cursor = getWritableDatabase().rawQuery("select * from " + TABLE_NAME + " where " + key + " = " +  "'"+value+ "'",
				null);
		return cursor;
	}
}
