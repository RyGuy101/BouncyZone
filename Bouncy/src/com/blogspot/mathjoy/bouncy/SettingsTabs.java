package com.blogspot.mathjoy.bouncy;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SettingsTabs extends FragmentActivity implements TabListener
{
	ActionBar actionBar;
	ViewPager viewPager;
	ZonesFragment zonesFragment;
	public static Activity activity;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_tab_settings);
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

	public void overwriteConf(View v)
	{
		zonesFragment.overwriteConf(v);
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
			f = new Fragment();
		}
		if (pos == 1)
		{
			f = new ZonesFragment();
		} else if (pos == 2)
		{
			f = new TestFragmentB();
		}
		return f;
	}

	@Override
	public int getCount()
	{
		return 3;
	}
}