package com.android.progressiveauthentication;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PasscodeAuthentication extends Activity {
	
	public SharedPreferences prefs;
	public static final String AUTH_TYPE = "Passcode";
	public static final String HARD_CODED = "6858";
	public static final String ERROR_TAG = "PASSCODE";
	public static final String IDENTIFIER = "com.android.progressiveauthentication";

	public static final String HARD_CODED = "1234";
    public static final String HASH_ALGO = "PBKDF2WithHmacSHA1";
    public static final String HARD_CODED_PWD = "pass";
    public static final int SALT_LENGTH = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(ERROR_TAG, "intent string extra" + getIntent().getStringExtra("intent"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_authenticate);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Button enterButton = (Button) findViewById(R.id.button1);

        enterButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.i(ERROR_TAG, "Entered Click!");
                EditText passcode = (EditText) findViewById(R.id.passcode);
                if (passcode == null) {
                    Log.e(ERROR_TAG, "Passcode field is null");
                }
                boolean res = verifyPasscode(passcode.getEditableText().toString());
                Intent resultantIntent = new Intent();
                resultantIntent.putExtra(IDENTIFIER, res);
                setResult(Activity.RESULT_OK, resultantIntent);
                if (res) {
                    Log.i(ERROR_TAG, "Passed passcode check!");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("auth", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                }
                else {
                    Log.i(ERROR_TAG, "Failed passcode check!");
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
    
    private boolean verifyPasscode(String pwd) {
    	Log.i(ERROR_TAG, "In Verify Passcode!");
    	// Fetch hashed value
    	String result = prefs.getString(AUTH_TYPE, "");
    	if (result == null) {
    		Log.e(ERROR_TAG, "Not seeing a stored key; This is an issue.");
    		return false;
    	}
    	
    	// Fetch salt from shared preferences
    	byte[] salt = stringToByteArr(prefs.getString(AUTH_TYPE+"_SALT", null));
    	Log.i(ERROR_TAG, "In Verify Passcode!");
    	String key;
    	// Attempt to hash supplied passcode with salt
    	try {
			key = generateKey(pwd.toCharArray(), salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return false;
		}
    	// Checks whether stored hash equals hashed supplied passcode
    	if (result.equals(key)) {
    		return true;
    	}
    	return false;
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
        return result;
    }
    
	// Will encode byteArray to String
	public static String byteArrToString(byte[] b) {
		String str = null;
		str = new String(b);
    	return str;
    }
    
	// Will decode String to byteArray
    public static byte[] stringToByteArr(String s) {
    	byte[] bytes = null;
		bytes = s.getBytes();
    	return bytes;
    }
}
