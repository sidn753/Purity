/*
 Purity.java
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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.purity.list.EntryItem;
import org.purity.list.Item;
import org.purity.list.SectionItem;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Purity extends Activity {
	FragmentManager fragmentManager;
	PackageManager packageManager;

	FrameLayout frameLayout;
	DrawerLayout drawerLayout;
	ListView appsList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super .onCreate(savedInstanceState);
		setContentView(R.layout.app_layout);

		if(android.os.Build.VERSION.SDK_INT >= 21) {
			this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		}

		this.fragmentManager = getFragmentManager();
		this.fragmentManager.beginTransaction().replace(R.id.appFrameLayout, new PurityFragment()).commit();

		this.packageManager = getPackageManager();

		this.frameLayout = (FrameLayout)findViewById(R.id.appFrameLayout);
		this.drawerLayout = (DrawerLayout)findViewById(R.id.appDrawerLayout);
		this.appsList = (ListView)findViewById(R.id.appList);

		this.appsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = ((EntryItem) adapter.getItemAtPosition(position)).getApplicationInfo();
				drawerLayout.closeDrawers();
				startActivity(packageManager.getLaunchIntentForPackage(app.packageName));
			}
		});

		this.appsList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = ((EntryItem) adapter.getItemAtPosition(position)).getApplicationInfo();
				Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + app.packageName));
				drawerLayout.closeDrawers();
				startActivity(intent);
				return true;
			}
		});

		this.drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, android.R.color.transparent, android.R.string.untitled, android.R.string.untitled) {
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = drawerLayout.getWidth() * slideOffset / 4;
				frameLayout.setTranslationX(moveFactor);
			}
		});
	}

	@Override
	public void onStart() {
		super .onStart();

		List<Item> items = new ArrayList<Item>();

		List<ApplicationInfo> apps = this.packageManager.getInstalledApplications(0);
		for(ApplicationInfo app : apps) {
			if(this.packageManager.getLaunchIntentForPackage(app.packageName) != null) {
				items.add(new EntryItem(app));
			}
		}

		Collections.sort(items, new Comparator<Item>() {
			private Collator collator = Collator.getInstance();

			@Override
			public int compare(Item item0, Item item1) {
				ApplicationInfo app0 = ((EntryItem)item0).getApplicationInfo();
				ApplicationInfo app1 = ((EntryItem)item1).getApplicationInfo();
				return collator.compare(app0.loadLabel(packageManager), app1.loadLabel(packageManager));
			}
		});

		char last = 0;
		for(int i = 0; i < items.size(); i++) {
			EntryItem item = (EntryItem) items.get(i);
			char startWith = item.getApplicationInfo().loadLabel(packageManager).charAt(0);
			if(startWith != last) {
				if(Character.isDigit(startWith)) {
					if(!Character.isDigit(last)) {
						items.add(i, new SectionItem('#'));
					}
				}else {
					items.add(i, new SectionItem(startWith));
				}
				last = startWith;
			}
		}

		this.appsList.setAdapter(new PurityAdapter(this, R.layout.app_row, items));
	}
}
