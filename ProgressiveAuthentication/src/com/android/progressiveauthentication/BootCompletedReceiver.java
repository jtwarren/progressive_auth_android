package com.android.progressiveauthentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

	final static String TAG = "PROG_AUTH";
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		Log.w(TAG, "starting PublicAuthService");
        Intent publicAuthServiceIntent = new Intent();
        publicAuthServiceIntent.setAction("com.android.progressiveauthentication.PublicAuthService");
        context.startService(publicAuthServiceIntent);
	}

}
