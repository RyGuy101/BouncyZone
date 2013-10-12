package com.blogspot.mathjoy.bouncy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MyMenu extends Activity implements OnItemSelectedListener, OnSeekBarChangeListener
{
	Spinner ballColor;
	SeekBar seekGravity;
	TextView displayGravity;
	String[] colors =
	{ "red", "orange", "yellow", "green", "blue", "purple", "pink", "brown", "white", "gray" };
	static String pickedColor;
	static double gravity;

	// public static final String NAME = "com.blogspot.mathjoy.bouncy";
	// TextView textBallColor;
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
		seekGravity.setOnSeekBarChangeListener(this);
		displayGravity = (TextView) findViewById(R.id.valueOfGravity);
		// textBallColor = (TextView) findViewById(R.id.textBallColor);
		for (int i = 0; i <= colors.length - 1; i++)
		{
			if (colors[i] == pickedColor)
			{
				ballColor.setSelection(i);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_menu, menu);
		return true;
	}

	public void goToGame(View v)
	{
		gravity = seekGravity.getProgress() / 100.0;
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("selectedColor", pickedColor);
		intent.putExtra("gravityValue", gravity);
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		pickedColor = (String) ballColor.getSelectedItem();
		// textBallColor.setText(pickedColor);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		// TODO Auto-generated method stub
		if (seekBar.equals(seekGravity))
		{
			gravity = seekBar.getProgress() / 100.0;
			Double temp = gravity;
			displayGravity.setText(temp.toString());
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
	}
}