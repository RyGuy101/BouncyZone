package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveConfActivity extends Activity
{
	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	EditText editName;
	Toast emptySpaceT;
	Toast blankT;
	boolean overwrite = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_save_conf);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		button = spool.load(this, R.raw.button, 1);
		editName = (EditText) findViewById(R.id.editConfName);
		emptySpaceT = Toast.makeText(this, "You must enter a name.", Toast.LENGTH_SHORT);
		blankT = Toast.makeText(this, "Please include visible characters in the name.", Toast.LENGTH_SHORT);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}

	public void saveConf(View v)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		String name = editName.getText().toString();
		int n = sp.getInt("numOfConfs", 0);
		overwrite = false;
		boolean foundDuplicate = false;
		for (int i = 0; i < n; i++)
		{
			if (sp.getString(i + "name", " ").equals(name))
			{
				foundDuplicate = true;
				emptySpaceT.cancel();
				blankT.cancel();
				createAlreadyExistsAlertDialogue(sp, name, n).show();
			}
		}
		if (!foundDuplicate)
		{
			doTheSaving(sp, name, n);
		}
	}

	private void doTheSaving(SharedPreferences sp, String name, int n)
	{
		boolean emptySpace = true;
		boolean tooMany = false;
		boolean foundAvailableIndex = false;
		SharedPreferences sps = getSharedPreferences(MyMenu.settingsSP, 0);

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
		if ((!emptySpace && !tooMany) || overwrite)
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
			edit.putInt(n + "frictionValue", (int) sps.getFloat("frictionValue", 100));
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
		} else if (emptySpace)
		{
			if (name.length() == 0)
			{
				// emptySpaceT.cancel();
				blankT.cancel();
				emptySpaceT.show();
			} else
			{
				emptySpaceT.cancel();
				// blankT.cancel();
				blankT.show();
			}
		} else if (tooMany)
		{
			emptySpaceT.cancel();
			blankT.cancel();
			createTooManyAlertDialogue().show();
		}
	}

	@Override
	public void onBackPressed()
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		emptySpaceT.cancel();
		blankT.cancel();
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

	private AlertDialog createTooManyAlertDialogue()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("You can't have more than 100 zones. Delete one or more in order to make room.").setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}

	private AlertDialog createAlreadyExistsAlertDialogue(final SharedPreferences sp, final String name, final int n)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Html.fromHtml("A zone named <b>" + name + "</b> already exists.\nWould you like to overwrite it?")).setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				overwrite = true;
				doTheSaving(sp, name, n);
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				overwrite = false;
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}
}
