package org.purity;

import java.util.List;

import me.rubrumlapidem.purity.R;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PurityAdapter extends ArrayAdapter<ApplicationInfo> {
	public PurityAdapter(Context context, int resource, List<ApplicationInfo> apps) {
		super(context, resource, apps);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.app_row, parent, false);
		}else {
			v = convertView;
		}

		ApplicationInfo app = getItem(position);
		BitmapDrawable appIconSrc = (BitmapDrawable) app.loadIcon(getContext().getPackageManager());

		int appIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getContext().getResources().getDisplayMetrics());
		BitmapDrawable appIconResized = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(appIconSrc.getBitmap(), appIconSize, appIconSize, false));

		TextView appLabel = (TextView) v.findViewById(R.id.appRowLabel);
		appLabel.setText(app.loadLabel(getContext().getPackageManager()));

		appLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(appIconResized, null, null, null);

		return v;
	}
}
