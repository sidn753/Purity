
package me.rubrumlapidem.purity;

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
	FragmentManager app_fragment_manager;
	PackageManager app_package_manager;

	ActionBarDrawerToggle app_action_bar_drawer_toggle;
	FrameLayout app_frame_layout;
	DrawerLayout app_drawer_layout;
	ListView app_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super .onCreate(savedInstanceState);
		setContentView(R.layout.app_layout);

		this.app_fragment_manager = getFragmentManager();
		this.app_fragment_manager.beginTransaction().replace(R.id.app_frame_layout, new PurityFragment()).commit();

		this.app_package_manager = getPackageManager();

		this.app_frame_layout = (FrameLayout)findViewById(R.id.app_frame_layout);
		this.app_drawer_layout = (DrawerLayout)findViewById(R.id.app_drawer_layout);
		this.app_list = (ListView)findViewById(R.id.app_list);

		this.app_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = (ApplicationInfo) adapter.getItemAtPosition(position);
				app_drawer_layout.closeDrawers();
				startActivity(app_package_manager.getLaunchIntentForPackage(app.packageName));
			}
		});

		this.app_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View parent, int position, long id) {
				ApplicationInfo app = (ApplicationInfo) adapter.getItemAtPosition(position);
				Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + app.packageName));
				app_drawer_layout.closeDrawers();
				startActivity(intent);
				return true;
			}
		});

		this.app_action_bar_drawer_toggle = new ActionBarDrawerToggle(this, this.app_drawer_layout, android.R.color.transparent, R.string.app_empty_string, R.string.app_empty_string) {
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = app_list.getWidth() * slideOffset;
				app_frame_layout.setTranslationX(moveFactor);
			}
		};
		this.app_drawer_layout.setDrawerListener(this.app_action_bar_drawer_toggle);
	}

	@Override
	public void onStart() {
		super .onStart();

		List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();

		List<ApplicationInfo> apps_not_sort = this.app_package_manager.getInstalledApplications(0);
		for(ApplicationInfo app : apps_not_sort) {
			if(this.app_package_manager.getLaunchIntentForPackage(app.packageName) != null) {
				apps.add(app);
			}
		}

		Collections.sort(apps, new Comparator<ApplicationInfo>() {
			private Collator collator = Collator.getInstance();

			@Override
			public int compare(ApplicationInfo app0, ApplicationInfo app1) {
				return collator.compare(app0.loadLabel(app_package_manager), app1.loadLabel(app_package_manager));
			}
		});

		this.app_list.setAdapter(new PurityAdapter(this, R.layout.app_row, apps));
	}
}
