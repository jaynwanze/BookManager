package com.example.bookmanager.db.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    // define constants related to DB schema such as DB name
    public static final String DATABASE_NAME = "BookManagerDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_BOOK="Book";
    public static final String COL_ID= "ID";
    public static final String COL_USER_ID="UserID";
    public static final String COL_TITLE="Title";
    public static final String COL_AUTHOR="Author";
    public static final String COL_CATEGORY="Category";
    public static final String COL_START_DATE="StartDate";
    public static final String COL_REVIEW="Review";
    public static final String COL_STATUS="Status";
    public static final String TABLE_USER="User";
    public static final String COL_EMAIL="Email";
    public static final String COL_PASSWORD="Password";
    public static final String COL_DOB="DOB";
    public static final String COL_NAME="Name";

    // the constructor of this class must call the super class(i.e.
//SQLiteHelper)
    public DBHandler( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
// DB is created and onCreate or onUpgrade is called depends on
//version
    }
    public void onCreate( SQLiteDatabase db ) {
    // build sql create statements to create tables in DB
        String userSqlStatement = "CREATE TABLE " + TABLE_USER +" ("+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT COLLATE NOCASE, " +
                COL_PASSWORD + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_DOB + " TEXT)";
        String bookSqlStatement =  "CREATE TABLE " + TABLE_BOOK + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER REFERENCES " + TABLE_USER + "(" + COL_ID + "), " +
                COL_TITLE + " TEXT, " +
                COL_AUTHOR + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_START_DATE + " TEXT, " +
                COL_REVIEW + " TEXT, " +
                COL_STATUS + " TEXT)";

        db.execSQL(userSqlStatement);
        db.execSQL(bookSqlStatement);


}
    public void onUpgrade( SQLiteDatabase db,
                           int oldVersion, int newVersion ) {
// Drop old table if it exists and create new tables,
//or alter table
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        Log.d("DBHandler", "Database opened for writing");
        return db;
    }

    @Override
    public synchronized void close() {
        super.close();
        Log.d("DBHandler", "Database closed");
    }
}
