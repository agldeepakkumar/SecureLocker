package com.securelocker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "secure.db";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String PATH = "path";
    public static final String TABLE_GALLERY = "table_gallery";
    public static final String CREATE_IMAGE_TABLE_QUERY = "CREATE TABLE "+TABLE_GALLERY + " ( "+COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+PATH + " TEXT );";

    public MySqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE_QUERY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY);
    }
}
