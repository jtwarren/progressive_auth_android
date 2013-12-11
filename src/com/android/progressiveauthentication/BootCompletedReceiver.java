package com.android.progressiveauthentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

	final static String TAG = "PROG_AUTH";
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		Log.w(TAG, "starting AuthService");
        Intent authServiceIntent = new Intent();
        authServiceIntent.setAction("com.android.internal.app.AuthService");
        context.startService(authServiceIntent);
	}

}
