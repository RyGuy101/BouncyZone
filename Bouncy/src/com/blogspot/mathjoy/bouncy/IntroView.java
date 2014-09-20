package com.blogspot.mathjoy.bouncy;

import org.jbox2d.dynamics.BodyType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

public class IntroView extends MyView
{
	public IntroView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		ballColor = Color.GREEN;
	}

	@Override
	protected void onDraw(Canvas c)
	{
		super.onDraw(c);

	}

	protected void setup()
	{
		super.setup();
		platforms.add(new Platform(BodyType.STATIC, toMeters(100), toMeters(100), toMeters(400), toMeters(400), 0, 1, 0));
	}
}
