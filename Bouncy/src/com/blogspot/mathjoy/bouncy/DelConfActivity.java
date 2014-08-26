package com.blogspot.mathjoy.bouncy;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DelConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	Spinner chooseConf;
	String[] confNames;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_del_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		chooseConf = (Spinner) findViewById(R.id.chooseDelConf);
		if (sp.getInt("numOfConfs", 0) > 0)
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, confNames);
			chooseConf.setAdapter(confNamesAd);
		} else
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[] { "You haven't saved any zones!" });
			chooseConf.setAdapter(confNamesAd);
		}
	}

	public void delConf(View v)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		int thisN = -1;
		String name = " ";
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf.getSelectedItem().equals(sp.getString(i + "name", " ")))
			{
				thisN = i;
				name = sp.getString(i + "name", " ");
			}
		}
		if (name != " ")
		{
			createDeleteAlertDialogue(name, sp, thisN).show();
		}
	}

	private void doTheDeleting(SharedPreferences sp, int thisN)
	{
		if (!sp.getString(thisN + "name", " ").equals(" "))
		{
			for (int n = thisN; n < sp.getInt("numOfConfs", 0) - 1; n++)
			{
				Editor edit = sp.edit();
				edit.putString(n + "name", sp.getString((n + 1) + "name", " "));
				edit.putFloat(n + "startBallX", sp.getFloat((n + 1) + "startBallX", 0));
				edit.putFloat(n + "startBallY", sp.getFloat((n + 1) + "startBallY", 0));
				edit.putFloat(n + "startBallXSpeed", sp.getFloat((n + 1) + "startBallXSpeed", 0));
				edit.putFloat(n + "startBallYSpeed", sp.getFloat((n + 1) + "startBallYSpeed", 0));
				edit.putInt(n + "gravityValue", sp.getInt((n + 1) + "gravityValue", 100));
				edit.putInt(n + "bounceLevelValue", sp.getInt((n + 1) + "bounceLevelValue", 100));
				edit.putInt(n + "frictionValue", sp.getInt((n + 1) + "frictionValue", 100));
				edit.putInt(n + "platformsSize", sp.getInt((n + 1) + "platformsSize", 0));
				for (int i = 0; i < sp.getInt((n + 1) + "platformsSize", 0); i++)
				{
					edit.putFloat(n + "platformStartX" + i, sp.getFloat((n + 1) + "platformStartX" + i, 0));
					edit.putFloat(n + "platformStartY" + i, sp.getFloat((n + 1) + "platformStartY" + i, 0));
					edit.putFloat(n + "platformEndX" + i, sp.getFloat((n + 1) + "platformEndX" + i, 0));
					edit.putFloat(n + "platformEndY" + i, sp.getFloat((n + 1) + "platformEndY" + i, 0));
				}
				edit.commit();
			}
			Editor edit2 = sp.edit();
			edit2.putString((sp.getInt("numOfConfs", 0) - 1) + "name", " ");
			edit2.putInt("numOfConfs", sp.getInt("numOfConfs", 1) - 1);
			edit2.commit();
			onBackPressed();
		}
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
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
		}
		return super.onOptionsItemSelected(item);
	}

	private AlertDialog createDeleteAlertDialogue(final String name, final SharedPreferences sp, final int thisN)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Html.fromHtml("Are you sure you want to delete <b>" + name + "</b>?")).setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				doTheDeleting(sp, thisN);
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}
}
