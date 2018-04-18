package com.bifan.txtreaderlib.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bifan.txtreaderlib.bean.FileReadRecordBean;

import java.util.ArrayList;
import java.util.List;

public class FileReadRecordDB extends SQLiteOpenHelper {
    public static final int Version_1 = 1;
    public static final String DB_NAME = "hwTxtReader";
    protected String TABLE_NAME = "FileReadRecord";

    public static final String FileHashName = "fileHashName";
    public static final String SearchId = "searchId";
    public static final String FilePath = "filePath";
    public static final String FileName = "fileName";
    public static final String ParagraphIndex = "paragraphIndex";
    public static final String ChartIndex = "chartIndex";

    protected String sql = "create table if not exists " + TABLE_NAME + " (" + SearchId
            + " integer primary key autoincrement," + FileHashName + " varchar(50),"
            + FilePath + " varchar(100), " + FileName + " varchar(100)," + ParagraphIndex + " integer, " + ChartIndex + " integer)";

    public FileReadRecordDB(Context context) {
        this(context, DB_NAME, null, Version_1);
    }

    public FileReadRecordDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void createTable() {
        getWritableDatabase().execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 关闭数据表
     */
    public void closeTable() {
        close();
    }

    /**
     * 删除数据表
     */
    public void delectTable() {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        getWritableDatabase().execSQL(sql);
    }

    public void insertData(String fileHashName, String path, String fileName, int paragraphIndex, int chartIndex) {
        if (!TextUtils.isEmpty(FileHashName)) {
            if (IsHasData(FileHashName, fileHashName)) {//已经存在了，先删除
                deleteRecordsByFileHashName(fileHashName);
            }
            String sql = " insert into " + TABLE_NAME + " (" + FileHashName + "," + FilePath + "," + FileName + "," + ParagraphIndex + "," + ChartIndex + ") values ('" + fileHashName
                    + "','" + path + "','" + fileName + "','" + paragraphIndex + "','" + chartIndex + "')";
            getWritableDatabase().execSQL(sql);
        }
    }

    /**
     * @param recordBean 确保userId不为null，为null的话，不做插入处理
     */
    public void insertData(@NonNull FileReadRecordBean recordBean) {
        insertData(
                recordBean.fileHashName
                , recordBean.filePath
                , recordBean.fileName
                , recordBean.paragraphIndex
                , recordBean.chartIndex);
    }

    /**
     * @param fileHashName
     * @return 如果没有记录，返回null
     * --------------------
     */
    public FileReadRecordBean getRecordByHashName(String fileHashName) {
        if (TextUtils.isEmpty(fileHashName) || !IsHasData(FileHashName, fileHashName))
            return null;
        Cursor cursor = getCursorByKeyValue(FileHashName, fileHashName);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            FileReadRecordBean bean = getRecordBeanFromCursor(cursor);
            return bean;
        }
        return null;
    }

    /**
     * @param userId
     * @return 如果userId 为空，不分用户返回的是所有下载的数据,否则返回指定用户的下载记录,不会返回空
     */
    public List<FileReadRecordBean> getDownloadRecord(String userId) {
        List<FileReadRecordBean> searchBeans = new ArrayList<>();
        Cursor cursor;
        if (TextUtils.isEmpty(userId)) {
            cursor = getWritableDatabase().rawQuery("select * from " + TABLE_NAME, null);
        } else {
            cursor = getCursorByKeyValue(FileHashName, userId);
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                searchBeans.add(getRecordBeanFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return searchBeans;

    }

    /**
     * @return 获取所有下载记录
     */
    public List<FileReadRecordBean> getAllFileRecord() {
        return getDownloadRecord(null);
    }

    /**
     * @param cursor
     * @return
     */
    private FileReadRecordBean getRecordBeanFromCursor(Cursor cursor) {
        FileReadRecordBean bean = new FileReadRecordBean();
        int userIdIndex = cursor.getColumnIndex(FileHashName);
        bean.fileHashName = cursor.getString(userIdIndex);

        int searchIdIndex = cursor.getColumnIndex(SearchId);
        bean.id = cursor.getInt(searchIdIndex);

        int CachePathIndex = cursor.getColumnIndex(FilePath);
        bean.filePath = cursor.getString(CachePathIndex);

        int fileNameIndex = cursor.getColumnIndex(FileName);
        bean.fileName = cursor.getString(fileNameIndex);


        int paragraphIndex = cursor.getColumnIndex(ParagraphIndex);
        bean.paragraphIndex = cursor.getInt(paragraphIndex);

        int chartIndex = cursor.getColumnIndex(ChartIndex);
        bean.chartIndex = cursor.getInt(chartIndex);

        return bean;
    }



    /**
     * @param recordBean recordBean  删除searchId的搜索记录
     */
    public void deleteOneRecordById(FileReadRecordBean recordBean) {
        if (IsHasData(SearchId, recordBean.id + "")) {
            delete(SearchId, recordBean.id + "");
        }

    }

    /**
     * @param fileId 根据fileId删除记录
     */
    public void deleteRecordsByFileHashName(String fileId) {
        if (IsHasData(FileHashName, fileId + "")) {
            delete(FileHashName, fileId + "");
        }

    }

    /**
     * @param key
     * @param value
     * @return 是否有数据
     */
    private Boolean IsHasData(String key, String value) {
        Cursor cursor = getCursorByKeyValue(key, value);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @param key
     * @param value
     */
    private void delete(String key, String value) {
        String sql = "delete from " + TABLE_NAME + " where " + key + " = " + "'" + value + "'";
        getWritableDatabase().execSQL(sql);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    private Cursor getCursorByKeyValue(String key, String value) {
        Cursor cursor = getWritableDatabase()
                .rawQuery("select * from " + TABLE_NAME + " where " + key + " = " + "'" + value + "'", null);
        return cursor;
    }

}
