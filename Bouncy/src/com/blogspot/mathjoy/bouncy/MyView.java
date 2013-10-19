package com.blogspot.mathjoy.bouncy;

import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import org.apache.http.client.CircularRedirectException;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyView extends View implements OnTouchListener
{
	Paint paint = new Paint();
	static float radius;
	static float x;
	static float y;
	static float acceleration;
	static float xSpeed;
	static float ySpeed;
	double dx;
	double dy;
	double dr;
	double D;
	static boolean alreadyStarted;
	public static int color;
	public static double gravity;
	public static boolean touching = false;
	public static float touchX;
	public static float touchY;
	public static final int MODE_BALL = 0;
	public static final int MODE_CREATE_PLATFORM = 1;
	public static final int MODE_MOVE_PLATFORM = 2;
	public static final int MODE_DELETE_PLATFORM = 3;
	public static int mode = 0;
	ArrayList<Platform> platforms = new ArrayList<Platform>();
	// these might be used later
	float angle = 90;// Math.atan(xSpeed/ySpeed)
	float speed = 0;// xSpeed * Math.cos(angle) + ySpeed * Math.sin(angle)

	public MyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		Platform testLine = new Platform(10, 10, 100, 100);
		platforms.add(testLine);
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Platform testLine = new Platform(10, 10, 100, 100);
		platforms.add(testLine);
	}

	public MyView(Context context)
	{
		super(context);
		Platform testLine = new Platform(10, 10, 100, 100);
		platforms.add(testLine);
	}

	public void setColor()
	{
		// colorString = thatColor;
	}

	@Override
	protected void onDraw(Canvas c)
	{
		try
		{
			Thread.sleep(20);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		super.onDraw(c);
		c.drawColor(Color.BLACK);
		radius = (float) (this.getHeight() / 75.0);
		acceleration = (float) ((this.getHeight() / 1000.0) * gravity);
		if (alreadyStarted == false)
		{
			x = (float) (this.getWidth() / 2.0);
			y = radius;
			acceleration = (float) ((this.getHeight() / 1000.0) * gravity);
			alreadyStarted = true;
		}
		if (mode == MODE_BALL && touching == true)
		{
			x = touchX;
			y = touchY;
			c.drawCircle(x, y, radius, paint);
		} else
		{
			// if (y >= this.getHeight() - radius)
			// {
			// ySpeed *= -1;
			// } else
			// {
			ySpeed += acceleration;
			// }
			// if ()
			{
			}
			//getTag();
		}
		x += xSpeed;
		y += ySpeed;
		paint.setColor(color);
		c.drawCircle(x, y, radius, paint);
		// int test = platforms.size();
		for (int i = 0; i < platforms.size(); i++)
		{
			paint.setColor(Color.WHITE);
			c.drawLine(platforms.get(i).getStartX(), platforms.get(i).getStartY(), platforms.get(i).getEndX(), platforms.get(i).getEndY(), paint);
		}
		// c.drawLine(0, this.getHeight() - 1, 100, this.getHeight() - 1,
		// paint);
		invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		// Junk
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		touchX = event.getX();
		touchY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touching = true;
			xSpeed = 0;// make finger speed
			ySpeed = 0;
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			touching = false;
		}
		return true;
	}
}
