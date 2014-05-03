package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;
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
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		chooseConf = (Spinner) findViewById(R.id.chooseConf);
		if (sp.getInt("numOfConfs", 0) > 0)
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
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		int n = -1;
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf.getSelectedItem().equals(sp.getString(i + "name", " ")))
			{
				n = i;
			}
		}
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		if (!sp.getString(n + "name", " ").equals(" "))
		{
			// for (int i = 0; i < sp.getInt("platformsSize", 0); i++)
			// {
			// platforms.add(new Platform(sp.getFloat(n + "platformStartX", 0), sp.getFloat(n + "platformStartY", 0), sp.getFloat(n + "platformEndX", 0), sp.getFloat(n + "platformEndY", 0)));
			// }
		}
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}
}
