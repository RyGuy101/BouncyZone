package com.blogspot.mathjoy.bouncy;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
		} else
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]
			{ "You have no saved configurations!" });
			chooseConf.setAdapter(confNamesAd);
		}
	}

	public void delConf(View v)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		int thisN = -1;
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf.getSelectedItem().equals(sp.getString(i + "name", " ")))
			{
				thisN = i;
			}
		}
		if (!sp.getString(thisN + "name", " ").equals(" "))
		{
			for (int n = thisN; n < sp.getInt("numOfConfs", 0) - 1; n++)
			{
				Editor edit = sp.edit();
				edit.putString(n + "name", sp.getString((n + 1) + "name", " "));
				edit.putFloat(n + "startBallX", sp.getFloat((n + 1) + "startBallX", 0));
				edit.putFloat(n + "startBallY", sp.getFloat((n + 1) + "startBallY", 0));
				edit.putFloat(n + "startBallXSpeed", sp.getFloat((n + 1) + "startBallXSpeed", 0));
				edit.putFloat(n + "startBallYSpeed", sp.getFloat((n + 1) + "startBallYSpeed", 0));
				edit.putInt(n + "gravityValue", sp.getInt((n + 1) + "gravityValue", 100));
				edit.putInt(n + "bounceLevelValue", sp.getInt((n + 1) + "bounceLevelValue", 100));
				edit.putInt(n + "platformsSize", sp.getInt((n + 1) + "platformsSize", 0));
				for (int i = 0; i < sp.getInt((n + 1) + "platformsSize", 0); i++)
				{
					edit.putFloat(n + "platformStartX" + i, sp.getFloat((n + 1) + "platformStartX" + i, 0));
					edit.putFloat(n + "platformStartY" + i, sp.getFloat((n + 1) + "platformStartY" + i, 0));
					edit.putFloat(n + "platformEndX" + i, sp.getFloat((n + 1) + "platformEndX" + i, 0));
					edit.putFloat(n + "platformEndY" + i, sp.getFloat((n + 1) + "platformEndY" + i, 0));
				}
				edit.commit();
			}
			Editor edit2 = sp.edit();
			edit2.putString((sp.getInt("numOfConfs", 0) - 1) + "name", " ");
			edit2.putInt("numOfConfs", sp.getInt("numOfConfs", 1) - 1);
			edit2.commit();
			onBackPressed();
		}
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
