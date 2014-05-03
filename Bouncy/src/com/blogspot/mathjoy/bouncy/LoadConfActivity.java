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

public class LoadConfActivity extends Activity
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
		setContentView(R.layout.activity_load_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			confNames[i] = sp.getString(i + "name", "Untitled");
		}
		chooseConf = (Spinner) findViewById(R.id.chooseConf);
		if (confNames[0] != null)
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, confNames);
			chooseConf.setAdapter(confNamesAd);
		}
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void loadConf(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}
}
