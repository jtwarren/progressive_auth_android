package com.android.progressiveauthentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PasscodeAuthentication extends Activity {
	
	public SharedPreferences prefs;
	public static final String AUTH_TYPE = "Passcode";
	public static final String HARD_CODED = "1234";
	public static final String ERROR_TAG = "PASSCODE";
	public static final String IDENTIFIER = "com.example.passwordauth";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.passcode_auth);
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    final Button enterButton = (Button) findViewById(R.id.button1);
	    
	    SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AUTH_TYPE, HARD_CODED);
        editor.commit();
	    
	    enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText pin = (EditText) findViewById(R.id.editText1);
				if (pin == null) {
					Log.e(ERROR_TAG, "Pw field is null");
				}
				String input = pin.getEditableText().toString();
				String result = prefs.getString(AUTH_TYPE, null);
				boolean r = result.equals(input);
				Intent resultantIntent = new Intent();
				resultantIntent.putExtra(IDENTIFIER, r);
				setResult(Activity.RESULT_OK, resultantIntent);
				if (r) {
					Log.i(ERROR_TAG, "Passed password check!");
				}
				else {
					Log.i(ERROR_TAG, "Failed password check!");
				}
				finish();
				
			}
	    });
	    // TODO Auto-generated method stub
	}
	
	@SuppressWarnings("unused")
	private boolean verifyPin() {
		return false;
	}

}
