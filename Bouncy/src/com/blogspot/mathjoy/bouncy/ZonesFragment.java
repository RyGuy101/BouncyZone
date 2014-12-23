package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ZonesFragment extends Fragment
{
	float buttonVolume = IntroActivity.buttonVolume;
	boolean saveIsOpen = false;
	LinearLayout saveLayout;
	AlertDialog saveConfAlert = null;
	AlertDialog renameConfAlert;

	EditText editName;
	String emptySpaceS = "You must enter a name.";
	String blankS = "You must include visible characters in the name.";
	Toast emptySpaceT;
	Toast blankT;
	boolean overwrite = false;

	ListView chooseConf;
	String[] confNames;
	private static boolean noZones = false;

	Button loadButt;
	Button renameButt;
	Button deleteButt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_zones, container, false);
		saveLayout = null;
		chooseConf = (ListView) view.findViewById(R.id.chooseConf);
		loadButt = (Button) view.findViewById(R.id.goToLoadConf);
		renameButt = (Button) view.findViewById(R.id.renameConf);
		deleteButt = (Button) view.findViewById(R.id.goToDelConf);
		refreshZoneList();
		return view;
	}

	private void refreshZoneList()
	{
		if (chooseConf == null)
		{
			chooseConf = (ListView) SettingsTabs.activity.findViewById(R.id.chooseConf);
		}
		if (loadButt == null)
		{
			loadButt = (Button) SettingsTabs.activity.findViewById(R.id.goToLoadConf);
		}
		if (renameButt == null)
		{
			renameButt = (Button) SettingsTabs.activity.findViewById(R.id.renameConf);
		}
		if (deleteButt == null)
		{
			deleteButt = (Button) SettingsTabs.activity.findViewById(R.id.goToDelConf);
		}
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		confNames = new String[sp.getInt("numOfConfs", 0)];
		int i2 = 0;
		for (int i = sp.getInt("numOfConfs", 0) - 1; i >= 0; i--)
		{
			confNames[i2] = sp.getString(i + "name", " ");
			i2++;
		}
		if (sp.getInt("numOfConfs", 0) > 0)
		{
			Object checked = "";
			try
			{
				checked = chooseConf.getItemAtPosition(chooseConf.getCheckedItemPosition());
			} catch (Exception e)
			{
			}
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(SettingsTabs.activity, android.R.layout.simple_list_item_single_choice, confNames);
			chooseConf.setAdapter(confNamesAd);
			chooseConf.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			noZones = false;
			loadButt.setEnabled(true);
			renameButt.setEnabled(true);
			deleteButt.setEnabled(true);
			chooseConf.setItemChecked(0, true);
			for (int i = 0; i < confNames.length; i++)
			{
				if (chooseConf.getItemAtPosition(i).equals(checked))
				{
					chooseConf.setItemChecked(i, true);
				}
			}
		} else
		{
			ArrayAdapter<String> confNamesAd = new ArrayAdapter<String>(SettingsTabs.activity, android.R.layout.simple_list_item_1, new String[] { "You haven't saved any zones!" });
			chooseConf.setAdapter(confNamesAd);
			noZones = true;
			loadButt.setEnabled(false);
			renameButt.setEnabled(false);
			deleteButt.setEnabled(false);
		}
	}

	public void goToSaveSettings(View v)
	{
		if (createSaveConfAlertDialogue() != null)
		{
			saveConfAlert = createSaveConfAlertDialogue();
		}
		saveConfAlert.show();
	}

	public void goToLoadSettings(View v)
	{
		loadConf();
	}

	public void goToDelSettings(View v)
	{
		delConf();
	}

	//	public void overwriteConf(View v)
	//	{
	//		if (!noZones)
	//		{
	//			createOverwriteAlertDialogue().show();
	//		}
	//	}
	public void renameConf(View v)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		int thisN = -1;
		String name = " ";
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf == null)
			{
				chooseConf = (ListView) SettingsTabs.activity.findViewById(R.id.chooseConf);
			}
			if (chooseConf.getItemAtPosition(chooseConf.getCheckedItemPosition()).equals(sp.getString(i + "name", " ")))
			{
				thisN = i;
				name = sp.getString(i + "name", " ");
			}
		}
		if (name != " ")
		{
			createRenameConfAlertDialogue(sp, thisN, name).show();
		}
	}

	private void saveConf(String name)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		int n = sp.getInt("numOfConfs", 0);
		overwrite = false;
		boolean foundDuplicate = false;
		for (int i = 0; i < n; i++)
		{
			if (sp.getString(i + "name", " ").equals(name))
			{
				foundDuplicate = true;
				if (emptySpaceT != null)
				{
					emptySpaceT.cancel();
				}
				if (blankT != null)
				{
					blankT.cancel();
				}
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
		boolean foundAvailableIndex = false;
		SharedPreferences sps = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);

		for (int i = 0; i < name.length(); i++)
		{
			if (name.charAt(i) != ' ' && name.charAt(i) != '	')
			{
				emptySpace = false;
			}
		}
		if (!emptySpace || overwrite)
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
			edit.putInt(n + "platformsSize", MyView.shapes.size());
			for (int i = 0; i < MyView.shapes.size(); i++)
			{
				if (MyView.shapes.get(i) instanceof Platform)
				{
					Platform platform = (Platform) MyView.shapes.get(i);
					edit.putFloat(n + "platformStartX" + i, platform.getStartX());
					edit.putFloat(n + "platformStartY" + i, platform.getStartY());
					edit.putFloat(n + "platformEndX" + i, platform.getEndX());
					edit.putFloat(n + "platformEndY" + i, platform.getEndY());
					edit.putFloat(n + "ovalX" + i, -1000);
					edit.putFloat(n + "ovalY" + i, -1000);
					edit.putFloat(n + "ovalXDiameter" + i, -1000);
					edit.putFloat(n + "ovalYDiameter" + i, -1000);
					edit.putFloat(n + "rectX" + i, -1000);
					edit.putFloat(n + "rectY" + i, -1000);
					edit.putFloat(n + "rectWidth" + i, -1000);
					edit.putFloat(n + "rectHeight" + i, -1000);
				} else if (MyView.shapes.get(i) instanceof HollowOval)
				{
					HollowOval oval = (HollowOval) MyView.shapes.get(i);
					edit.putFloat(n + "ovalX" + i, oval.getX());
					edit.putFloat(n + "ovalY" + i, oval.getY());
					edit.putFloat(n + "ovalXDiameter" + i, oval.getXDiameter());
					edit.putFloat(n + "ovalYDiameter" + i, oval.getYDiameter());
					edit.putFloat(n + "platformStartX" + i, -1000);
					edit.putFloat(n + "platformStartY" + i, -1000);
					edit.putFloat(n + "platformEndX" + i, -1000);
					edit.putFloat(n + "platformEndY" + i, -1000);
					edit.putFloat(n + "rectX" + i, -1000);
					edit.putFloat(n + "rectY" + i, -1000);
					edit.putFloat(n + "rectWidth" + i, -1000);
					edit.putFloat(n + "rectHeight" + i, -1000);
				} else if (MyView.shapes.get(i) instanceof HollowRectangle)
				{
					HollowRectangle rect = (HollowRectangle) MyView.shapes.get(i);
					edit.putFloat(n + "rectX" + i, rect.getX());
					edit.putFloat(n + "rectY" + i, rect.getY());
					edit.putFloat(n + "rectWidth" + i, rect.getWidth());
					edit.putFloat(n + "rectHeight" + i, rect.getHeight());
					edit.putFloat(n + "platformStartX" + i, -1000);
					edit.putFloat(n + "platformStartY" + i, -1000);
					edit.putFloat(n + "platformEndX" + i, -1000);
					edit.putFloat(n + "platformEndY" + i, -1000);
					edit.putFloat(n + "ovalX" + i, -1000);
					edit.putFloat(n + "ovalY" + i, -1000);
					edit.putFloat(n + "ovalXDiameter" + i, -1000);
					edit.putFloat(n + "ovalYDiameter" + i, -1000);
				}
			}
			if (!foundAvailableIndex)
			{
				edit.putInt("numOfConfs", n + 1);
			}
			edit.commit();
			IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
			if (saveConfAlert != null)
			{
				saveConfAlert.dismiss();
				saveConfAlert = null;
			}
			refreshZoneList();
		} else if (emptySpace)
		{
			if (name.length() == 0)
			{
				if (emptySpaceT != null)
				{
					emptySpaceT.cancel();
				}
				if (blankT != null)
				{
					blankT.cancel();
				}
				emptySpaceT = Toast.makeText(SettingsTabs.activity, emptySpaceS, Toast.LENGTH_SHORT);
				emptySpaceT.show();
			} else
			{
				if (emptySpaceT != null)
				{
					emptySpaceT.cancel();
				}
				if (blankT != null)
				{
					blankT.cancel();
				}
				blankT = Toast.makeText(SettingsTabs.activity, blankS, Toast.LENGTH_SHORT);
				blankT.show();
			}
		}
	}

	private void renameConf(SharedPreferences sp, int thisN, String name, AlertDialog alert)
	{
		Editor edit = sp.edit();
		edit.putString(thisN + "name", name);
		edit.commit();
		alert.dismiss();
		IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
		refreshZoneList();
	}

	private AlertDialog createAlreadyExistsAlertDialogue(final SharedPreferences sp, final String name, final int n)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingsTabs.activity);
		builder.setMessage(Html.fromHtml("A zone named <b>" + name + "</b> already exists.\nDo you want to overwrite it?")).setPositiveButton("Yes", new DialogInterface.OnClickListener()
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

	private AlertDialog createRenameConfAlertDialogue(final SharedPreferences sp, final int thisN, final String name)
	{
		AlertDialog.Builder b = new AlertDialog.Builder(SettingsTabs.activity);
		b.setMessage(Html.fromHtml("Rename <b>" + name + "</b> to...")).setPositiveButton("Rename!", new DialogInterface.OnClickListener()
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
		LinearLayout ll = new LinearLayout(SettingsTabs.activity);
		ll.setOrientation(LinearLayout.VERTICAL);
		final TextView numChars = new TextView(SettingsTabs.activity);
		numChars.setText("0/30 characters");
		final EditText et = new EditText(SettingsTabs.activity);
		et.setHint("Give your zone a new name!");
		et.setInputType(InputType.TYPE_CLASS_TEXT);
		et.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (s.length() <= 30)
				{
					renameConfAlert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					numChars.setTextColor(Color.BLACK);
				} else
				{
					renameConfAlert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					numChars.setTextColor(Color.RED);
				}
				numChars.setText(s.length() + "/30 characters");

				for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
				{
					if (sp.getString(i + "name", " ").equals(et.getText().toString()) && i != thisN)
					{
						renameConfAlert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						numChars.setTextColor(Color.RED);
						numChars.setText("Already exists");

					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
		ll.addView(et);
		ll.addView(numChars);
		b.setView(ll);
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
						String newName = et.getText().toString();
						boolean emptySpace = true;
						for (int i = 0; i < newName.length(); i++)
						{
							if (newName.charAt(i) != ' ' && newName.charAt(i) != '	')
							{
								emptySpace = false;
							}
						}
						if (emptySpace)
						{
							if (newName.length() == 0)
							{
								if (emptySpaceT != null)
								{
									emptySpaceT.cancel();
								}
								if (blankT != null)
								{
									blankT.cancel();
								}
								emptySpaceT = Toast.makeText(SettingsTabs.activity, emptySpaceS, Toast.LENGTH_SHORT);
								emptySpaceT.show();
							} else
							{
								if (emptySpaceT != null)
								{
									emptySpaceT.cancel();
								}
								if (blankT != null)
								{
									blankT.cancel();
								}
								blankT = Toast.makeText(SettingsTabs.activity, blankS, Toast.LENGTH_SHORT);
								blankT.show();
							}
						} else
						{
							renameConf(sp, thisN, newName, alert);
						}
					}

				});
			}
		});
		renameConfAlert = alert;
		return alert;
	}

	private AlertDialog createSaveConfAlertDialogue()
	{
		if (saveConfAlert != null)
		{
			return null;
		}
		AlertDialog.Builder b = new AlertDialog.Builder(SettingsTabs.activity);
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
		LinearLayout ll = new LinearLayout(SettingsTabs.activity);
		ll.setOrientation(LinearLayout.VERTICAL);
		final TextView numChars = new TextView(SettingsTabs.activity);
		numChars.setText("0/30 characters");
		editName = new EditText(SettingsTabs.activity);
		editName.setHint("Give your zone a name!");
		editName.setInputType(InputType.TYPE_CLASS_TEXT);
		editName.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (s.length() <= 30)
				{
					if (saveConfAlert != null)
					{
						saveConfAlert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					}
					numChars.setTextColor(Color.BLACK);
				} else
				{
					if (saveConfAlert != null)
					{
						saveConfAlert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					}
					numChars.setTextColor(Color.RED);
				}
				numChars.setText(s.length() + "/30 characters");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
		ll.addView(editName);
		ll.addView(numChars);
		b.setView(ll);
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
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		int n = -1;
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf == null)
			{
				chooseConf = (ListView) SettingsTabs.activity.findViewById(R.id.chooseConf);
			}
			if (chooseConf.getItemAtPosition(chooseConf.getCheckedItemPosition()).equals(sp.getString(i + "name", " ")))
			{
				n = i;
			}
		}
		if (!sp.getString(n + "name", " ").equals(" "))
		{
			IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
			MyView.ball.setPosition(new Vec2(sp.getFloat(n + "startBallX", 0), sp.getFloat(n + "startBallY", 0)));
			MyView.ball.setVelocity(new Vec2(sp.getFloat(n + "startBallXSpeed", 0), sp.getFloat(n + "startBallYSpeed", 0)));
			MyView.startBallX = sp.getFloat(n + "startBallX", 0);
			MyView.startBallY = sp.getFloat(n + "startBallY", 0);
			MyView.startBallXSpeed = sp.getFloat(n + "startBallXSpeed", 0);
			MyView.startBallYSpeed = sp.getFloat(n + "startBallYSpeed", 0);
			SavePrefs("gravityValue", sp.getInt(n + "gravityValue", 100));
			SavePrefs("bounceLevelValue", sp.getInt(n + "bounceLevelValue", 100));
			SavePrefs("frictionValue", sp.getInt(n + "frictionValue", 100));

			MyView.clearShapes();
			for (int i = 0; i < sp.getInt(n + "platformsSize", 0); i++)
			{
				if (sp.getFloat(n + "platformStartX" + i, -1000) != -1000)
				{
					MyView.shapes.add(new Platform(BodyType.STATIC, sp.getFloat(n + "platformStartX" + i, 0), sp.getFloat(n + "platformStartY" + i, 0), sp.getFloat(n + "platformEndX" + i, 0), sp.getFloat(n + "platformEndY" + i, 0), 0, 1, 0));
				} else if (sp.getFloat(n + "ovalX" + i, -1000) != -1000)
				{
					MyView.shapes.add(new HollowOval(BodyType.STATIC, sp.getFloat(n + "ovalX" + i, 0), sp.getFloat(n + "ovalY" + i, 0), sp.getFloat(n + "ovalXDiameter" + i, 0), sp.getFloat(n + "ovalYDiameter" + i, 0), 0, 1, 0));
				} else if (sp.getFloat(n + "rectX" + i, -1000) != -1000)
				{
					MyView.shapes.add(new HollowRectangle(BodyType.STATIC, sp.getFloat(n + "rectX" + i, 0), sp.getFloat(n + "rectY" + i, 0), sp.getFloat(n + "rectWidth" + i, 0), sp.getFloat(n + "rectHeight" + i, 0), 0, 1, 0));
				}
			}
			Intent intent = new Intent(SettingsTabs.activity, MainActivity.class);
			intent.putExtra("fromLoad", true);
			SettingsTabs.activity.startActivity(intent);
			SettingsTabs.activity.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_right);
		}
	}

	private void SavePrefs(String key, float value)
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	private AlertDialog createOverwriteAlertDialogue()
	{
		final SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		final int n = sp.getInt("numOfConfs", 0);
		if (chooseConf == null)
		{
			chooseConf = (ListView) SettingsTabs.activity.findViewById(R.id.chooseConf);
		}
		final String name = (String) chooseConf.getItemAtPosition(chooseConf.getCheckedItemPosition());
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingsTabs.activity);
		builder.setMessage(Html.fromHtml("Overwrite <b>" + name + "</b> with the current zone?")).setPositiveButton("Yes", new DialogInterface.OnClickListener()
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
			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}

	public void delConf()
	{
		SharedPreferences sp = SettingsTabs.activity.getSharedPreferences(MainSettingsFragment.dataSP, 0);
		int thisN = -1;
		String name = " ";
		for (int i = 0; i < sp.getInt("numOfConfs", 0); i++)
		{
			if (chooseConf == null)
			{
				chooseConf = (ListView) SettingsTabs.activity.findViewById(R.id.chooseConf);
			}
			if (chooseConf.getItemAtPosition(chooseConf.getCheckedItemPosition()).equals(sp.getString(i + "name", " ")))
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
			IntroActivity.spoolButton.play(IntroActivity.button, buttonVolume, buttonVolume, 0, 0, 1);
			refreshZoneList();
		}
	}

	private AlertDialog createDeleteAlertDialogue(final String name, final SharedPreferences sp, final int thisN)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingsTabs.activity);
		builder.setMessage(Html.fromHtml("Permanently delete <b>" + name + "</b>?")).setPositiveButton("Yes", new DialogInterface.OnClickListener()
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
