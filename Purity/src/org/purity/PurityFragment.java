package org.purity;

import org.purity.R;

import android.app.Fragment;
import android.app.WallpaperManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PurityFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.app_main_fragment, container, false);
		return rootView;
	}

	@Override
	public void onResume() {
		super .onResume();
		final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
		this.getView().setBackground(wallpaperManager.getDrawable());
	}
}
