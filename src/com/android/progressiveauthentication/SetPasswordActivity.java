package com.android.progressiveauthentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPasswordActivity extends Activity {
	
	public SharedPreferences prefs;
	public static final String AUTH_TYPE = "Password";
	public static final String SALT = "Salt";
	public static final String ERROR_TAG = "PASSWORD";
	public static final String IDENTIFIER = "com.example.passwordauth";
	public static final String HASH_ALGO = "PBKDF2WithHmacSHA1";
	public static final String HARD_CODED_PWD = "pass";
	public static final int SALT_LENGTH = 20;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(ERROR_TAG, "intent string extra" + getIntent().getStringExtra("intent"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_authenticate);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Button enterButton = (Button) findViewById(R.id.button1);
		
        // String gen = HARD_CODED_PWD;
        enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(ERROR_TAG, "Entered Click!");
				EditText password = (EditText) findViewById(R.id.editText1);
				if (password == null) {
					  Log.e(ERROR_TAG, "Pw field is null");
				} else {
			      ContentValues values = new ContentValues();
			      values.put(AuthTable.COLUMN_PACKAGE, "com.android.mms");
	     	    values.put(AuthTable.COLUMN_LEVEL, 1);
	     	    Log.e("CONTENT",AuthProvider.CONTENT_URI.toString());
	     	    Uri uri = getContentResolver().insert(AuthProvider.CONTENT_URI, values);

            ContentValues values1 = new ContentValues();
            values1.put(AuthTable.COLUMN_PACKAGE, "com.android.browser");
            values1.put(AuthTable.COLUMN_LEVEL, 2);
            Uri uri1 = getContentResolver().insert(AuthProvider.CONTENT_URI, values1);


	     	    Log.e("CONTENT","GOT Content Resolver");
	     	    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progressive_authetication, menu);
        return true;
    }

    
    public void readFromCursor() {
        // Retrieve student records
        Uri auth = AuthProvider.CONTENT_URI;
        Cursor c = managedQuery(auth, null, null, null, null);
        if (c.moveToFirst()) {
           do{
        	  Log.e("CONTENT",  c.getString(c.getColumnIndex(AuthTable.COLUMN_ID)) + 
              ", " +  c.getString(c.getColumnIndex( AuthTable.COLUMN_PACKAGE)) + 
              ", " + c.getString(c.getColumnIndex( AuthTable.COLUMN_LEVEL)));
              Toast.makeText(this, 
              c.getString(c.getColumnIndex(AuthTable.COLUMN_ID)) + 
              ", " +  c.getString(c.getColumnIndex( AuthTable.COLUMN_PACKAGE)) + 
              ", " + c.getString(c.getColumnIndex( AuthTable.COLUMN_LEVEL)), 
              Toast.LENGTH_SHORT).show();
           } while (c.moveToNext());
        }
     }

}
