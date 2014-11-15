package com.blogspot.mathjoy.bouncy;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseGameActivity implements OnTouchListener, ContactListener, SensorEventListener, OnLongClickListener
{
	Intent intent;
	String pickedColor;
	int[] possibleColors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };
	static boolean justOpened = true;
	public static ImageButton ball;
	public static ImageButton platform;
	public static ImageButton settings;
	public static ImageButton undo;
	public static TextView redoText;
	public ImageButton buttonDown;
	public static final String GAME_SP = "game";
	boolean updatedUndoButton = false;
	boolean longClicked = false;
	public static float bounceVolume = (float) 0.6;
	public static MainActivity activity;

	SensorManager sm;
	Sensor s;
	boolean thereIsSensor = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = this;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_main);
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
		{
			s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			thereIsSensor = true;
		}
		ball = (ImageButton) findViewById(R.id.Ball);
		platform = (ImageButton) findViewById(R.id.Platform);
		settings = (ImageButton) findViewById(R.id.Settings);
		undo = (ImageButton) findViewById(R.id.Undo);
		redoText = (TextView) findViewById(R.id.redoText);
		settings.setBackgroundColor(Color.LTGRAY);
		undo.setBackgroundColor(Color.LTGRAY);
		ball.setOnTouchListener(this);
		ball.setOnLongClickListener(this);
		platform.setOnTouchListener(this);
		platform.setOnLongClickListener(this);
		settings.setOnTouchListener(this);
		undo.setOnTouchListener(this);
		undo.setOnLongClickListener(this);
		try
		{
			if (getIntent().getExtras().getBoolean("fromLoad") == true)
			{
				ball.setBackgroundColor(Color.GRAY);
				platform.setBackgroundColor(Color.LTGRAY);
				MyView.mode = MyView.MODE_BALL;
			}
		} catch (Exception e)
		{
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (thereIsSensor)
		{
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
		}
		SharedPreferences sp = getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		MyView.accelerometer = sp.getBoolean("useAccelerometer", false);
		pickedColor = sp.getString("selectedColor", "Red");
		for (int i = 0; i < possibleColors.length; i++)
		{
			if (pickedColor.equals(possibleColorNames[i]))
			{
				MyView.ballColor = possibleColors[i];
			}
		}
		int frictionPower = 7;
		double restitutionPower = 0.5;
		MyView.ballRestitution = (float) (Math.pow(sp.getFloat("bounceLevelValue", 100.0f), restitutionPower) / Math.pow(100, restitutionPower));
		MyView.ballFriction = (float) (Math.pow(sp.getFloat("frictionValue", 100.0f), frictionPower) / Math.pow(100, frictionPower));
		if (MyView.ballFriction == 0)
		{
			MyView.showLine = false;
		} else
		{
			MyView.showLine = true;
		}
		if (MyView.ball != null)
		{
			MyView.ball.setRestitution((float) (Math.pow(sp.getFloat("bounceLevelValue", 100.0f), restitutionPower) / Math.pow(100, restitutionPower)));
			MyView.ball.setFriction((float) (Math.pow(sp.getFloat("frictionValue", 100.0f), frictionPower) / Math.pow(100, frictionPower)));
			if (MyView.ballFriction == 0)
			{
				MyView.ball.setAngularVelocity(0);
			}
		}
		WorldManager.setGravity(new Vec2(0, (float) (sp.getFloat("gravityValue", 100.0f) / 10.0)));
		WorldManager.world.setContactListener(this);
		MyView.intro = false;
		if (MyView.mode == MyView.MODE_BALL)
		{
			MainActivity.ball.setBackgroundColor(Color.GRAY);
			MainActivity.platform.setBackgroundColor(Color.LTGRAY);
		} else if (MyView.mode == MyView.MODE_PLATFORM)
		{
			MainActivity.platform.setBackgroundColor(Color.GRAY);
			MainActivity.ball.setBackgroundColor(Color.LTGRAY);
		}
		if (MyView.oldPlatforms.size() == 0)
		{
			redoText.setTextColor(Color.TRANSPARENT);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (thereIsSensor)
		{
			sm.unregisterListener(this);
		}
	}

	public void goToMenu(View v)
	{
		IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
		Intent intent = new Intent(this, SettingsTabs.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_left);
	}

	public void modeBall(View view)
	{
		if (longClicked)
		{
			longClicked = false;
			PopupMenu popup = new PopupMenu(MainActivity.this, view);
			popup.getMenuInflater().inflate(R.menu.ball_popup, popup.getMenu());
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
			{
				public boolean onMenuItemClick(MenuItem item)
				{
					MyView.resetBallPosition();
					return true;
				}
			});
			popup.show();
		} else if (MyView.mode != MyView.MODE_BALL)
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			ball.setBackgroundColor(Color.GRAY);
			platform.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_BALL;
		}
	}

	public void modePlatform(View view)
	{
		if (longClicked)
		{
			longClicked = false;
			PopupMenu popup = new PopupMenu(MainActivity.this, view);
			popup.getMenuInflater().inflate(R.menu.shapes_popup, popup.getMenu());
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
			{
				public boolean onMenuItemClick(MenuItem item)
				{
					return true;
				}
			});
			popup.show();
		} else if (MyView.mode != MyView.MODE_PLATFORM)
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			platform.setBackgroundColor(Color.GRAY);
			ball.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_PLATFORM;
		}
	}

	public void undo(View view)
	{
		if (!longClicked)
		{
			if (MyView.shapes != null)
			{
				if (MyView.shapes.size() > 0)
				{
					IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
					MyView.destroyLastPlatform();
					redoText.setTextColor(Color.argb(128, 0, 0, 0));
				}
			}
		} else
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			MyView.reCreatePlatform();
			if (MyView.oldPlatforms.size() == 0)
			{
				redoText.setTextColor(Color.TRANSPARENT);
			}
			longClicked = false;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (v.equals(undo))
			{
				undo.setImageResource(R.drawable.undo);
			}
			if (modeMatchesButton(v))
			{
				v.setBackgroundColor(Color.GRAY);
			} else
			{
				v.setBackgroundColor(Color.LTGRAY);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			longClicked = false;
			if (!modeMatchesButton(v))
			{
				v.setBackgroundColor(Color.rgb(170, 170, 170));
			}
		}
		return false;
	}

	public boolean modeMatchesButton(View v)
	{
		return (v.equals(ball) && MyView.mode == MyView.MODE_BALL) || (v.equals(platform) && MyView.mode == MyView.MODE_PLATFORM);
	}

	@Override
	public void onSignInFailed()
	{
	}

	@Override
	public void onSignInSucceeded()
	{
	}

	private void setStepsOfAchivement(int id, int steps)
	{
		try
		{
			Games.Achievements.setSteps(getApiClient(), getString(id), steps);
		} catch (Exception e)
		{
		}
	}

	public void updateStepsOfBounceAchievements(int numBounces)
	{
		if (numBounces == 100)
		{
			setStepsOfAchivement(R.string.achievement_bouncy, numBounces);
		} else if (numBounces == 500)
		{
			setStepsOfAchivement(R.string.achievement_super_bouncy, numBounces);

		} else if (numBounces == 1000)
		{
			setStepsOfAchivement(R.string.achievement_mega_bouncy, numBounces);
		} else if (numBounces == 2000)
		{
			setStepsOfAchivement(R.string.achievement_hyper_bouncy, numBounces);
		} else if (numBounces == 100000)
		{
			setStepsOfAchivement(R.string.achievement_bouncy_king, (int) (numBounces / 10.0));
		}
	}

	class MyTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			SharedPreferences sp = getSharedPreferences(GAME_SP, 0);
			Editor edit = sp.edit();
			edit.putInt("numBounces", sp.getInt("numBounces", 0) + 1);
			edit.commit();
			updateStepsOfBounceAchievements(sp.getInt("numBounces", 0));
			return null;
		}
	}

	@Override
	public void beginContact(Contact contact)
	{
		MyView.beginContact = true;
		if (contact.getFixtureA().getBody().equals(MyView.ball.getBody()) || contact.getFixtureB().getBody().equals(MyView.ball.getBody()))
		{
			MyView.isIntroBall = false;
		} else
		{
			MyView.isIntroBall = true;
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		MyView.endContact = true;
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1)
	{
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1)
	{
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		SharedPreferences sp = getSharedPreferences(MainSettingsFragment.settingsSP, 0);
		if (sp.getBoolean("useAccelerometer", false))
		{
			double xForce = event.values[0];
			double yForce = event.values[1];
			double multiplier = 1.02 * Math.sqrt(2) * (float) (sp.getFloat("gravityValue", 100.0f) / 100.0);
			WorldManager.setGravity(new Vec2((float) (-xForce * multiplier), (float) (yForce * multiplier)));
		}
	}

	@Override
	public boolean onLongClick(View v)
	{
		if (v.getId() == R.id.Undo)
		{
			if (MyView.oldPlatforms.size() > 0)
			{
				undo.setImageResource(R.drawable.redo);
				undo.setBackgroundColor(Color.DKGRAY);
				longClicked = true;
			}
		} else if (v.getId() == R.id.Ball)
		{
			ball.setBackgroundColor(Color.DKGRAY);
			longClicked = true;
		} else if (v.getId() == R.id.Platform)
		{
			platform.setBackgroundColor(Color.DKGRAY);
			longClicked = true;
		}
		return false;
	}
}
