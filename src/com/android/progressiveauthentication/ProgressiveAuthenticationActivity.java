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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.IProgressiveAuthenticationService;
import android.os.ServiceManager;

import android.content.Intent;
import android.content.ServiceConnection;



public class ProgressiveAuthenticationActivity extends Activity {
 
  private static final String TAG = "PROG_AUTH";
  private Intent mIntent;
  private int mCurrentLevel;
  private int mRequiredLevel = -1;

  private IAuthService mIAuthService;

  private static IProgressiveAuthenticationService om = IProgressiveAuthenticationService.Stub.asInterface(ServiceManager.getService("ProgressiveAuthentication"));
                

  private static final int PASSCODE_REQUEST = 1;
  private static final int PASSWORD_REQUEST = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mIntent = Intent.getIntent(getIntent().getStringExtra("intent"));
            String packageName = mIntent.resolveActivity(getPackageManager()).getPackageName().toString();
            mRequiredLevel = om.getRequiredLevel(packageName);
            mCurrentLevel = om.getAuthLevel();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        handleAuthentication();

    }


    private void handleAuthentication() {
        if (mRequiredLevel <= mCurrentLevel) {
            Log.d(TAG, "Required <= current");
            mIntent.putExtra("auth", true);
            startActivity(mIntent);
            this.finish();
            return;
        }
        if (mCurrentLevel < 1) {
            Log.d(TAG, "Required <= 1");
            Intent passwordIntent = new Intent(this, PasscodeAuthentication.class);
            startActivityForResult(passwordIntent, PASSCODE_REQUEST);
        } else if (mCurrentLevel < 2) {
            Log.d(TAG, "Required <= 2");
            Intent passwordIntent = new Intent(this, PasswordAuthenticate.class);
            startActivityForResult(passwordIntent, PASSWORD_REQUEST);
        } else {
          Log.d(TAG, "Required > 2");
          return;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("PROG_AUTH", "activity result");


        if (requestCode == PASSCODE_REQUEST) {
            Log.e("PROG_AUTH", "result passed and auth - code");
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("auth", false)) {
                    try {
                      Log.e("PROG_AUTH", "update Authentication level 1");
                        om.updateAuthenticationLevel(1);
                        mCurrentLevel = om.getAuthLevel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                  }
              handleAuthentication();
              }
        }


        if (requestCode == PASSWORD_REQUEST) {
            Log.e("PROG_AUTH", "result passed and auth - pw");
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("auth", false)) {
                    try {
                      Log.e("PROG_AUTH", "update Authentication level 1");
                        om.updateAuthenticationLevel(2);
                        mCurrentLevel = om.getAuthLevel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handleAuthentication();
              }
        }


    }
}
