package com.blogspot.mathjoy.bouncy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View
{

	Paint paint = new Paint();
	static float radius;
	static float x;
	static float y;
	static float acceleration;
	static float xSpeed;
	static float ySpeed;
	static boolean notStart;
	public static int color;
	// these might be used later
	float angle = 90;// Math.atan(xSpeed/ySpeed)
	float speed = 0;// xSpeed * Math.cos(angle) + ySpeed * Math.sin(angle)

	public MyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setColor() {
		// colorString = thatColor;
	}

	@Override
	protected void onDraw(Canvas c) {
		// TODO Auto-generated method stub
		try
		{
			Thread.sleep(20);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDraw(c);
		radius = (float) (c.getWidth() / 50.0);
		if (notStart == false)
		{
			x = (float) (c.getWidth() / 2.0);
			y = radius;
			acceleration = (float) (c.getWidth() / 500.0);
			notStart = true;
		}
		if (y >= this.getHeight() - radius)
		{
			ySpeed *= -1;
		} else
		{
			ySpeed += acceleration;
		}
		x += xSpeed;
		y += ySpeed;
		c.drawColor(Color.BLACK);
		getTag();
		paint.setColor(color);
		c.drawCircle(x, y, radius, paint);
		// c.drawLine(0, this.getHeight() - 1, 100, this.getHeight() - 1,
		// paint);
		invalidate();
	}

}
