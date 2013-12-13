package com.android.progressiveauthentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class AuthLevelDialog extends DialogFragment {

    public interface AuthDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int level);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    AuthDialogListener mListener;
	
    int selectedItem = 0;
	String[] authLevels = new String[] { "Unprotected", "Passcode", "Password" };

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int index = -1;
		int hello;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle("Pick Authentication Level")
				.setSingleChoiceItems(R.array.authLevels, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								selectedItem = which;
							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.e("POSITIVE", "" + id);
								Log.e("LISTENER", "null?" + (mListener==null));
								mListener.onDialogPositiveClick(AuthLevelDialog.this, selectedItem);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.e("NEGATIVE", "" + id);
								mListener.onDialogNegativeClick(AuthLevelDialog.this);
							}
						});

		return builder.create();
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AuthDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AuthDialogListener");
        }
    }
}