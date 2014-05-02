package com.blogspot.mathjoy.bouncy;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class LoadConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	String[] confNames = new String[100];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
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
