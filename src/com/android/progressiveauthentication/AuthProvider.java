package com.android.progressiveauthentication;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class AuthProvider extends ContentProvider{
	
	// used for the UriMacher
	private static final int AUTHS = 100;
	private static final int AUTH = 101;
	
	// database
	private AuthDatabaseHelper database;
	
	private static final String AUTHORITY = "com.android.progressiveauthentication.contentprovider";

	private static final String BASE_PATH = "auth";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/auth";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/authlevel";
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, AUTHS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", AUTH);
	  }

	  
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case AUTHS:
	      rowsDeleted = sqlDB.delete(AuthTable.TABLE_AUTH, selection,
	          selectionArgs);
	      break;
	    case AUTH:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(AuthTable.TABLE_AUTH,
	            AuthTable.COLUMN_ID + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = sqlDB.delete(AuthTable.TABLE_AUTH,
	            AuthTable.COLUMN_ID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    long id = 0;
	    switch (uriType) {
	    case AUTHS:
	      id = sqlDB.insert(AuthTable.TABLE_AUTH, null, values);
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
	    database = new AuthDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(AuthTable.TABLE_AUTH);
	    
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case AUTH:
	        queryBuilder.appendWhere(AuthTable.COLUMN_ID + "="
	                + uri.getLastPathSegment());
	        break;
	    case AUTHS:
	        // no filter
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	    
	    SQLiteDatabase db = database.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	            selectionArgs, null, null, sortOrder);
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
	    case AUTHS:
	      rowsUpdated = sqlDB.update(AuthTable.TABLE_AUTH, 
	          values, 
	          selection,
	          selectionArgs);
	      break;
	    case AUTH:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsUpdated = sqlDB.update(AuthTable.TABLE_AUTH, 
	            values,
	            AuthTable.COLUMN_ID + "=" + id, 
	            null);
	      } else {
	        rowsUpdated = sqlDB.update(AuthTable.TABLE_AUTH, 
	            values,
	            AuthTable.COLUMN_ID + "=" + id 
	            + " and " 
	            + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}

	
}
