package com.blogspot.mathjoy.bouncy;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameServicesActivity extends BaseGameActivity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	TextView numBouncesText;
	TextView otd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_game_services);
		button = spool.load(this, R.raw.button, 1);
		numBouncesText = (TextView) findViewById(R.id.numBouncesText);
		otd = (TextView) findViewById(R.id.otdText);
		SharedPreferences sp = getSharedPreferences(MainActivity.GAME_SP, 0);
		int numBounces = sp.getInt("numBounces", 0);
		if (numBounces != 0)
		{
			if (numBounces == 1)
			{
				numBouncesText.setText("You have bounced the ball 1 time!");
				otd.setText("(on this device)");
			} else
			{
				numBouncesText.setText("You have bounced the ball " + numBounces + " times!");
				otd.setText("(on this device)");
			}
		}

	}

	@Override
	public void onSignInFailed()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onSignInSucceeded()
	{
		SharedPreferences sp = getSharedPreferences(MainActivity.GAME_SP, 0);
		int numBounces = sp.getInt("numBounces", 0);
		setStepsOfAchivement(R.string.achievement_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_super_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_mega_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_hyper_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_bouncy_king, (int) (numBounces / 10.0));
	}

	public void showAchievements(View v)
	{
		if (isSignedIn())
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
		} else
		{
			showAlert("Please sign in to view achievements.");
		}
	}

	//	public void showLeaderboards(View v)
	//	{
	//		if (isSignedIn())
	//		{
	//			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), RC_UNUSED);
	//		} else
	//		{
	//			showAlert("Please sign in to view leaderboards.");
	//		}
	//	}

	public void unlockAchievement(int id)
	{
		Games.Achievements.unlock(getApiClient(), getString(id));
	}

	public void incrementAchievement(int id, int stepsToAdd)
	{
		Games.Achievements.increment(getApiClient(), getString(id), stepsToAdd);
	}

	public void updateLeaderboard(int id, int score)
	{
		Games.Leaderboards.submitScore(getApiClient(), getString(id), score);
	}

	public void setStepsOfAchivement(int id, int steps)
	{
		Games.Achievements.setSteps(getApiClient(), getString(id), steps);
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		}
		return super.onOptionsItemSelected(item);
	}
}
