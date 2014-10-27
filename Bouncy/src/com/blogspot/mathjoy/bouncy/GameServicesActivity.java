package com.blogspot.mathjoy.bouncy;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.Player;
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
	Button signIn;
	Button signOut;
	TextView googleInfo;
	TextView signInInfo;
	TextView hello;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_game_services);
		signIn = (Button) findViewById(R.id.signInButt);
		signOut = (Button) findViewById(R.id.signOutButt);
		signOut.setVisibility(View.GONE);
		googleInfo = (TextView) findViewById(R.id.googleInfo);
		signInInfo = (TextView) findViewById(R.id.signInInfo);
		hello = (TextView) findViewById(R.id.helloUser);
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
		signOut.setVisibility(View.GONE);
		signIn.setVisibility(View.VISIBLE);
		googleInfo.setText("Sign in to Google in order to\nunlock and view achievements.");
		googleInfo.setTextColor(Color.rgb(128, 128, 128));
		signInInfo.setText("Not signed in");
		hello.setText("Hello, anonymous user!");
	}

	@Override
	public void onSignInSucceeded()
	{
		signOut.setVisibility(View.VISIBLE);
		signIn.setVisibility(View.GONE);
		googleInfo.setText("You are signed in to Google\n");
		googleInfo.setTextColor(Color.BLACK);
		signInInfo.setText("");
		Games.Achievements.load(getApiClient(), false).setResultCallback(this);
		Player p = Games.Players.getCurrentPlayer(getApiClient());
		String displayName;
		if (p == null)
		{
			displayName = "unknown";
		} else
		{
			displayName = p.getDisplayName();
		}
		hello.setText("Hello, " + displayName + "!");
	}

	public void showAchievements(View v)
	{
		if (isSignedIn())
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
		} else
		{
			showAlert("Viewing achievements requires signing in to Google.");
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
	public void signOut(View v)
	{
		signOut();
		signOut.setVisibility(View.GONE);
		signIn.setVisibility(View.VISIBLE);
		googleInfo.setText("Sign in to Google in order to\nunlock and view achievements.");
		googleInfo.setTextColor(Color.rgb(128, 128, 128));
		signInInfo.setText("Not signed in");
		hello.setText("Hello, anonymous user!");
	}

	public void signIn(View v)
	{
		beginUserInitiatedSignIn();
	}

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
