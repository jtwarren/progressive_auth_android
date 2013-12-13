package com.android.progressiveauthentication;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AuthTable {

  // Database table
  public static final String TABLE_AUTH = "authlevels";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PACKAGE = "package";
  public static final String COLUMN_LEVEL = "level";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_AUTH
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_PACKAGE + " text not null, " 
      + COLUMN_LEVEL + " integer not null default -1" 
      + ");";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(AuthTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTH);
    onCreate(database);
  }
} 
