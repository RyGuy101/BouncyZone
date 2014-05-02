package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	String[] confNames = new String[100];
	EditText editName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		editName = (EditText) findViewById(R.id.editConfName);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void saveConf(View v)
	{
		boolean save = false;
		String name = editName.getText().toString();
		SharedPreferences sp = getSharedPreferences(MyMenu.settingsSP, 0);
		if (sp.getString(name + "name", "1").equals("1"))
		{
			if (sp.getString(name + "name", "2").equals("2"))
			{
				save = true;
			}
		}
		if (save)
		{
			Editor edit = sp.edit();
			edit.putString(name + "name", name);
			edit.putFloat(name + "startBallX", MyView.startBallX);
			edit.putFloat(name + "startBallXY", MyView.startBallY);
			edit.putFloat(name + "startBallXSpeed", MyView.startBallXSpeed);
			edit.putFloat(name + "startBallYSpeed", MyView.startBallYSpeed);
			edit.putInt(name + "platformsSize", MyView.platforms.size());
			for (int i = 0; i < MyView.platforms.size(); i++)
			{
				edit.putFloat(name + "platformStartX" + i, MyView.platforms.get(i).getStartX());
				edit.putFloat(name + "platformStartY" + i, MyView.platforms.get(i).getStartY());
				edit.putFloat(name + "platformEndX" + i, MyView.platforms.get(i).getEndX());
				edit.putFloat(name + "platformEndY" + i, MyView.platforms.get(i).getEndY());
			}
			edit.commit();
			onBackPressed();
		} else
		{
			Toast.makeText(this, "You already have a configuration with that name", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}
}
