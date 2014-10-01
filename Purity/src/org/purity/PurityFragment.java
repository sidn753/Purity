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

	Sensor accelerometer;
	Sensor magnetometer;

	float[] gravity;
	float[] geomagnetic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		wallpaperManager = WallpaperManager.getInstance(getActivity());
		sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

		View rootView = inflater.inflate(R.layout.app_main_fragment, container, false);
		background = (ImageView)rootView.findViewById(R.id.appBackground);

		accelerometer = (Sensor)sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = (Sensor)sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		return rootView;
	}

	@Override
	public void onResume() {
		super .onResume();
		background.setImageDrawable(wallpaperManager.getDrawable());
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public void onPause() {
		super .onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			gravity = event.values;
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			geomagnetic = event.values;

		if((gravity != null) && (geomagnetic != null)) {
			float[] R = new float[9];
			float[] I = new float[9];

			if(SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
				float[] orientation = new float[3];
				SensorManager.getOrientation(R, orientation);

				background.setTranslationX(orientation[2] * 12.0f);
				background.setTranslationY(orientation[1] * 12.0f);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
