package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class IntroView extends MyView
{
	public IntroView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		ballColor = Color.GREEN;
		sp = IntroActivity.spoolBounce;
	}

	@Override
	protected void onDraw(Canvas c)
	{
		super.onDraw(c);

	}

	protected void setup()
	{
		bounce = IntroActivity.bounce;
		super.setup();
		startBallY = toMeters(100);
		ball.setPosition(new Vec2(ball.getX(), toMeters(100)));
		ball.setFriction(0f);
		ball.setVelocity(new Vec2(5, 0));
		float w = this.getWidth();
		float h = this.getHeight();
		makePlatform(w * 0.02f, w * 0.02f, w - w * 0.02f, w * 0.02f);
		makePlatform(w * 0.02f, w * 0.02f, w * 0.02f, h - w * 0.02f);
		makePlatform(w * 0.02f, h - w * 0.02f, w - w * 0.02f, h - w * 0.02f);
		makePlatform(w - w * 0.02f, w * 0.02f, w - w * 0.02f, h - w * 0.02f);

		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getTop());
		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getBottom(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom());
		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getLeft(), IntroActivity.buttons.getBottom());
		makePlatform(IntroActivity.buttons.getRight(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom());

//		makePlatform(IntroActivity.buttons.getLeft() + w * 0.1f, w * 0.2f, IntroActivity.buttons.getRight() - w * 0.1f, IntroActivity.buttons.getTop() - w * 0.2f);
//		makePlatform(IntroActivity.buttons.getLeft() + w * 0.2f, h - w * 0.2f, IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom() + w * 0.2f);
//		makePlatform(IntroActivity.buttons.getRight() + w * 0.1f, IntroActivity.buttons.getTop() + IntroActivity.buttons.getHeight() / 2.0f, w - w * 0.2f, IntroActivity.buttons.getBottom());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return false;
	}

	private void makePlatform(float startX, float startY, float endX, float endY)
	{
		platforms.add(new Platform(BodyType.STATIC, toMeters(startX), toMeters(startY), toMeters(endX), toMeters(endY), 0, 1, 0));
	}
}
