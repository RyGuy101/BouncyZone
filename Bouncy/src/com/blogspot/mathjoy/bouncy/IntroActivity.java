package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class IntroActivity extends BaseGameActivity
{
	public static SoundPool spool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	public static int originalBounce;
	public static int originalButton;
	public static int bounce;
	public static int button;
	public static float buttonVolume = 1;
	public static View buttons;
	int[] possibleColors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };
	ToggleButton soundsTog;
	private static boolean firstTime = true;
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mp = MediaPlayer.create(this, R.raw.background);
		mp.setLooping(true);
		mp.start();
		originalBounce = spool.load(this, R.raw.bounce, 1);
		originalButton = spool.load(this, R.raw.button, 1);
		if (firstTime)
		{
			bounce = originalBounce;
			button = originalButton;
			firstTime = false;
		}
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
		soundsTog = (ToggleButton) findViewById(R.id.toggleSounds);
		soundsTog.setChecked(true);
		soundsTog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					bounce = originalBounce;
					button = originalButton;
				} else
				{
					bounce = 0;
					button = 0;
				}
				spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			}
		});
		soundsTog.setChecked(bounce != 0);
		if (WorldManager.world == null)
		{
			WorldManager.setupWorld();
			WorldManager.world.setContactListener(new MainActivity());
		}
	}

	public void playSound(View v)
	{
		//		spoolButton.play(button, buttonVolume, buttonVolume, 0, 0, 1);
	}

	public void goToGame(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (!firstTime)
		{
			IntroView.makeBallUnreal();
			IntroView.makePlatformsUnreal();
			MyView.makePlatformsReal();
			MyView.makeBallReal();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		WorldManager.setGravity(new Vec2(0, 10));
		MyView.makeBallUnreal();
		MyView.makePlatformsUnreal();
		IntroView.makePlatformsReal();
		IntroView.makeBallReal();
		SharedPreferences sp = getSharedPreferences("settings", 0);
		String pickedColor = sp.getString("selectedColor", "Red");
		for (int i = 0; i < possibleColors.length; i++)
		{
			if (pickedColor.equals(possibleColorNames[i]))
			{
				MyView.ballColor = possibleColors[i];
			}
		}
		IntroView.intro = true;
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

	@Override
	public void onSignInFailed()
	{
	}

	@Override
	public void onSignInSucceeded()
	{
	}

	private void setStepsOfAchivement(int id, int steps)
	{
		if (isSignedIn())
		{
			Games.Achievements.setSteps(getApiClient(), getString(id), steps);
		}
	}

	public void updateStepsOfBounceAchievements(int numBounces)
	{
		//		try
		//		{
		//			setStepsOfAchivement(R.string.achievement_bouncy, numBounces);
		//		} catch (Exception e)
		//		{
		//		}
		//		try
		//		{
		//			setStepsOfAchivement(R.string.achievement_super_bouncy, numBounces);
		//		} catch (Exception e)
		//		{
		//		}
		//		try
		//		{
		//			setStepsOfAchivement(R.string.achievement_mega_bouncy, numBounces);
		//		} catch (Exception e)
		//		{
		//		}
		//		try
		//		{
		//			setStepsOfAchivement(R.string.achievement_hyper_bouncy, numBounces);
		//		} catch (Exception e)
		//		{
		//		}
		//		try
		//		{
		//		setStepsOfAchivement(R.string.achievement_bouncy_king, (int) (numBounces / 10.0));
		//		} catch (Exception e)
		//		{
		//		}
		getGameHelper();
		if (isSignedIn())
		{

		}
	}
}
