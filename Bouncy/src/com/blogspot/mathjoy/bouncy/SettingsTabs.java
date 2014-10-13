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
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class SettingsTabs extends FragmentActivity implements TabListener
{
	ActionBar actionBar;
	ViewPager viewPager;
	ZonesFragment zonesFragment;
	MainSettingsFragment mainSettFragment;
	public static Activity activity;
	float buttonVolume = IntroActivity.buttonVolume;
	public static boolean gameReset;

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

		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		activity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_items, menu);
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
		Intent intent = new Intent(SettingsTabs.activity, GameServicesActivity.class);
		SettingsTabs.activity.startActivity(intent);
	}

	private void goToIntro()
	{
		Intent intent = new Intent(SettingsTabs.activity, IntroActivity.class);
		SettingsTabs.activity.startActivity(intent);
	}

	public void goToMainMenu(View v)
	{

	}

	//	private void goToGame()
	//	{
	//		IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
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
		}
		return super.onOptionsItemSelected(item);
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
		}
		if (pos == 1)
		{
			f = new ZonesFragment();
		}
		return f;
	}

	@Override
	public int getCount()
	{
		return 2;
	}
}