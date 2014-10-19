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
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class IntroActivity extends BaseGameActivity
{
	public static SoundPool spoolBounce = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	public static SoundPool spoolButton = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
	public static int bounce;
	public static int button;
	public static float buttonVolume = (float) 0.3;
	public static View buttons;
	int[] possibleColors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };
	ToggleButton soundsTog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		bounce = spoolBounce.load(this, R.raw.bounce, 1);
		button = spoolButton.load(this, R.raw.button, 1);
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
					bounce = spoolBounce.load(getApplicationContext(), R.raw.bounce, 1);
					button = spoolButton.load(getApplicationContext(), R.raw.button, 1);
				} else
				{
					bounce = 0;
					button = 0;
				}
			}
		});
		if (WorldManager.world == null)
		{
			WorldManager.setupWorld();
			WorldManager.world.setContactListener(new MainActivity());
		}
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
		IntroView.makeBallUnreal();
		IntroView.makePlatformsUnreal();
		MyView.makePlatformsReal();
		MyView.makeBallReal();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (WorldManager.world != null)
		{
			WorldManager.setGravity(new Vec2(0, 10));
		} else
		{
			WorldManager.setGravityButDontUpdateWorld(new Vec2(0, 10));
		}
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
