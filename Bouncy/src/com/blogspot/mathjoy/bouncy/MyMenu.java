package com.blogspot.mathjoy.bouncy;

import java.sql.Savepoint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	Spinner ballColor;
	SeekBar seekGravity;
	SeekBar seekBounceLevel;
	TextView displayGravity;
	TextView displayBounceLevel;
	String[] colors =
	{ "red", "orange", "yellow", "green", "blue", "purple", "pink", "brown", "white", "gray" };
	static String pickedColor;
	static float gravity = 1;
	static float bounceLevel = 1;
	boolean gameReset = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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
		seekGravity.setProgress((int) (gravity * 100.0));
		seekBounceLevel.setProgress((int) (bounceLevel * 100.0));
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
		pickedColor = (String) ballColor.getSelectedItem();
		gravity = (float) (seekGravity.getProgress() / 100.0);
		bounceLevel = (float) (seekBounceLevel.getProgress() / 100.0);
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
			gravity = (float) (seekBar.getProgress() / 100.0);
			Float temp = gravity;
			displayGravity.setText(temp.toString());
		}
		if (seekBar.equals(seekBounceLevel))
		{
			bounceLevel = (float) (seekBar.getProgress() / 100.0);
			Float temp = bounceLevel;
			displayBounceLevel.setText(temp.toString());
		}
	}

	public void gameReset(View v)
	{
		gameReset = true;
		Button temp = (Button) v;
		temp.setText("Game Cleared!");
	}

	public void settingsReset(View v)
	{
		seekGravity.setProgress(100);
		seekBounceLevel.setProgress(100);
		ballColor.setSelection(0);
	}

	private void LoadPrefs()
	{
		SharedPreferences sp = getSharedPreferences("settings", 0);
		pickedColor = sp.getString("selectedColor", "red");
		gravity = sp.getFloat("gravityValue", 1);
		bounceLevel = sp.getFloat("bounceLevelValue", 1);
	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = getSharedPreferences("settings", 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	private void SavePrefs(String key, String value)
	{
		SharedPreferences sp = getSharedPreferences("settings", 0);
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
}
