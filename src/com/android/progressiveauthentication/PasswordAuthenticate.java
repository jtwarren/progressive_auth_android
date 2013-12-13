package com.android.progressiveauthentication;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordAuthenticate extends Activity {
	
	public SharedPreferences prefs;
	public static final String AUTH_TYPE = "Password";
	public static final String SALT = "Salt";
	public static final String ERROR_TAG = "PASSWORD";
	public static final String IDENTIFIER = "com.example.passwordauth";
	public static final String HASH_ALGO = "PBKDF2WithHmacSHA1";
	public static final String HARD_CODED_PWD = "pass";
	public static final int SALT_LENGTH = 20;

	public static int test_counter = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(ERROR_TAG, "intent string extra" + getIntent().getStringExtra("intent"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_authenticate);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Button enterButton = (Button) findViewById(R.id.button1);
        // Generate a random salt of length 20
        // byte[] b = new byte[SALT_LENGTH];
        // new Random().nextBytes(b);
        String gen = HARD_CODED_PWD;
        /*
		try {
			gen = generateKey(HARD_CODED_PWD.toCharArray(), b);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gen = "";
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gen = "";
		}
        */
		
        // String gen = HARD_CODED_PWD;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AUTH_TYPE, gen);
        // editor.putInt(SALT, byteArrToInt(b));
        editor.commit();
        enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(ERROR_TAG, "Entered Click!");
				EditText password = (EditText) findViewById(R.id.editText1);
				if (password == null) {
					Log.e(ERROR_TAG, "Pw field is null");
				} else {
					readFromCursor();
//					  test_counter += 1;
//				      ContentValues values = new ContentValues();
//				      values.put(AuthTable.COLUMN_PACKAGE, "test.package.name"+test_counter);
//		     	      values.put(AuthTable.COLUMN_LEVEL, test_counter);
//		     	      Log.e("CONTENT",AuthProvider.CONTENT_URI.toString());
//		     	      Uri uri = getContentResolver().insert(AuthProvider.CONTENT_URI, values);
//		     	      Log.e("CONTENT","GOT Content Resolver");
//		     	      Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
				}
				Log.i(ERROR_TAG, "Pw field has this: " + password.getEditableText().toString());
				boolean res = verifyPassword(password.getEditableText().toString());
				Intent resultantIntent = new Intent();
				resultantIntent.putExtra(IDENTIFIER, res);
				setResult(Activity.RESULT_OK, resultantIntent);
				if (res) {
					Log.i(ERROR_TAG, "Passed password check!");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("auth", true);
                    setResult(Activity.RESULT_OK, resultIntent);
				}
				else {
					Log.i(ERROR_TAG, "Failed password check!");
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
    
    private boolean verifyPassword(String pwd) {
    	Log.i(ERROR_TAG, "In Verify Password!");
    	// Get hashed key
    	String result = prefs.getString(AUTH_TYPE, null);
    	if (result == null) {
    		Log.e(ERROR_TAG, "Not seeing a stored key");
    		return false;
    	}
    	Log.i(ERROR_TAG, "Fetching this guy: " + result);
    	// Get salt from storage
    	// byte[] salt = intToByteArr(prefs.getInt(SALT, -1));
    	String key = pwd;
    	// Attempt to hash supplied pwd with salt
    	/*
    	try {
			key = generateKey(pwd.toCharArray(), salt);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        */
    	// Checks whether stored result equals hashed supplied password
    	if (result.equals(key)) {
    		return true;
    	}
    	return false;
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
    
    // Cryptographic hashing with salt
	private static String generateKey(char[] pwd, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final int iterations = 1000; 

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASH_ALGO);
        KeySpec keySpec = new PBEKeySpec(pwd, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        // Convert to a string for storage while maintaining consistency
        String result = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
        Log.i(ERROR_TAG, "Generated this key result: " + result);
        return result;
    }
    
    public static int byteArrToInt(byte[] b) {
    	ByteBuffer wrapped = ByteBuffer.wrap(b);
    	int i = wrapped.getInt();
    	return i;
    }
    
    public static byte[] intToByteArr(int i) {
    	ByteBuffer dbuf = ByteBuffer.allocate(SALT_LENGTH);
    	dbuf.putInt(i);
    	byte[] bytes = dbuf.array();
    	return bytes;
    }
}
