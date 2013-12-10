package com.android.progressiveauthentication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AuthService extends Service {

    private static final String TAG = "ProgressiveAuthenticationService";
    private int mAuthLevel = 0;

    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "starting AuthService");
		
		registerForBroadcasts();
		
		return START_STICKY;
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	// TODO implement binder interface for service
	private final IAuthService.Stub binder = new IAuthService.Stub() {
		
		public boolean authorized(String packageName) {
	        if (mAuthLevel == 1) {
	            Log.d(TAG, "returning true, auth level 1");
	            return true;
	        }

	        if (packageName.equals("com.android.mms")) {
	            Log.d(TAG, "returning false, mms");
	            return false;
	        }

	        Log.d(TAG, "returning true, auth level 0");
	        return true;
		}
		
		public void setAuthLevel(int level) {
	        Log.d(TAG, "setting auth level: " + level);
			mAuthLevel = level;
		}
	};

	
	
	
	// Listen for Screen on/off intents
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.i(TAG, "Screen on");
                mAuthLevel = 0;
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i(TAG, "Screen off");
                mAuthLevel = 0;
            }
        }
    };

    private void registerForBroadcasts() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, intentFilter);
    }
}
