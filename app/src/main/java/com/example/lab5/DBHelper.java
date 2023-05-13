package com.example.lab5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "accounts.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + Contract.GuestEntry.TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + Contract.GuestEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + Contract.GuestEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + Contract.GuestEntry.COLUMN_ADDRESS + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Оновлення з версії " + oldVersion + " на версію " + newVersion);
        String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + Contract.GuestEntry.TABLE_NAME;
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }


}
