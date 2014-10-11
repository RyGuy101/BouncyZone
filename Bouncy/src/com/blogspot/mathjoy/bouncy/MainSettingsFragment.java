package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;

import com.blogspot.mathjoy.bouncy.R.id;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainSettingsFragment extends Fragment implements OnItemSelectedListener, OnSeekBarChangeListener, OnTouchListener
{
	public final static String settingsSP = "settings";
	public final static String dataSP = "data";
	float buttonVolume = IntroActivity.buttonVolume;
	static Spinner ballColor;
	static SeekBar seekGravity;
	static SeekBar seekBounceLevel;
	static SeekBar seekFriction;
	TextView displayGravity;
	TextView displayBounceLevel;
	TextView displayFriction;
	String[] colorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };
	int[] colors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	static String pickedColor;
	static int gravity = 100;
	static int bounceLevel = 100;
	static int friction = 100;
	ImageButton gameServices;
	ColorView colorView;
	Button resetGame;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_my_menu, container, false);
		ArrayAdapter<String> colorAd = new ArrayAdapter<String>(SettingsTabs.activity, android.R.layout.simple_spinner_item, colorNames);
		ballColor = (Spinner) view.findViewById(R.id.ballColor);
		ballColor.setAdapter(colorAd);
		ballColor.setOnItemSelectedListener(this);
		seekGravity = (SeekBar) view.findViewById(R.id.gravity);
		seekBounceLevel = (SeekBar) view.findViewById(R.id.bouceLevel);
		seekFriction = (SeekBar) view.findViewById(R.id.friction);
		seekGravity.setOnSeekBarChangeListener(this);
		seekBounceLevel.setOnSeekBarChangeListener(this);
		seekFriction.setOnSeekBarChangeListener(this);
		displayGravity = (TextView) view.findViewById(R.id.valueOfGravity);
		displayBounceLevel = (TextView) view.findViewById(R.id.valueOfBounceLevel);
		displayFriction = (TextView) view.findViewById(R.id.valueOfFriction);
		colorView = (ColorView) view.findViewById(R.id.colorView);
		gameServices = (ImageButton) view.findViewById(R.id.gameServices);
		gameServices.setBackgroundColor(Color.WHITE);
		gameServices.setOnTouchListener(this);
		LoadPrefs();
		seekGravity.setProgress(gravity);
		seekBounceLevel.setProgress(bounceLevel);
		seekFriction.setProgress(friction);
		displayFriction.setText("Friction: " + friction + "%");
		for (int i = 0; i <= colorNames.length - 1; i++)
		{
			if (colorNames[i].equals(pickedColor))
			{
				ballColor.setSelection(i);
				colorView.ballColor = colors[i];
			}
		}
		resetGame = (Button) view.findViewById(R.id.gameReset);
		if (SettingsTabs.gameReset)
		{
			resetGame.setText("Game Cleared!");
		}
		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		int boundary = -24;
		if (event.getAction() == MotionEvent.ACTION_UP || event.getX() < boundary || event.getX() > v.getWidth() - boundary || event.getY() < v.getY() + boundary || event.getY() > v.getBottom() - boundary)
		{
			v.setBackgroundColor(Color.WHITE);
		} else
		{
			v.setBackgroundColor(Color.LTGRAY);
		}
		return false;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2)
	{
		if (seekBar.equals(seekGravity))
		{
			gravity = seekBar.getProgress();
			Float temp = (float) (gravity / 100.0);
			// gravity = temp;
			displayGravity.setText("Gravity: " + temp.toString());
		}
		if (seekBar.equals(seekBounceLevel))
		{
			bounceLevel = seekBar.getProgress();
			Float temp = (float) (bounceLevel / 100.0);
			// bounceLevel = temp;
			displayBounceLevel.setText("Bounce: " + temp.toString());
		}
		if (seekBar.equals(seekFriction))
		{
			friction = seekBar.getProgress();
			displayFriction.setText("Friction: " + friction + "%");
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		if (seekBar.equals(seekGravity))
		{
			SavePrefs("gravityValue", MainSettingsFragment.gravity);
		}
		if (seekBar.equals(seekBounceLevel))
		{
			SavePrefs("bounceLevelValue", MainSettingsFragment.bounceLevel);
		}
		if (seekBar.equals(seekFriction))
		{
			SavePrefs("frictionValue", MainSettingsFragment.friction);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		for (int i = 0; i <= colorNames.length - 1; i++)
		{
			if (colorNames[i].equals(ballColor.getSelectedItem()))
			{
				colorView.ballColor = colors[i];
				SavePrefs("selectedColor", colorNames[i]);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub

	}

	public void gameReset(View v)
	{
		if (!SettingsTabs.gameReset)
		{
			IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
			MyView.clearPlatforms();
			MyView.reset();
			MyView.oldPlatforms.clear();
			MyView.ball.setVelocity(new Vec2(0, 0));
			MyView.mode = MyView.MODE_BALL;
			MyView.startBallXSpeed = 0;
			MyView.startBallYSpeed = 0;
			SettingsTabs.gameReset = true;
			resetGame = (Button) v;
			resetGame.setText("Game Cleared!");
		}
	}

	public void settingsReset(View v)
	{
		IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
		seekGravity.setProgress(100);
		seekBounceLevel.setProgress(100);
		seekFriction.setProgress(100);
		SavePrefs("gravityValue", seekGravity.getProgress());
		SavePrefs("bounceLevelValue", seekBounceLevel.getProgress());
		SavePrefs("frictionValue", seekFriction.getProgress());
	}

	private void LoadPrefs()
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(settingsSP, 0);
		pickedColor = sp.getString("selectedColor", "red");
		gravity = (int) sp.getFloat("gravityValue", 100);
		bounceLevel = (int) sp.getFloat("bounceLevelValue", 100);
		friction = (int) sp.getFloat("frictionValue", 100);
	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	private void SavePrefs(String key, String value)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public void goToGameServices(View v)
	{
		IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
		Intent intent = new Intent(SettingsTabs.activity, GameServicesActivity.class);
		SettingsTabs.activity.startActivity(intent);
	}
}
