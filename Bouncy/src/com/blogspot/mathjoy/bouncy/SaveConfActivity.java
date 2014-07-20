package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveConfActivity extends Activity implements TextWatcher
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	String[] confNames = new String[100];
	EditText editName;
	Toast alreadyExistsT;
	Toast emptySpaceT;
	Toast blankT;
	Toast tooManyT;
	boolean override = false;
	String repeatedName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		editName = (EditText) findViewById(R.id.editConfName);
		editName.addTextChangedListener(this);
		alreadyExistsT = Toast.makeText(this, "You already have a configuration with that name. Click the save button again to overwrite it.", Toast.LENGTH_LONG);
		emptySpaceT = Toast.makeText(this, "You must enter a name.", Toast.LENGTH_SHORT);
		blankT = Toast.makeText(this, "You must include visible characters.", Toast.LENGTH_SHORT);
		tooManyT = Toast.makeText(this, "You can't have more than 100 configurations.", Toast.LENGTH_SHORT);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void saveConf(View v)
	{
		boolean alreadyExists = false;
		boolean emptySpace = true;
		boolean tooMany = false;
		boolean foundAvailableIndex = false;
		String name = editName.getText().toString();
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		SharedPreferences sps = getSharedPreferences(MyMenu.settingsSP, 0);
		int n = sp.getInt("numOfConfs", 0);
		for (int i = 0; i < n; i++)
		{
			if (sp.getString(i + "name", " ").equals(name))
			{
				alreadyExists = true;
			}
		}
		for (int i = 0; i < name.length(); i++)
		{
			if (name.charAt(i) != ' ' && name.charAt(i) != '	')
			{
				emptySpace = false;
			}
		}
		if (n >= 100)
		{
			tooMany = true;
		}
		if (!repeatedName.equals(name))
		{
			override = false;
		}
		if ((!alreadyExists && !emptySpace && !tooMany) || override)
		{
			for (int i = 0; i < n; i++)
			{
				if (sp.getString(i + "name", " ").equals(name))
				{
					n = i;
					foundAvailableIndex = true;
					break;
				} else if (sp.getString(i + "name", " ").equals(" "))
				{
					n = i;
					foundAvailableIndex = true;
				}
			}
			Editor edit = sp.edit();
			edit.putString(n + "name", name);
			edit.putFloat(n + "startBallX", MyView.startBallX);
			edit.putFloat(n + "startBallY", MyView.startBallY);
			edit.putFloat(n + "startBallXSpeed", MyView.startBallXSpeed);
			edit.putFloat(n + "startBallYSpeed", MyView.startBallYSpeed);
			edit.putInt(n + "gravityValue", (int) sps.getFloat("gravityValue", 100));
			edit.putInt(n + "bounceLevelValue", (int) sps.getFloat("bounceLevelValue", 100));
			edit.putInt(n + "platformsSize", MyView.platforms.size());
			for (int i = 0; i < MyView.platforms.size(); i++)
			{
				edit.putFloat(n + "platformStartX" + i, MyView.platforms.get(i).getStartX());
				edit.putFloat(n + "platformStartY" + i, MyView.platforms.get(i).getStartY());
				edit.putFloat(n + "platformEndX" + i, MyView.platforms.get(i).getEndX());
				edit.putFloat(n + "platformEndY" + i, MyView.platforms.get(i).getEndY());
			}
			if (!foundAvailableIndex)
			{
				edit.putInt("numOfConfs", n + 1);
			}
			edit.commit();
			onBackPressed();
		} else if (alreadyExists)
		{
			override = true;
			repeatedName = name;
			// alreadyExistsT.cancel();
			emptySpaceT.cancel();
			blankT.cancel();
			tooManyT.cancel();
			alreadyExistsT.show();
		} else if (emptySpace)
		{
			if (name.length() == 0)
			{
				alreadyExistsT.cancel();
				// emptySpaceT.cancel();
				blankT.cancel();
				tooManyT.cancel();
				emptySpaceT.show();
			} else
			{
				alreadyExistsT.cancel();
				emptySpaceT.cancel();
				// blankT.cancel();
				tooManyT.cancel();
				blankT.show();
			}
		} else if (tooMany)
		{
			alreadyExistsT.cancel();
			emptySpaceT.cancel();
			blankT.cancel();
			// tooManyT.cancel();
			tooManyT.show();
		}
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		alreadyExistsT.cancel();
		emptySpaceT.cancel();
		blankT.cancel();
		super.onBackPressed();
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		override = false;
	}
}
