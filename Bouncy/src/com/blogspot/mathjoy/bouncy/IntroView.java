package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class IntroView extends MyView
{
	static float w;
	static float h;

	public IntroView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		ballColor = Color.RED;
		sp = IntroActivity.spoolBounce;
		intro = true;
	}

	protected void setup()
	{
		super.setup();
		super.makeBallUnreal();
		introBall = new Circle(BodyType.DYNAMIC, originalStartBallX, toMeters(h * 0.1f), 0.1f, 1, 0, 1);
		w = this.getWidth();
		h = this.getHeight();
		introBall.setPosition(new Vec2(originalStartBallX, toMeters(h * 0.1f)));
		introBall.setFriction(0f);
		introBall.setVelocity(new Vec2(toMeters(w * 0.5f), 0));

		makePlatform(1, 1, w - 1, 1);
		makePlatform(1, 1, 1, h - 1);
		makePlatform(1, h - 1, w - 1, h - 1);
		makePlatform(w - 1, 1, w - 1, h - 1);

		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getTop());
		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getBottom(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom());
		makePlatform(IntroActivity.buttons.getLeft(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getLeft(), IntroActivity.buttons.getBottom());
		makePlatform(IntroActivity.buttons.getRight(), IntroActivity.buttons.getTop(), IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom());

		//		makePlatform(IntroActivity.buttons.getLeft() + w * 0.1f, w * 0.2f, IntroActivity.buttons.getRight() - w * 0.1f, IntroActivity.buttons.getTop() - w * 0.2f);
		//		makePlatform(IntroActivity.buttons.getLeft() + w * 0.2f, h - w * 0.2f, IntroActivity.buttons.getRight(), IntroActivity.buttons.getBottom() + w * 0.2f);
		//		makePlatform(IntroActivity.buttons.getRight() + w * 0.1f, IntroActivity.buttons.getTop() + IntroActivity.buttons.getHeight() / 2.0f, w - w * 0.2f, IntroActivity.buttons.getBottom());
	}

	private void makePlatform(float startX, float startY, float endX, float endY)
	{
		introPlatforms.add(new Platform(BodyType.STATIC, toMeters(startX), toMeters(startY), toMeters(endX), toMeters(endY), 0, 1, 0));
	}

	public static void makePlatformsReal()
	{
		for (Platform platform : introPlatforms)
		{
			platform.create();
		}
	}

	public static void makePlatformsUnreal()
	{
		for (Platform platform : introPlatforms)
		{
			platform.destroy();
		}
	}

	public static void makeBallUnreal()
	{
		introBall.destroy();
	}

	public static void makeBallReal()
	{
		if (introBall != null)
		{
			introBall.create();
			introBall.setPosition(new Vec2(originalStartBallX, toMeters(h * 0.1f)));
			introBall.setFriction(0f);
			introBall.setVelocity(new Vec2(toMeters(w * 0.5f), 0));
		}
	}
}
