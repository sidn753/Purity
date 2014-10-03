/*
 * Purity.java
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

		this.fragmentManager = getFragmentManager();
		this.fragmentManager.beginTransaction().replace(R.id.appFrameLayout, new PurityFragment()).commit();

		this.packageManager = getPackageManager();

		this.frameLayout = (FrameLayout)findViewById(R.id.appFrameLayout);
		this.drawerLayout = (DrawerLayout)findViewById(R.id.appDrawerLayout);
		this.appsList = (ListView)findViewById(R.id.appList);

		this.appsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = (ApplicationInfo) adapter.getItemAtPosition(position);
				drawerLayout.closeDrawers();
				startActivity(packageManager.getLaunchIntentForPackage(app.packageName));
			}
		});

		this.appsList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = (ApplicationInfo) adapter.getItemAtPosition(position);
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

		List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();

		List<ApplicationInfo> appsNotSort = this.packageManager.getInstalledApplications(0);
		for(ApplicationInfo app : appsNotSort) {
			if(this.packageManager.getLaunchIntentForPackage(app.packageName) != null) {
				apps.add(app);
			}
		}

		Collections.sort(apps, new Comparator<ApplicationInfo>() {
			private Collator collator = Collator.getInstance();

			@Override
			public int compare(ApplicationInfo app0, ApplicationInfo app1) {
				return collator.compare(app0.loadLabel(packageManager), app1.loadLabel(packageManager));
			}
		});

		this.appsList.setAdapter(new PurityAdapter(this, R.layout.app_row, apps));
	}
}
