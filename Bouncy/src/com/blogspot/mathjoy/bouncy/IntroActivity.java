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
import android.widget.ImageButton;
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
	ImageButton soundsTog;
	private static boolean firstTime = true;
	public static MediaPlayer mp;
	private boolean soundsOn = true;

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
		soundsTog = (ImageButton) findViewById(R.id.toggleSounds);
		soundsTog.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!soundsOn)
				{
					soundsOn = true;
					bounce = originalBounce;
					button = originalButton;
					soundsTog.setBackgroundResource(R.drawable.sounds_on);
				} else
				{
					soundsOn = false;
					bounce = 0;
					button = 0;
					soundsTog.setBackgroundResource(R.drawable.muted);
				}
				spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			}
		});
		soundsOn = bounce != 0;
		if (!soundsOn)
		{
			soundsTog.setBackgroundResource(R.drawable.muted);
		}
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
		if (!firstTime)
		{
			IntroView.makeBallUnreal();
			IntroView.makePlatformsUnreal();
			MyView.makePlatformsReal();
			MyView.makeBallReal();
		}
		MyApplication.activityPaused();
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
		if (!mp.isPlaying())
		{
			IntroActivity.mp = MediaPlayer.create(this, R.raw.background);
			mp.setLooping(true);
			mp.start();
		}
		MyApplication.activityResumed();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (!MyApplication.isActivityVisible())
		{
			mp.stop();
		}
	}

	public void goToGameServices(View v)
	{
		Intent intent = new Intent(this, GameServicesActivity.class);
		startActivity(intent);
	}

	public void goToSettings(View v)
	{
		Intent intent = new Intent(this, SettingsTabsActivity.class);
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
