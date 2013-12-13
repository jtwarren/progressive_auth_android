package com.android.progressiveauthentication;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        final Button enterButton = (Button) findViewById(R.id.button1);
        enterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(ERROR_TAG, "Entered Click!");
				String password = ((EditText) findViewById(R.id.password)).getEditableText().toString();
				if (password == null || password.isEmpty()) {
					Log.e(ERROR_TAG, "Pw field is null");
					Toast toast = Toast.makeText(SetPasswordActivity.this, "password field is empty", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					
			        // Generate a random salt of length 20
			        byte[] salt = new byte[SALT_LENGTH];
			        new Random().nextBytes(salt);
			        salt = stringToByteArr(byteArrToString(salt));
			        String gen = null;
			        
					try {
						gen = generateKey(password.toCharArray(), salt);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						gen = HARD_CODED_PWD;
				        salt = new byte[SALT_LENGTH];
					} catch (InvalidKeySpecException e) {
						e.printStackTrace();
						gen = HARD_CODED_PWD;
				        salt = new byte[SALT_LENGTH];
					}

			        SharedPreferences.Editor editor = prefs.edit();
			        
			        editor.putString(AUTH_TYPE, gen);
			        editor.putString(AUTH_TYPE + "_SALT", byteArrToString(salt));
			        editor.commit();
					finish();
				}
			}
		});
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.e("PROG_AUTH", data.toString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progressive_authetication, menu);
        return true;
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
