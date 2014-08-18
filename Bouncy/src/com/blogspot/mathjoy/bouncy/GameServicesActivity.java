package com.blogspot.mathjoy.bouncy;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.internal.constants.TimeSpan;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.GpsStatus.Listener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class GameServicesActivity extends BaseGameActivity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_game_services);
		button = spool.load(this, R.raw.button, 1);
	}

	@Override
	public void onSignInFailed()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onSignInSucceeded()
	{
		SharedPreferences sp = getSharedPreferences(MainActivity.gameSP, 0);
		updateLeaderboard(R.string.leaderboard_bounces, sp.getInt("numBounces", 0));
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

	public void showLeaderboards(View v)
	{
		if (isSignedIn())
		{
			startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), RC_UNUSED);
		} else
		{
			showAlert("Please sign in to view leaderboards.");
		}
	}

	public void unlockAchievement(int id)
	{
		Games.Achievements.unlock(getApiClient(), getString(id));
	}

	public void incrementAchievement(int id, int steps)
	{
		Games.Achievements.increment(getApiClient(), getString(id), steps);
	}

	public void updateLeaderboard(int id, int score)
	{
		Games.Leaderboards.submitScore(getApiClient(), getString(id), score);
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
