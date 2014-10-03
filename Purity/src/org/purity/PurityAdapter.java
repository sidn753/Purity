/*
 * PurityAdapter.java
 * Copyright (C) 2014  Marchand Axel
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.purity;

import java.util.List;

import org.purity.R;

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
