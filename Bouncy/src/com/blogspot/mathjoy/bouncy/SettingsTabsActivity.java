package com.blogspot.mathjoy.bouncy;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsTabsActivity extends FragmentActivity implements TabListener
{
	ActionBar actionBar;
	ViewPager viewPager;
	ZonesFragment zonesFragment;
	MainSettingsFragment mainSettFragment;
	public static Activity activity;
	float buttonVolume = IntroActivity.buttonVolume;
	public static boolean gameReset;
	boolean soundsOn;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		gameReset = false;
		setContentView(R.layout.activity_tab_settings);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mainSettFragment = new MainSettingsFragment();
		zonesFragment = new ZonesFragment();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{

			}
		});
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab1 = actionBar.newTab();
		tab1.setText("Main");
		tab1.setTabListener(this);

		ActionBar.Tab tab2 = actionBar.newTab();
		tab2.setText("Zones");
		tab2.setTabListener(this);

		ActionBar.Tab tab3 = actionBar.newTab();
		tab3.setText("More");
		tab3.setTabListener(this);

		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
		activity = this;

		if (IntroActivity.bounce == 0)
		{
			soundsOn = false;
		} else
		{
			soundsOn = true;
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (!IntroActivity.mp.isPlaying() && IntroActivity.musicOn)
		{
			IntroActivity.mp = MediaPlayer.create(this, R.raw.background);
			IntroActivity.mp.setLooping(true);
			IntroActivity.mp.start();
		}
		MyApplication.activityResumed();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MyApplication.activityPaused();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (!MyApplication.isActivityVisible())
		{
			IntroActivity.mp.stop();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_items, menu);
		if (!soundsOn)
		{
			menu.getItem(0).setIcon(R.drawable.muted_big);
		}
		if (!IntroActivity.musicOn)
		{
			menu.getItem(1).setIcon(R.drawable.music_off_big);
		}
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1)
	{
		viewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	public void goToSaveSettings(View v)
	{
		zonesFragment.goToSaveSettings(v);
	}

	public void goToLoadSettings(View v)
	{
		zonesFragment.goToLoadSettings(v);
	}

	public void goToDelSettings(View v)
	{
		zonesFragment.goToDelSettings(v);
	}

	public void renameConf(View v)
	{
		zonesFragment.renameConf(v);
	}

	public void gameReset(View v)
	{
		mainSettFragment.gameReset(v);
	}

	public void settingsReset(View v)
	{
		mainSettFragment.settingsReset(v);

	}

	private void goToGameServices()
	{
		Intent intent = new Intent(SettingsTabsActivity.activity, GameServicesActivity.class);
		SettingsTabsActivity.activity.startActivity(intent);
	}

	private void goToIntro()
	{
		Intent intent = new Intent(SettingsTabsActivity.activity, IntroActivity.class);
		SettingsTabsActivity.activity.startActivity(intent);
	}

	public void goToMainMenu(View v)
	{

	}

	//	private void goToGame()
	//	{
	//		IntroActivity.spool.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
	//		MainSettingsFragment.pickedColor = (String) MainSettingsFragment.ballColor.getSelectedItem();
	//		MainSettingsFragment.gravity = MainSettingsFragment.seekGravity.getProgress();
	//		MainSettingsFragment.bounceLevel = MainSettingsFragment.seekBounceLevel.getProgress();
	//		MainSettingsFragment.friction = MainSettingsFragment.seekFriction.getProgress();
	//		SavePrefs("selectedColor", MainSettingsFragment.pickedColor);
	//		SavePrefs("gravityValue", MainSettingsFragment.gravity);
	//		SavePrefs("bounceLevelValue", MainSettingsFragment.bounceLevel);
	//		SavePrefs("frictionValue", MainSettingsFragment.friction);
	//		Intent intent = new Intent(this, MainActivity.class);
	//		startActivity(intent);
	//		overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
	//	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	private void SavePrefs(String key, String value)
	{
		SharedPreferences sp = getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.toIntro:
			goToIntro();
			return true;
		case R.id.toGameStats:
			goToGameServices();
			return true;
		case R.id.toGameStatsWithIcon:
			goToGameServices();
			return true;
		case R.id.soundsOnActionBar:
			toggleSounds(item);
			return true;
		case R.id.musicOnActionBar:
			toggleMusic(item);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toggleSounds(MenuItem item)
	{
		if (!soundsOn)
		{
			IntroActivity.bounce = IntroActivity.originalBounce;
			IntroActivity.button = IntroActivity.originalButton;
			item.setIcon(R.drawable.sounds_on_big);
			soundsOn = true;
			IntroActivity.spool.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
		} else
		{
			IntroActivity.bounce = 0;
			IntroActivity.button = 0;
			item.setIcon(R.drawable.muted_big);
			soundsOn = false;
		}
	}

	private void toggleMusic(MenuItem item)
	{
		if (!IntroActivity.musicOn)
		{
			IntroActivity.mp = MediaPlayer.create(this, R.raw.background);
			IntroActivity.mp.setLooping(true);
			IntroActivity.mp.start();
			item.setIcon(R.drawable.music_on_big);
			IntroActivity.musicOn = true;
		} else
		{
			IntroActivity.mp.stop();
			item.setIcon(R.drawable.music_off_big);
			IntroActivity.musicOn = false;
		}
	}
}

class MyAdapter extends FragmentPagerAdapter
{

	public MyAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int pos)
	{
		Fragment f = null;
		if (pos == 0)
		{
			f = new MainSettingsFragment();
		} else if (pos == 1)
		{
			f = new ZonesFragment();
		} else if (pos == 2)
		{
			f = new MoreSettingsFragment();
		}
		return f;
	}

	@Override
	public int getCount()
	{
		return 3;
	}
}