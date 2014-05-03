package com.blogspot.mathjoy.bouncy;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DelConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	Spinner chooseConf;
	String[] confNames;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_del_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		chooseConf = (Spinner) findViewById(R.id.chooseDelConf);
		if (sp.getInt("numOfConfs", 0) > 0)
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, confNames);
			chooseConf.setAdapter(confNamesAd);
		}
	}

	public void delConf(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}
}
