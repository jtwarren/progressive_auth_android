package com.android.progressiveauthentication;

import java.util.List;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

//public class PickAuthLevelActivity extends ListActivity {
//
//	public void onCreate(Bundle icicle) {
//		super.onCreate(icicle);
//		
//		final PackageManager pm = getPackageManager();
//		//get a list of installed apps.
//		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//		String[] packageNames = new String[packages.size()];
//		for (int i =0; i<packages.size(); i++) {
//			packageNames[i] = packages.get(i).loadLabel(pm).toString();
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, packageNames);
//		setListAdapter(adapter);
//	}
//	
//	  @Override
//	  protected void onListItemClick(ListView l, View v, int position, long id) {
//	    String item = (String) getListAdapter().getItem(position);
//	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
//	  }
//}

public class PickAuthLevelActivity extends ListActivity implements AuthLevelDialog.AuthDialogListener{

    List<ApplicationInfo> packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager pm = getPackageManager();
		//get a list of installed apps.
        Log.e("LISTS", "here");
		packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        Log.e("LISTS", "here2");
		String[] packageNames = new String[packages.size()];
		Drawable[] packageLogos = new Drawable[packages.size()];
		for (int i =0; i<packages.size(); i++) {
	        Log.e("LISTS", "here3" + i);
			packageNames[i] = packages.get(i).loadLabel(pm).toString();
			packageLogos[i] = packages.get(i).loadLogo(pm);
	        Log.e("LISTS", "here4"+i);
		}
		PackageList adapter = new PackageList(this, packages, packageNames, packageLogos);
        Log.e("LISTS", "here5");
		setListAdapter(adapter);        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    showAuthDialog(position);
    }
    
    public void showAuthDialog(int position) {
        // Create an instance of the dialog fragment and show it
    	Bundle data = new Bundle();
    	data.putInt("POS", position);
        DialogFragment dialog = new AuthLevelDialog();
        dialog.setArguments(data);
        dialog.show(getFragmentManager(), "AuthLevelDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int level) {
        // User touched the dialog's positive button
    	int position = dialog.getArguments().getInt("POS",-1);
    	ContentValues values = new ContentValues();
    	values.put(AuthTable.COLUMN_PACKAGE, packages.get(position).packageName);
    	values.put(AuthTable.COLUMN_LEVEL, level);
    	Uri uri = getContentResolver().insert(AuthProvider.CONTENT_URI, values);
    	Log.e("AUTH", "" + AuthProvider.CONTENT_URI.toString() + " " + packages.get(position).packageName + " " + level);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}