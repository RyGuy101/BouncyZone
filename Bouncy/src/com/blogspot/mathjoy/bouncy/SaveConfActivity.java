package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
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
		alreadyExistsT = Toast.makeText(this, "You already have a configuration with that name. Click the save button again to override it.", Toast.LENGTH_LONG);
		emptySpaceT = Toast.makeText(this, "You must enter a name.", Toast.LENGTH_SHORT);
		blankT = Toast.makeText(this, "You must have other characters besides a space.", Toast.LENGTH_SHORT);
		tooManyT = Toast.makeText(this, "You can't have more than 100 configurations.", Toast.LENGTH_SHORT);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void saveConf(View v)
	{
		boolean alreadyExists = true;
		boolean emptySpace = true;
		boolean tooMany = false;
		String name = editName.getText().toString();
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		SharedPreferences sps = getSharedPreferences(MyMenu.settingsSP, 0);
		int numOfConfs = sp.getInt("numOfConfs", 0);
		if (sp.getString(name + "name", "1").equals("1"))
		{
			if (sp.getString(name + "name", "2").equals("2"))
			{
				alreadyExists = false;
			}
		}
		for (int i = 0; i < name.length(); i++)
		{
			if (name.charAt(i) != ' ' && name.charAt(i) != '	')
			{
				emptySpace = false;
			}
		}
		if (numOfConfs >= 100)
		{
			tooMany = true;
		}
		if (!repeatedName.equals(name))
		{
			override = false;
		}
		if ((!alreadyExists && !emptySpace && !tooMany) || override)
		{
			Editor edit = sp.edit();
			edit.putString(numOfConfs + "name", name);
			edit.putFloat(numOfConfs + "startBallX", MyView.startBallX);
			edit.putFloat(numOfConfs + "startBallXY", MyView.startBallY);
			edit.putFloat(numOfConfs + "startBallXSpeed", MyView.startBallXSpeed);
			edit.putFloat(numOfConfs + "startBallYSpeed", MyView.startBallYSpeed);
			edit.putInt(numOfConfs + "platformsSize", MyView.platforms.size());
			edit.putInt(numOfConfs + "gravityValue", (int) sps.getFloat("gravityValue", 100));
			edit.putInt(numOfConfs + "bounceLevelValue", (int) sps.getFloat("bounceLevelValue", 100));
			for (int i = 0; i < MyView.platforms.size(); i++)
			{
				edit.putFloat(numOfConfs + "platformStartX" + i, MyView.platforms.get(i).getStartX());
				edit.putFloat(numOfConfs + "platformStartY" + i, MyView.platforms.get(i).getStartY());
				edit.putFloat(numOfConfs + "platformEndX" + i, MyView.platforms.get(i).getEndX());
				edit.putFloat(numOfConfs + "platformEndY" + i, MyView.platforms.get(i).getEndY());
			}
			edit.putInt("numOfConfs", numOfConfs + 1);
			edit.commit();
			onBackPressed();
		} else if (alreadyExists)
		{
			override = true;
			repeatedName = name;
			alreadyExistsT.cancel();
			emptySpaceT.cancel();
			blankT.cancel();
			tooManyT.cancel();
			alreadyExistsT.show();
		} else if (emptySpace)
		{
			if (name.length() == 0)
			{
				alreadyExistsT.cancel();
				emptySpaceT.cancel();
				blankT.cancel();
				tooManyT.cancel();
				emptySpaceT.show();
			} else
			{
				alreadyExistsT.cancel();
				emptySpaceT.cancel();
				blankT.cancel();
				tooManyT.cancel();
				blankT.show();
			}
		} else if (tooMany)
		{
			alreadyExistsT.cancel();
			emptySpaceT.cancel();
			blankT.cancel();
			tooManyT.cancel();
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
}
