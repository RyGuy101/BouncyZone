package com.blogspot.mathjoy.bouncy;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MoreSettingsFragment extends Fragment
{
	ToggleButton accel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_more_settings, container, false);
		accel = (ToggleButton) view.findViewById(R.id.accelToggle);
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		accel.setChecked(sp.getBoolean("useAccelerometer", false));
		accel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				SavePrefs("useAccelerometer", isChecked);
				IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			}
		});
		return view;
	}

	private void SavePrefs(String key, boolean value)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
}
