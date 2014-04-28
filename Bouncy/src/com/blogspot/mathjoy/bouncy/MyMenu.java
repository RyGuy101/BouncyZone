package com.blogspot.mathjoy.bouncy;

import java.sql.Savepoint;
import java.util.ArrayList;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MyMenu extends Activity implements OnItemSelectedListener, OnSeekBarChangeListener
{
	public final String settingsSP = "settings";
	public final String dataSP = "data";
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	Spinner ballColor;
	SeekBar seekGravity;
	SeekBar seekBounceLevel;
	TextView displayGravity;
	TextView displayBounceLevel;
	String[] colors =
	{ "red", "orange", "yellow", "green", "blue", "purple", "pink", "brown", "white", "gray" };
	static String pickedColor;
	static int gravity = 100;
	static int bounceLevel = 100;
	boolean gameReset = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		setContentView(R.layout.activity_my_menu);
		ArrayAdapter<String> colorAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
		ballColor = (Spinner) findViewById(R.id.ballColor);
		ballColor.setAdapter(colorAd);
		ballColor.setOnItemSelectedListener(this);
		seekGravity = (SeekBar) findViewById(R.id.gravity);
		seekBounceLevel = (SeekBar) findViewById(R.id.bouceLevel);
		seekGravity.setOnSeekBarChangeListener(this);
		seekBounceLevel.setOnSeekBarChangeListener(this);
		displayGravity = (TextView) findViewById(R.id.valueOfGravity);
		displayBounceLevel = (TextView) findViewById(R.id.valueOfBounceLevel);
		LoadPrefs();
		seekGravity.setProgress(gravity);
		seekBounceLevel.setProgress(bounceLevel);
		for (int i = 0; i <= colors.length - 1; i++)
		{
			if (colors[i].equals(pickedColor))
			{
				ballColor.setSelection(i);
			}
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.my_menu, menu);
	// return true;
	// }
	public void goToGame(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		pickedColor = (String) ballColor.getSelectedItem();
		gravity = (seekGravity.getProgress());
		bounceLevel = (seekBounceLevel.getProgress());
		SavePrefs("selectedColor", pickedColor);
		SavePrefs("gravityValue", gravity);
		SavePrefs("bounceLevelValue", bounceLevel);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("isGameReset", gameReset);
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// spool.play(button, 1, 1, 0, 0, 1);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if (seekBar.equals(seekGravity))
		{
			gravity = seekBar.getProgress();
			Float temp = (float) (gravity / 100.0);
			// gravity = temp;
			displayGravity.setText(temp.toString());
		}
		if (seekBar.equals(seekBounceLevel))
		{
			bounceLevel = seekBar.getProgress();
			Float temp = (float) (bounceLevel / 100.0);
			// bounceLevel = temp;
			displayBounceLevel.setText(temp.toString());
		}
	}

	public void gameReset(View v)
	{
		if (!gameReset)
		{
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			MyView.platforms.clear();
			gameReset = true;
			Button temp = (Button) v;
			temp.setText("Game Cleared!");
		}
	}

	public void settingsReset(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		seekGravity.setProgress(100);
		seekBounceLevel.setProgress(100);
		ballColor.setSelection(0);
	}

	private void LoadPrefs()
	{
		SharedPreferences sp = getSharedPreferences(settingsSP, 0);
		pickedColor = sp.getString("selectedColor", "red");
		gravity = (int) sp.getFloat("gravityValue", 100);
		bounceLevel = (int) sp.getFloat("bounceLevelValue", 100);
	}

	private void SavePrefs(String key, double value)
	{
		SharedPreferences sp = getSharedPreferences(settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, (float) value);
		edit.commit();
	}

	// private void SavePrefs(String key, int value)
	// {
	// SharedPreferences sp = getSharedPreferences(settingsSP, 0);
	// Editor edit = sp.edit();
	// edit.putInt(key, value);
	// edit.commit();
	// }
	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = getSharedPreferences(settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	private void SavePrefs(String key, String value)
	{
		SharedPreferences sp = getSharedPreferences(settingsSP, 0);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
	}

	@Override
	public void onBackPressed()
	{
		goToGame(new View(this));
	}

	private void saveCurrentConfiguration(String name)
	{
		SharedPreferences sp = getSharedPreferences(dataSP, 0);
		Editor edit = sp.edit();
		edit.putString(name + "name", name);
		edit.putFloat(name + "startBallX", MyView.startBallX);
		edit.putFloat(name + "startBallXY", MyView.startBallY);
		edit.putFloat(name + "startBallXSpeed", MyView.startBallXSpeed);
		edit.putFloat(name + "startBallYSpeed", MyView.startBallYSpeed);
		edit.putInt(name + "platformsSize", MyView.platforms.size());
		for (int i = 0; i < MyView.platforms.size(); i++)
		{
			edit.putFloat(name + "platformStartX" + i, MyView.platforms.get(i).getStartX());
			edit.putFloat(name + "platformStartY" + i, MyView.platforms.get(i).getStartY());
			edit.putFloat(name + "platformEndX" + i, MyView.platforms.get(i).getEndX());
			edit.putFloat(name + "platformEndY" + i, MyView.platforms.get(i).getEndY());
		}
		edit.commit();
	}

	private Configuration loadConfiguration()
	{
		SharedPreferences sp = getSharedPreferences(dataSP, 0);
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		for (int i = 0; i < sp.getInt("platformsSize", 0); i++)
		{
			platforms.add(new Platform(sp.getFloat("platformStartX", 0), sp.getFloat("platformStartY", 0), sp.getFloat("platformEndX", 0), sp.getFloat("platformEndY", 0)));
		}
		return new Configuration(sp.getString("name", "Untitled"), sp.getFloat("startBallX", 0), sp.getFloat("startBallY", 0), sp.getFloat("startBallXSpeed", 0), sp.getFloat("startBallYSpeed", 0), platforms);
	}
}
