package com.android.progressiveauthentication;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PackageList extends ArrayAdapter<String> {
	private final Activity context;
	private List<ApplicationInfo> packages;
	private String[] packageNames;
	private Drawable[] packageLogos;

	public PackageList(Activity context, List<ApplicationInfo> packages, String[] packageNames, Drawable[] packageLogos) {
		super(context, R.layout.row, packageNames);
		this.context = context;
		this.packages = packages;
		this.packageNames = packageNames;
		this.packageLogos = packageLogos;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.row, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(packageNames[position]);
		imageView.setImageDrawable(packageLogos[2]);
		return rowView;
	}
}