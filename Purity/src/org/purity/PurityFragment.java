/*
 *	PurityFragment.java
 *	Copyright (C) 2014  Marchand Axel
 *	This program is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.

 *	You should have received a copy of the GNU General Public License along
 *	with this program; if not, write to the Free Software Foundation, Inc.,
 *	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.purity;

import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PurityFragment extends Fragment implements SensorEventListener {
	WallpaperManager wallpaperManager;
	SensorManager sensorManager;

	ImageView background;
	Sensor sensor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		wallpaperManager = WallpaperManager.getInstance(getActivity());
		sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensor = (Sensor)sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		View rootView = inflater.inflate(R.layout.app_main_fragment, container, false);
		background = (ImageView)rootView.findViewById(R.id.appBackground);
		return rootView;
	}

	@Override
	public void onResume() {
		super .onResume();
		background.setImageDrawable(wallpaperManager.getDrawable());
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public void onPause() {
		super .onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.8f;

		float[] gravity = new float[2];
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];

		float[] linear_acceleration = new float[2];
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];

		background.setTranslationX(linear_acceleration[0] * 4.0f);
		background.setTranslationY(linear_acceleration[1] * -4.0f);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
