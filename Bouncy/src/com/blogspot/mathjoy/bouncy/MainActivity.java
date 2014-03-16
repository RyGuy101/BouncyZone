package com.blogspot.mathjoy.bouncy;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// OLD MAINACTIVITY
public class MainActivity extends Activity// implements OnTouchListener
{
	Intent intent;
	String thisPickedColor;
	MyView v;
	static int theRealColor = Color.RED;
	int[] possibleColors =
	{ Color.RED, Color.rgb(225, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames =
	{ "red", "orange", "yellow", "green", "blue", "purple", "pink", "brown", "white", "gray" };
	static boolean justOpened = true;
	Button ball;
	// Button grab;
	// Button platform;
	// Button delete;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		v = (MyView) findViewById(R.id.myView);
		ball = (Button) findViewById(R.id.Ball);
		// grab = (Button) this.findViewById(R.id.Grab);
		// platform = (Button) this.findViewById(R.id.Platform);
		// delete = (Button) this.findViewById(R.id.Delete);
		if (justOpened)
		{
			justOpened = false;
			v.ballColor = Color.RED;
			v.gAccelerationMultiplier = 1;
			v.bounceFactor = 1;
		} else
		{
			intent = getIntent();
			thisPickedColor = intent.getExtras().getString("selectedColor");
			for (int i = 0; i < possibleColors.length; i++)
			{
				if (thisPickedColor.equals(possibleColorNames[i]))
				{
					theRealColor = possibleColors[i];
				}
			}
			v.ballColor = theRealColor;
			v.gAccelerationMultiplier = intent.getExtras().getDouble("gravityValue");
			v.bounceFactor = intent.getExtras().getDouble("bounceLevelValue");
			if (intent.getExtras().getBoolean("isGameReset") == true)
			{
				v.platforms.clear();
				v.mode = MyView.MODE_BALL;
			}
		}
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
//	private static final int RESULT_SETTINGS = 1;
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item)
//	{
//		switch (item.getItemId())
//		{
//		case R.id.menu_settings:
//			Intent i = new Intent(this, SettingsActivity.class);
//			startActivityForResult(i, RESULT_SETTINGS);
//			break;
//		}
//		return true;
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode)
//		{
//		case RESULT_SETTINGS:
//			showUserSettings();
//			break;
//		}
//	}
//
//	private void showUserSettings()
//	{
//		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//		StringBuilder builder = new StringBuilder();
//		builder.append("\n Username: " + sharedPrefs.getString("prefUsername", "NULL"));
//		builder.append("\n Send report:" + sharedPrefs.getBoolean("prefSendReport", false));
//		builder.append("\n Sync Frequency: " + sharedPrefs.getString("prefSyncFrequency", "NULL"));
//		TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
//		settingsTextView.setText(builder.toString());
//	}

	public void goToMenu(View v)
	{
		Intent intent = new Intent(this, MyMenu.class);
		startActivity(intent);
	}

	public void modeBall(View view)
	{
		v.mode = MyView.MODE_BALL;
	}

	public void modePlatform(View view)
	{
		v.mode = MyView.MODE_CREATE_PLATFORM;
	}

	public void modeGrab(View view)
	{
		v.timeBetweenFrames = 20;
	}

	public void modeDelete(View view)
	{
		v.timeBetweenFrames = 1000;
	}
	// @Override
	// public boolean onTouch(View view, MotionEvent event)
	// {
	// // TODO Auto-generated method stub
	// v.setTouchX(event.getX());
	// v.setTouchY(event.getY());
	// if (event.getAction() == MotionEvent.ACTION_DOWN)
	// {
	// v.setTouching(true);
	// } else if (event.getAction() == MotionEvent.ACTION_UP)
	// {
	// v.setTouching(false);
	// }
	// return true;
	// }
}
