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

import org.purity.list.EntryItem;
import org.purity.list.Item;
import org.purity.list.SectionItem;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PurityAdapter extends ArrayAdapter<Item> {
	private PackageManager packageManager;
	private LayoutInflater layoutInflater;
	private Context context;

	public PurityAdapter(Context context, int resource, List<Item> items) {
		super(context, resource, items);

		this.packageManager = context.getPackageManager();
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Item item = getItem(position);

		if(item != null) {
			if(item.isSection()) {
				SectionItem section = (SectionItem) item;

				v = this.layoutInflater.inflate(R.layout.app_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				TextView title = (TextView) v.findViewById(R.id.appSectionLabel);
				title.setText(section.getTitle());
			}else {
				EntryItem entry = (EntryItem) item;
				ApplicationInfo app = entry.getApplicationInfo();
				Resources resources = this.context.getResources();

				v = this.layoutInflater.inflate(R.layout.app_row, null);

				BitmapDrawable appIconSrc = (BitmapDrawable) app.loadIcon(this.packageManager);

				int appIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, resources.getDisplayMetrics());
				BitmapDrawable appIconResized = new BitmapDrawable(resources, Bitmap.createScaledBitmap(appIconSrc.getBitmap(), appIconSize, appIconSize, false));

				TextView appLabel = (TextView) v.findViewById(R.id.appRowLabel);
				appLabel.setText(app.loadLabel(this.packageManager));

				appLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(appIconResized, null, null, null);
			}
		}

		return v;
	}
}
