package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyView extends View implements ContactListener, OnTouchListener
{
	static SoundPool sp = MainActivity.spoolBounce;
	static int bounce = MainActivity.bounce;
	public static float bounceVolume = (float) 0.6;
	public static float ballX;
	public static float ballY;
	public static float ballXSpeed;
	public static float ballYSpeed;
	public static float startBallX;
	public static float startBallY;
	public static float startBallXSpeed;
	public static float startBallYSpeed;
	public static ArrayList<Platform> platforms = new ArrayList<Platform>();
	public static boolean alreadyStarted = false;
	public static int mode;
	public static final int MODE_BALL = 0;
	public static final int MODE_CREATE_PLATFORM = 1;
	public static Vec2 gravity = new Vec2(0, 10.0f);
	public static double restitution;
	public static int ballColor;
	static float currentTouchX;
	static float currentTouchY;
	float[] touchX = { -1000, -1000 };
	float[] touchY = { -1000, -1000 };
	static boolean touching = false;
	static boolean initialTouch = false;
	static boolean wasTouching = false;

	public static Circle ball;
	public static Rectangle floor;
	Paint ballPaint = new Paint();

	boolean makeBounce = true;

	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		setup();
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}

	public MyView(Context context)
	{
		super(context);
		setup();
	}

	private void setup()
	{
		WorldManager.setupWorld(new Vec2(0f, 10f));
		WorldManager.world.setContactListener(this);
		ball = new Circle(BodyType.DYNAMIC, 1f, 0f, 0.1f, 0.5f, 0.5f, 1f);
		floor = new Rectangle(BodyType.STATIC, 1f, 5f, 1f, 0.1f, 0f, 0f, 0f);
		ballPaint.setColor(Color.RED);
	}

	//	private static int screenW;
	//	private static int screenH;

	private static float PPM = 128.0f;

	public static float toMeters(float pixels)
	{
		return pixels / PPM;
	}

	public static float toPixels(float meters)
	{
		return meters * PPM;
	}

	//	public static float getPPM()
	//	{
	//		return PPM;
	//	}
	//
	//	public static float getMPP()
	//	{
	//		return 1.0f / PPM;
	//	}

	@Override
	protected void onDraw(Canvas c)
	{
		long startTime = System.currentTimeMillis();
		super.onDraw(c);
		drawBackground(c);
		if (mode == MODE_BALL)
		{
			if (touching)
			{
				ballX = currentTouchX;
				ballY = currentTouchY;
				startBallX = currentTouchX;
				startBallY = currentTouchY;
				touchX[1] = touchX[0];
				touchY[1] = touchY[0];
				touchX[0] = currentTouchX;
				touchY[0] = currentTouchY;
				startBallXSpeed = ballXSpeed;
				startBallYSpeed = ballYSpeed;
				if (initialTouch)
				{
					initialTouch = false;
					WorldManager.setGravity(new Vec2(0f, 0f));
					ball.setPosition(new Vec2(toMeters(touchX[0]), toMeters(touchY[0])));
					ball.setVelocity(new Vec2(0f, 0f));
				} else if (knowEnoughtouch())
				{
					ball.setVelocity(new Vec2(toMeters(touchX[0] - touchX[1]) * 60, toMeters(touchY[0] - touchY[1]) * 60));
				}
			} else if (wasTouching)
			{
				wasTouching = false;
				WorldManager.setGravity(gravity);
			}

			WorldManager.step();
		}

		c.drawCircle(toPixels(ball.getX()), toPixels(ball.getY()), toPixels(ball.getRadius()), ballPaint);
		c.drawRect(toPixels(floor.getX() - floor.getWidth() / 2.0f), toPixels(floor.getY() - floor.getHeight() / 2.0f), toPixels(floor.getX() + floor.getWidth() / 2.0f), toPixels(floor.getY() + floor.getHeight() / 2.0f), ballPaint);

		long timeTook = System.currentTimeMillis() - startTime;
		if (timeTook < 1000.0 / 60.0)
		{
			try
			{
				Thread.sleep((long) (1000.0 / 60.0 - timeTook));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		invalidate();
	}

	@Override
	public void beginContact(Contact arg0)
	{
		if (makeBounce)
		{
			makeBounce = false;
			touching = false;
			sp.play(bounce, bounceVolume, bounceVolume, 0, 0, 1);
			SharedPreferences sp = MainActivity.sp;
			Editor edit = sp.edit();
			edit.putInt("numBounces", sp.getInt("numBounces", 0) + 1);
			edit.commit();
		}

	}

	@Override
	public void endContact(Contact arg0)
	{
		makeBounce = true;
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

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			initialTouch = true;
			touching = true;
			currentTouchX = event.getX();
			currentTouchY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			touching = false;
			wasTouching = true;
			for (int i = 0; i < 2; i++)
			{
				touchX[i] = -1000;
				touchY[i] = -1000;
			}

		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			currentTouchX = event.getX();
			currentTouchY = event.getY();
		}
		return true;
	}

	private void drawBackground(Canvas c)
	{
		c.drawColor(Color.BLACK);
	}

	private boolean knowEnoughtouch()
	{
		boolean knowEnoughTouch = true;
		for (int i = 0; i < 2; i++)
		{
			if (touchX[i] == -1000)
			{
				knowEnoughTouch = false;
			}
			if (touchY[i] == -1000)
			{
				knowEnoughTouch = false;
			}
		}
		if (touchX[0] == touchX[1])
		{
			knowEnoughTouch = false;

		}
		if (touchY[0] == touchY[1])
		{
			knowEnoughTouch = false;

		}
		return knowEnoughTouch;
	}
}
