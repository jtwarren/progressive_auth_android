package com.android.progressiveauthentication;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;

public class ProgressiveAuthenticationActivity extends Activity {

	private static final String TAG = "PROG_AUTH";
  private Intent mIntent;

  private static final int PASSCODE_REQUEST = 1;
  private static final int PASSWORD_REQUEST = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mIntent = Intent.getIntent(getIntent().getStringExtra("intent"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }



    mIntent.putExtra("auth", true);
    PasswordAuthenticate.this.startActivity(mIntent);



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.progressive_authetication, menu);
        return true;
    }
    
    IAuthService mIAuthService;
    private ServiceConnection mConnection = new ServiceConnection () {
	    @Override
	    public void onServiceConnected(ComponentName className, IBinder service) {
			Log.e("PROG_AUTH", "ProgAuthActivity connected to AuthService");
	    	mIAuthService = IAuthService.Stub.asInterface(service);
	    }

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mIAuthService = null;
		}
    };
    
  //   IPublicAuthService mIPublicAuthService;
  //   private ServiceConnection mPublicConnection = new ServiceConnection () {
	 //    @Override
	 //    public void onServiceConnected(ComponentName className, IBinder service) {
		// 	Log.e("PROG_AUTH", "ProgAuthActivity connected to PublicAuthService");
	 //    	mIPublicAuthService = IPublicAuthService.Stub.asInterface(service);	    	
	 //    }

		// @Override
		// public void onServiceDisconnected(ComponentName arg0) {
		// 	mIPublicAuthService = null;
		// }
  //   };
    
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (AuthService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
    
	@Override
	protected void onStart() {
    	super.onStart();
    	
    	if (isMyServiceRunning()) {
    		Log.i(TAG, "running");
    	} else {
    		Log.i(TAG, "not running");
    	}
    	
	   	Intent intent = new Intent (this, AuthService.class);
	   	bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
	   	// Intent pubIntent = new Intent (this, PublicAuthService.class);
	   	// bindService(pubIntent, mPublicConnection, Context.BIND_AUTO_CREATE);
    }
}
