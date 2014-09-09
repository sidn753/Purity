package me.rubrumlapidem.purity;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		BitmapDrawable app_logo_src = (BitmapDrawable) app.loadIcon(getContext().getPackageManager());

		int app_icon_bitmap_drawable_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getContext().getResources().getDisplayMetrics());
		BitmapDrawable app_logo_rsz = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(app_logo_src.getBitmap(), app_icon_bitmap_drawable_size, app_icon_bitmap_drawable_size, false));

		TextView app_label = (TextView) v.findViewById(R.id.app_row_label);
		app_label.setText(app.loadLabel(getContext().getPackageManager()));

		ImageView app_logo = (ImageView) v.findViewById(R.id.app_row_logo);
		app_logo.setImageDrawable(app_logo_rsz);

		return v;
	}
}
