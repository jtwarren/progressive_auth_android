package com.android.progressiveauthentication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class PublicAuthService extends Service {
	
	private static final String TAG = "PROG_AUTH";	
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "starting PublicAuthService");
		
		Log.w(TAG, "starting AuthService");
        Intent authServiceIntent = new Intent();
        authServiceIntent.setAction("com.auth.progressive.progressiveauthlauncher.AuthService");
        startService(authServiceIntent);

	   	Intent authIntent = new Intent (this, AuthService.class);
	   	bindService(authIntent, mConnection, Context.BIND_AUTO_CREATE);
		
		return START_STICKY;
    }

    IAuthService mIAuthService;
    private ServiceConnection mConnection = new ServiceConnection () {
	    @Override
	    public void onServiceConnected(ComponentName className, IBinder service) {
			Log.e("PROG_AUTH", "public auth service connected to auth service");
	    	mIAuthService = IAuthService.Stub.asInterface(service);
	    }

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.e("PROG_AUTH", "PublicAuthService disconnected to AuthService");
			mIAuthService = null;
		}
    };
    
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	// TODO implement binder interface for service
	private final IPublicAuthService.Stub binder = new IPublicAuthService.Stub() {
		
		public boolean authorized(String packageName) {
			// not implemented
			Log.i(TAG, "in PublicAuthService authenticated");
			try {
				return mIAuthService.authorized(packageName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	};
}
