package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener, OnClickListener
{
	// public static MediaPlayer bounce;
	public static SoundPool spool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	public static int bounce;
	public static int button;
	public static float buttonVolume = (float) 1;
	Intent intent;
	String pickedColor;
	int[] possibleColors =
	{ Color.RED, Color.rgb(225, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames =
	{ "red", "orange", "yellow", "green", "blue", "purple", "pink", "brown", "white", "gray" };
	static boolean justOpened = true;
	public static ImageButton ball;
	public static ImageButton platform;
	public static ImageButton settings;
	public static ImageButton undo;
	public ImageButton buttonDown;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// bounce = MediaPlayer.create(this, R.raw.bounce);
		bounce = spool.load(this, R.raw.bounce, 1);
		button = spool.load(this, R.raw.button, 1);
		SharedPreferences sp = getSharedPreferences("settings", 0);
		pickedColor = sp.getString("selectedColor", "red");
		MyView.gAccelerationMultiplier = sp.getFloat("gravityValue", 100) / 100.0;
		MyView.bounceFactor = sp.getFloat("bounceLevelValue", 100) / 100.0;
		for (int i = 0; i < possibleColors.length; i++)
		{
			if (pickedColor.equals(possibleColorNames[i]))
			{
				MyView.ballColor = possibleColors[i];
			}
		}
		// if (justOpened)
		// {
		// justOpened = false;
		// } else
		// {
		// intent = getIntent();
		// if (intent.getExtras().getBoolean("isGameReset") == true)
		// {
		// MyView.platforms.clear();
		// }
		// }
		// MyView.mode = MyView.MODE_BALL;
		setContentView(R.layout.activity_main);
		ball = (ImageButton) findViewById(R.id.Ball);
		platform = (ImageButton) findViewById(R.id.Platform);
		settings = (ImageButton) findViewById(R.id.Settings);
		undo = (ImageButton) findViewById(R.id.Undo);
		settings.setBackgroundColor(Color.LTGRAY);
		undo.setBackgroundColor(Color.LTGRAY);
		if (MyView.mode == MyView.MODE_BALL)
		{
			MainActivity.ball.setBackgroundColor(Color.GRAY);
			MainActivity.platform.setBackgroundColor(Color.LTGRAY);
		} else if (MyView.mode == MyView.MODE_CREATE_PLATFORM)
		{
			MainActivity.platform.setBackgroundColor(Color.GRAY);
			MainActivity.ball.setBackgroundColor(Color.LTGRAY);
		}
		ball.setOnTouchListener(this);
		platform.setOnTouchListener(this);
		settings.setOnTouchListener(this);
		undo.setOnTouchListener(this);
		ball.setOnClickListener(this);
		platform.setOnClickListener(this);
		settings.setOnClickListener(this);
		undo.setOnClickListener(this);
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		moveTaskToBack(true);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	public void goToMenu(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		Intent intent = new Intent(this, MyMenu.class);
		startActivity(intent);
	}

	public void modeBall(View view)
	{
		if (MyView.mode != MyView.MODE_BALL)
		{
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			ball.setBackgroundColor(Color.GRAY);
			platform.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_BALL;
		}
	}

	public void modePlatform(View view)
	{
		if (MyView.mode != MyView.MODE_CREATE_PLATFORM)
		{
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			platform.setBackgroundColor(Color.GRAY);
			ball.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_CREATE_PLATFORM;
		}
	}

	// public void modeGrab(View view)
	// {
	// MyView.timeBetweenFrames = 20;
	// }
	//
	// public void modeDelete(View view)
	// {
	// MyView.timeBetweenFrames = 1000;
	// }
	public void undo(View view)
	{
		if (MyView.platforms != null)
		{
			if (MyView.platforms.size() > 0)
			{
				spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
				MyView.platforms.remove(MyView.platforms.size() - 1);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (!((v.equals(ball) && MyView.mode == MyView.MODE_BALL) || (v.equals(platform) && MyView.mode == MyView.MODE_CREATE_PLATFORM)))
		{
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				v.setBackgroundColor(Color.LTGRAY);
			} else
			{
				v.setBackgroundColor(Color.rgb(170, 170, 170));
			}
		}
		return false;
	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(ball))
		{
			modeBall(v);
		} else if (v.equals(platform))
		{
			modePlatform(v);
		} else if (v.equals(settings))
		{
			goToMenu(v);
		} else if (v.equals(undo))
		{
			undo(v);
		}
	}
}
