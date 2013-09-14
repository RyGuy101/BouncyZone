package com.blogspot.mathjoy.bouncy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View
{

	Paint paint = new Paint();
	float radius = 25;
	float x = 0;
	float y = 0;
	float acceleration = 0;//pixels per 1/50 sec per 1/50 sec
	float xSpeed = 0;//pixels per 1/50 sec
	float ySpeed = 0;//pixels per 1/50 sec
	boolean start = true;

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
		if (start)
		{
			x = (float) (c.getWidth()/2.0);
			y = radius;
			acceleration = (float) (c.getWidth()/100.0);
		}
		ySpeed += acceleration;
		x += xSpeed;
		y += ySpeed;
		c.drawCircle(x, y, radius, paint);
		invalidate();
	}

}
