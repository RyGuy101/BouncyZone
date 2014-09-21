package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends Activity
{
	public static SoundPool spoolBounce = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	public static int bounce;
	public static View buttons;
	int[] possibleColors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		bounce = spoolBounce.load(this, R.raw.bounce, 1);
		buttons = findViewById(R.id.introButtons);
		SharedPreferences sp = getSharedPreferences("settings", 0);
		String pickedColor = sp.getString("selectedColor", "Red");
		for (int i = 0; i < possibleColors.length; i++)
		{
			if (pickedColor.equals(possibleColorNames[i]))
			{
				MyView.ballColor = possibleColors[i];
			}
		}
	}

	public void goToGame(View v)
	{
		IntroView.startBallY = IntroView.originalStartBallY;
		IntroView.clearPlatforms();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void goToGameServices(View v)
	{
		Intent intent = new Intent(this, GameServicesActivity.class);
		startActivity(intent);
	}

	public void goToSettings(View v)
	{
		Intent intent = new Intent(this, SettingsTabs.class);
		startActivity(intent);
	}
}
