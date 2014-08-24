package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class MoreSettingsActivity extends Activity
{

	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_left);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_more_settings);
		button = spool.load(this, R.raw.button, 1);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
//		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_left);
	}

	public void goToSaveSettings(View v)
	{
		overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		Intent intent = new Intent(this, SaveConfActivity.class);
		startActivity(intent);
	}

	public void goToLoadSettings(View v)
	{
		overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		Intent intent = new Intent(this, LoadConfActivity.class);
		startActivity(intent);
	}

	public void goToDelSettings(View v)
	{
		overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		Intent intent = new Intent(this, DelConfActivity.class);
		startActivity(intent);
	}

}
