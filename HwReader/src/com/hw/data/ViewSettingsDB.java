package com.hw.data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hw.readermain.ReaderViewSetting;
import com.hw.txtreader.TxtReaderViewSettings;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class ViewSettingsDB extends SQLiteOpenHelper {

	public static final String DB_NAME = "hwreaderdb";
	public static final String TABLE_NAME = "viewsettingsmessage";

	public static final String BOOK_name = "bookname";
	public static final String BOOK_Hashname = "bookhashname";
	public static final String SettingMsg = "booksettingmessage";

	private final String sql = "create table if not exists " + TABLE_NAME + " (id integer primary key autoincrement,"
			+ BOOK_name + " varchar(50)," + BOOK_Hashname + " varchar(100)," + SettingMsg + " varchar(500))";

	public ViewSettingsDB(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public ViewSettingsDB(Context context, String name, CursorFactory factory, int version) {
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

	private void inserSetting(String bookname, String bookhashname, String bookmsg) {
		String sql = " insert into " + TABLE_NAME + " (" + BOOK_name + "," + BOOK_Hashname + "," + SettingMsg
				+ ") values ('" + bookname + "','" + bookhashname + "','" + bookmsg + "')";
		getWritableDatabase().execSQL(sql);
	}

	public void inserSetting(String bookname, String bookhashname, ReaderViewSetting viewsettings) {

		if (IsHasData(BOOK_Hashname, bookhashname)) {
			delete(BOOK_Hashname, bookhashname);
		}

		Gson gson = new Gson();

		inserSetting(bookname, bookhashname, gson.toJson(viewsettings));
		Log.e("inserSetting", gson.toJson(viewsettings));
	}

	/**
	 * 找不到时返回null
	 * 
	 * @param bookhashname
	 */
	public String getViewSettingMsg(String bookhashname) {
		if (IsHasData(BOOK_Hashname, bookhashname)) {
			Cursor cursor = getCursorByKeyValue(BOOK_Hashname, bookhashname);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int index = cursor.getColumnIndex(SettingMsg);
				String BookMsg = cursor.getString(index);
				try {

					return BookMsg;
				} catch (Exception e) {

					e.printStackTrace();
				} finally {
					cursor.close();
				}
			}
		}

		return null;
	}

	public TxtReaderViewSettings getTxtViewSettingMsg(String bookhashname) {
		String bookmsg = getViewSettingMsg(bookhashname);
		if (bookmsg != null) {
			Gson gson = new Gson();
			try {				
				return gson.fromJson(bookmsg, TxtReaderViewSettings.class);
			} catch (JsonSyntaxException e) {

				e.printStackTrace();
			}

		}
		return null;
	}

	public void deleteSettingsByHashName(String bookhasnmane) {
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
		String sql = "delete from " + TABLE_NAME + " where " + key + " = " + "'" + value + "'";
		getWritableDatabase().execSQL(sql);
	}

	private Cursor getCursorByKeyValue(String key, String value) {

		Cursor cursor = getWritableDatabase()
				.rawQuery("select * from " + TABLE_NAME + " where " + key + " = " + "'" + value + "'", null);
		return cursor;
	}
}
