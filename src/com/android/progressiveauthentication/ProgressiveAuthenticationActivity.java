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

  private IAuthService mIAuthService;

  private static IProgressiveAuthenticationService om = IProgressiveAuthenticationService.Stub.asInterface(ServiceManager.getService("ProgressiveAuthentication"));
                

  private static final int PASSCODE_REQUEST = 1;
  private static final int PASSWORD_REQUEST = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("PROG_AUTH", "oncreate");
        super.onCreate(savedInstanceState);

        int level = 0;


        try {
            mIntent = Intent.getIntent(getIntent().getStringExtra("intent"));
            level = getIntent().getIntExtra("level", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (level == 1) {
          Intent passwordIntent = new Intent(this, PasscodeAuthentication.class);
          startActivityForResult(passwordIntent, PASSCODE_REQUEST);
        }

        if (level == 2) {
          Intent passwordIntent = new Intent(this, PasswordAuthenticate.class);
          startActivityForResult(passwordIntent, PASSWORD_REQUEST);
        }


        

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("PROG_AUTH", "activity result");


        if (requestCode == PASSCODE_REQUEST) {
            Log.e("PROG_AUTH", "result passed and auth - code");
            if (resultCode == RESULT_OK) {
              if (data.getBooleanExtra("auth", false)) {
                Log.e("PROG_AUTH", "result passed and auth - code");

                try {
                  om.updateAuthenticationLevel(1);
                } catch (Exception e) {
                  e.printStackTrace();
                }

                mIntent.putExtra("auth", true);
                startActivity(mIntent);
                this.finish();
              } else {
                this.finish();
              }
            }
        }


        if (requestCode == PASSWORD_REQUEST) {
            Log.e("PROG_AUTH", "result passed and auth");
            if (resultCode == RESULT_OK) {
              if (data.getBooleanExtra("auth", false)) {
                Log.e("PROG_AUTH", "result passed and auth");

                try {
                  om.updateAuthenticationLevel(2);
                } catch (Exception e) {
                  e.printStackTrace();
                }

                mIntent.putExtra("auth", true);
                startActivity(mIntent);
                this.finish();
              } else {
                this.finish();
              }
            }
        }


    }
}
