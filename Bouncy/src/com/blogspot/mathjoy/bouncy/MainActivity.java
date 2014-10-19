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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseGameActivity implements OnTouchListener, ContactListener
{
	Intent intent;
	String pickedColor;
	int[] possibleColors = { Color.RED, Color.rgb(255, 127, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(160, 32, 240), Color.rgb(255, 105, 180), Color.rgb(127, 63, 15), Color.WHITE, Color.GRAY };
	String[] possibleColorNames = { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "White", "Gray" };
	static boolean justOpened = true;
	public static ImageButton ball;
	public static ImageButton platform;
	public static ImageButton settings;
	public static LinearLayout undoLayout;
	public static ImageButton undo;
	public static TextView redoText;
	public ImageButton buttonDown;
	public static final String GAME_SP = "game";
	boolean updatedUndoButton = false;
	boolean undoLongClicked = false;
	public static float bounceVolume = (float) 0.6;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_main);
		ball = (ImageButton) findViewById(R.id.Ball);
		platform = (ImageButton) findViewById(R.id.Platform);
		settings = (ImageButton) findViewById(R.id.Settings);
		undoLayout = (LinearLayout) findViewById(R.id.undoLayout);
		undo = (ImageButton) findViewById(R.id.Undo);
		redoText = (TextView) findViewById(R.id.redoText);
		if (MyView.oldPlatforms.size() == 0)
		{
			redoText.setTextColor(Color.GRAY);
		}
		settings.setBackgroundColor(Color.LTGRAY);
		undo.setBackgroundColor(Color.LTGRAY);
		final ViewTreeObserver observer = redoText.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				if (!updatedUndoButton)
				{
					undo.getLayoutParams().height = undo.getHeight() - redoText.getHeight();
					updatedUndoButton = true;
				}
			}
		});
		if (MyView.mode == MyView.MODE_BALL)
		{
			MainActivity.ball.setBackgroundColor(Color.GRAY);
			MainActivity.platform.setBackgroundColor(Color.LTGRAY);
		} else if (MyView.mode == MyView.MODE_CREATE_PLATFORM)
		{
			MainActivity.platform.setBackgroundColor(Color.GRAY);
			MainActivity.ball.setBackgroundColor(Color.LTGRAY);
		}
		ball.setOnTouchListener(this);
		platform.setOnTouchListener(this);
		settings.setOnTouchListener(this);
		undoLayout.setOnTouchListener(this);
		undo.setOnTouchListener(this);
		undoLayout.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				if (MyView.oldPlatforms.size() > 0)
				{
					undo.setImageResource(R.drawable.redo);
					undo.setBackgroundColor(Color.DKGRAY);
					redoText.setBackgroundColor(Color.DKGRAY);
					undoLongClicked = true;
				}
				return false;
			}
		});
		undo.setOnLongClickListener(new View.OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				if (MyView.oldPlatforms.size() > 0)
				{
					undo.setImageResource(R.drawable.redo);
					undo.setBackgroundColor(Color.DKGRAY);
					redoText.setBackgroundColor(Color.DKGRAY);
					undoLongClicked = true;
				}
				return false;
			}
		});
		//		ball.setOnClickListener(this);
		//		platform.setOnClickListener(this);
		//		settings.setOnClickListener(this);
		//		undo.setOnClickListener(this);
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
		SharedPreferences sp = getSharedPreferences("settings", 0);
		pickedColor = sp.getString("selectedColor", "Red");
		for (int i = 0; i < possibleColors.length; i++)
		{
			if (pickedColor.equals(possibleColorNames[i]))
			{
				MyView.ballColor = possibleColors[i];
			}
		}
		int frictionPower = 7;
		MyView.ballRestitution = (float) (sp.getFloat("bounceLevelValue", 100.0f) / 100.0);
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
			MyView.ball.setRestitution((float) (sp.getFloat("bounceLevelValue", 100.0f) / 100.0));
			MyView.ball.setFriction((float) (Math.pow(sp.getFloat("frictionValue", 100.0f), frictionPower) / Math.pow(100, frictionPower)));
			if (MyView.ballFriction == 0)
			{
				MyView.ball.setAngularVelocity(0);
			}
		}
		WorldManager.setGravity(new Vec2(0, (float) (sp.getFloat("gravityValue", 100.0f) / 10.0)));
		WorldManager.world.setContactListener(this);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	public void goToMenu(View v)
	{
		IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
		//		MyView.makeBounce = false;
		//		MyView.ball.setPosition(new Vec2(MyView.ballX, MyView.ballY));
		Intent intent = new Intent(this, SettingsTabs.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_left);
	}

	public void modeBall(View view)
	{
		if (MyView.mode != MyView.MODE_BALL)
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			ball.setBackgroundColor(Color.GRAY);
			platform.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_BALL;
		}
	}

	public void modePlatform(View view)
	{
		if (MyView.mode != MyView.MODE_CREATE_PLATFORM)
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			platform.setBackgroundColor(Color.GRAY);
			ball.setBackgroundColor(Color.LTGRAY);
			MyView.mode = MyView.MODE_CREATE_PLATFORM;
		}
	}

	// public void modeGrab(View view)
	// {
	// MyView.timeBetweenFrames = 20;
	// }
	//
	// public void modeDelete(View view)
	// {
	// MyView.timeBetweenFrames = 1000;
	// }
	public void undo(View view)
	{
		if (!undoLongClicked)
		{
			if (MyView.platforms != null)
			{
				if (MyView.platforms.size() > 0)
				{
					IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
					MyView.destroyLastPlatform();
					redoText.setTextColor(Color.BLACK);
				}
			}
		} else
		{
			IntroActivity.spoolButton.play(IntroActivity.button, IntroActivity.buttonVolume, IntroActivity.buttonVolume, 0, 0, 1);
			MyView.reCreatePlatform();
			if (MyView.oldPlatforms.size() == 0)
			{
				redoText.setTextColor(Color.GRAY);
			}
			undoLongClicked = false;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (!((v.equals(ball) && MyView.mode == MyView.MODE_BALL) || (v.equals(platform) && MyView.mode == MyView.MODE_CREATE_PLATFORM)))
		{
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				if (v.equals(undoLayout) || v.equals(undo))
				{
					undo.setImageResource(R.drawable.undo);
					undo.setBackgroundColor(Color.LTGRAY);
					redoText.setBackgroundColor(Color.LTGRAY);
				} else
				{
					v.setBackgroundColor(Color.LTGRAY);
				}
			} else if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				if (v.equals(undoLayout) || v.equals(undo))
				{
					undo.setBackgroundColor(Color.rgb(170, 170, 170));
					redoText.setBackgroundColor(Color.rgb(170, 170, 170));
					undoLongClicked = false;
				} else
				{
					v.setBackgroundColor(Color.rgb(170, 170, 170));
				}
			}
		}
		return false;
	}

	//	@Override
	//	public void onClick(View v)
	//	{
	//		if (v.equals(ball))
	//		{
	//			modeBall(v);
	//		} else if (v.equals(platform))
	//		{
	//			modePlatform(v);
	//		} else if (v.equals(settings))
	//		{
	//			goToMenu(v);
	//		} else if (v.equals(undo))
	//		{
	//			undo(v);
	//		}
	//	}

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
		setStepsOfAchivement(R.string.achievement_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_super_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_mega_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_hyper_bouncy, numBounces);
		setStepsOfAchivement(R.string.achievement_bouncy_king, (int) (numBounces / 10.0));
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
	public void beginContact(Contact arg0)
	{
		if (MyView.makeBounce)
		{
			MyView.makeBounce = false;
			MyView.touching = false;
			WorldManager.undoTemporaryGravitySet();
			if (MyView.makeBounceOnstart)
			{
				IntroActivity.spoolBounce.play(IntroActivity.bounce, bounceVolume, bounceVolume, 0, 0, 1);
				if (arg0.getFixtureA().getBody().equals(MyView.ball.getBody()) || arg0.getFixtureB().getBody().equals(MyView.ball.getBody()))
				{
					new MyTask().execute();
				}
			} else
			{
				MyView.makeBounceOnstart = true;
			}
		}
	}

	@Override
	public void endContact(Contact arg0)
	{
		MyView.makeBounce = true;
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1)
	{
		// TODO Auto-generated method stub

	}
}
