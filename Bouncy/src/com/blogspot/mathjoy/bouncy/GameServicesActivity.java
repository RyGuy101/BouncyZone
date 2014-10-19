package com.blogspot.mathjoy.bouncy;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameServicesActivity extends BaseGameActivity implements ResultCallback<LoadAchievementsResult>
{
	float buttonVolume = IntroActivity.buttonVolume;
	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	TextView numBouncesText;
	TextView otd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_game_services);
		numBouncesText = (TextView) findViewById(R.id.numBouncesText);
		otd = (TextView) findViewById(R.id.otdText);
		SharedPreferences sp = getSharedPreferences(MainActivity.GAME_SP, 0);
		int numBounces = sp.getInt("numBounces", 0);
		if (numBounces != 0)
		{
			if (numBounces == 1)
			{
				numBouncesText.setText("You've bounced the ball 1 time!");
			} else
			{
				numBouncesText.setText("You've bounced the ball " + numBounces + " times!");
			}
		} else
		{
			otd.setVisibility(View.GONE);
		}

	}

	@Override
	public void onSignInFailed()
	{
	}

	@Override
	public void onSignInSucceeded()
	{
		Games.Achievements.load(getApiClient(), false).setResultCallback(this);
	}

	public void showAchievements(View v)
	{
		if (isSignedIn())
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
		} else
		{
			showAlert("You have to be signed in to view achievements.");
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResult(LoadAchievementsResult result)
	{
		SharedPreferences sp = getSharedPreferences(MainActivity.GAME_SP, 0);
		int numBounces = sp.getInt("numBounces", 0);
		Achievement bouncy = null;
		Achievement superBouncy = null;
		Achievement megaBouncy = null;
		Achievement hyperBouncy = null;
		Achievement bouncyKing = null;

		if (result != null)
		{
			if (result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_OK)
			{
				if (result.getAchievements() != null)
				{
					AchievementBuffer achievementBuffer = result.getAchievements();
					for (Achievement achievement : achievementBuffer)
					{
						if (achievement.getAchievementId().equals(getString(R.string.achievement_bouncy)))
						{
							bouncy = achievement;
						} else if (achievement.getAchievementId().equals(getString(R.string.achievement_super_bouncy)))
						{
							superBouncy = achievement;
						} else if (achievement.getAchievementId().equals(getString(R.string.achievement_mega_bouncy)))
						{
							megaBouncy = achievement;
						} else if (achievement.getAchievementId().equals(getString(R.string.achievement_hyper_bouncy)))
						{
							hyperBouncy = achievement;
						} else if (achievement.getAchievementId().equals(getString(R.string.achievement_bouncy_king)))
						{
							bouncyKing = achievement;
						}
					}
					if (numBounces > 0)
					{
						if (bouncy.getCurrentSteps() < numBounces)
						{
							setStepsOfAchivement(R.string.achievement_bouncy, numBounces);
						}
						if (superBouncy.getCurrentSteps() < numBounces)
						{
							setStepsOfAchivement(R.string.achievement_super_bouncy, numBounces);
						}
						if (megaBouncy.getCurrentSteps() < numBounces)
						{
							setStepsOfAchivement(R.string.achievement_mega_bouncy, numBounces);
						}
						if (hyperBouncy.getCurrentSteps() < numBounces)
						{
							setStepsOfAchivement(R.string.achievement_hyper_bouncy, numBounces);
						}
						if (bouncyKing.getCurrentSteps() < (int) (numBounces / 10.0) && numBounces >= 10)
						{
							setStepsOfAchivement(R.string.achievement_bouncy_king, (int) (numBounces / 10.0));
						}
					}
					achievementBuffer.close();
				}
			}
		}
	}
}
