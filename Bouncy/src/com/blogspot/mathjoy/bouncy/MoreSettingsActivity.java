package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreSettingsActivity extends Activity
{

	public static SoundPool spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
	public static int button;
	float buttonVolume = MainActivity.buttonVolume;
	boolean saveIsOpen = false;
	LinearLayout saveLayout;
	Button save;
	Button overwriteButton;
	Button load;
	Button delete;
	AlertDialog saveConfAlert;

	EditText editName = null;
	Toast emptySpaceT;
	Toast blankT;
	boolean overwrite = false;

	ListView chooseConf;
	String[] confNames;
	private boolean noZones = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_more_settings);
		button = spool.load(this, R.raw.button, 1);
		saveLayout = null;
		save = (Button) findViewById(R.id.goToSaveConf);
		overwriteButton = (Button) findViewById(R.id.overwriteConf);
		load = (Button) findViewById(R.id.goToLoadConf);
		delete = (Button) findViewById(R.id.goToDelConf);
		emptySpaceT = Toast.makeText(this, "You must enter a name.", Toast.LENGTH_SHORT);
		blankT = Toast.makeText(this, "You must include visible characters in the name.", Toast.LENGTH_SHORT);
		chooseConf = (ListView) findViewById(R.id.chooseConf);
		chooseConf.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id)
			{
				String name = (String) chooseConf.getItemAtPosition(position);
				if (!noZones)
				{
					overwriteButton.setText(Html.fromHtml("Overwrite <b>" + name));
					load.setText(Html.fromHtml("Load <b>" + name));
					delete.setText(Html.fromHtml("Delete <b>" + name));
				}
			}
		});
		refreshZoneList();
		chooseConf.setItemChecked(0, true);
		String name = (String) chooseConf.getItemAtPosition(0);
		overwriteButton.setText(Html.fromHtml("Overwrite <b>" + name));
		load.setText(Html.fromHtml("Load <b>" + name));
		delete.setText(Html.fromHtml("Delete <b>" + name));
	}

	private void refreshZoneList()
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		if (sp.getInt("numOfConfs", 0) > 0)
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, confNames);
			chooseConf.setAdapter(confNamesAd);
			chooseConf.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			noZones = false;
		} else
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, new String[] { "You haven't saved any zones!" });
			chooseConf.setAdapter(confNamesAd);
			noZones = true;
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		//		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_left);
	}

	public void goToSaveSettings(View v)
	{
		//		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
		//		if (saveIsOpen)
		//		{
		//			closeSave();
		//		} else
		//		{
		//			setupSave();
		//			saveIsOpen = true;
		//		}
		saveConfAlert = createSaveConfAlertDialogue();
		saveConfAlert.show();
	}

	private void closeSave()
	{
		saveLayout.removeAllViews();
		saveIsOpen = false;
		save.setText(changeToDownArrows(save.getText().toString()));
	}

	private void setupSave()
	{
		editName = new EditText(this);
		save.setText(changeToUpArrows(save.getText().toString()));
		editName.setHint("Give your zone a name!");
		editName.setInputType(InputType.TYPE_CLASS_TEXT);
		editName.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editName.requestFocus();
		emptySpaceT = Toast.makeText(this, "You must enter a name.", Toast.LENGTH_SHORT);
		blankT = Toast.makeText(this, "You must include visible characters in the name.", Toast.LENGTH_SHORT);
		saveLayout.addView(editName);
		TextView tv = new TextView(this);
		tv.setText("The ball color, gravity, bounce, and friction will all be saved with your zone.");
		saveLayout.addView(tv);
		saveLayout.addView(new TextView(this));
		Button saveButton = new Button(this);
		saveButton.setText("Save!");
		saveButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				saveConf(editName.getText().toString());
			}
		});
		saveLayout.addView(saveButton);
	}

	public void goToLoadSettings(View v)
	{
		loadConf();
	}

	public void goToDelSettings(View v)
	{
		spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
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

	public void overwriteConf(View v)
	{

	}

	private String changeToDownArrows(String str)
	{
		String newString = "";
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == getString(R.string.up_arrow).charAt(0))
			{
				newString += getString(R.string.down_arrow);
			} else
			{
				newString += str.charAt(i);
			}
		}
		return newString;

	}

	private String changeToUpArrows(String str)
	{
		String newString = "";
		for (int i = 0; i < str.length(); i++)
		{
			if (str.charAt(i) == getString(R.string.down_arrow).charAt(0))
			{
				newString += getString(R.string.up_arrow);
			} else
			{
				newString += str.charAt(i);
			}
		}
		return newString;

	}

	private void saveConf(String name)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
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
				saveConfAlert.dismiss();
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
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			saveConfAlert.dismiss();
			refreshZoneList();
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
			saveConfAlert.dismiss();
			createTooManyAlertDialogue().show();
		}
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
				saveConfAlert.show();
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}

	private AlertDialog createSaveConfAlertDialogue()
	{
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage("Save the current zone as...").setPositiveButton("Save!", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		});
		if (editName == null)
		{
			editName = new EditText(this);
			editName.setHint("Give your zone a name!");
			editName.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		b.setView(editName);
		final AlertDialog alert = b.create();

		alert.setOnShowListener(new DialogInterface.OnShowListener()
		{
			@Override
			public void onShow(DialogInterface dialog)
			{
				Button saveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
				saveButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						saveConf(editName.getText().toString());
					}
				});
			}
		});
		return alert;
	}

	private void loadConf()
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.dataSP, 0);
		int n = -1;
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf.getSelectedItem().equals(sp.getString(i + "name", " ")))
			{
				n = i;
			}
		}
		if (!sp.getString(n + "name", " ").equals(" "))
		{
			spool.play(button, buttonVolume, buttonVolume, 0, 0, 1);
			MyView.ball.setPosition(new Vec2(sp.getFloat(n + "startBallX", 0), sp.getFloat(n + "startBallY", 0)));
			MyView.ball.setVelocity(new Vec2(sp.getFloat(n + "startBallXSpeed", 0), sp.getFloat(n + "startBallYSpeed", 0)));
			MyView.startBallX = sp.getFloat(n + "startBallX", 0);
			MyView.startBallY = sp.getFloat(n + "startBallY", 0);
			MyView.startBallXSpeed = sp.getFloat(n + "startBallXSpeed", 0);
			MyView.startBallYSpeed = sp.getFloat(n + "startBallYSpeed", 0);
			SavePrefs("gravityValue", sp.getInt(n + "gravityValue", 100));
			SavePrefs("bounceLevelValue", sp.getInt(n + "bounceLevelValue", 100));
			SavePrefs("frictionValue", sp.getInt(n + "frictionValue", 100));

			MyView.clearPlatforms();
			for (int i = 0; i < sp.getInt(n + "platformsSize", 0); i++)
			{
				MyView.platforms.add(new Platform(BodyType.STATIC, sp.getFloat(n + "platformStartX" + i, 0), sp.getFloat(n + "platformStartY" + i, 0), sp.getFloat(n + "platformEndX" + i, 0), sp.getFloat(n + "platformEndY" + i, 0), 0, 1, 0));
			}
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("fromLoad", true);
			startActivity(intent);
		}
	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = getSharedPreferences(MyMenu.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}
}
