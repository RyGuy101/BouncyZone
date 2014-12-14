package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyView extends View implements OnTouchListener
{
	public static float originalStartBallX;
	public static float originalStartBallY;
	public static float startBallX;
	public static float startBallY;
	public static float startBallXSpeed;
	public static float startBallYSpeed;
	public static float ballRestitution = 1;
	public static float ballFriction = 1;
	public static boolean accelerometer = false;
	public static ArrayList<Shape> shapes = new ArrayList<Shape>();
	public static ArrayList<Shape> oldPlatforms = new ArrayList<Shape>();
	public static boolean alreadyStarted = false;
	public static final int MODE_BALL = 0;
	public static final int MODE_PLATFORM = 1;
	public static final int MODE_OVAL = 2;
	public static final int MODE_RECT = 3;
	public static int mode;
	public static int shapesMode = MODE_PLATFORM;
	public static int ballColor;
	static float currentTouchX;
	static float currentTouchY;
	static float startTouchX;
	static float startTouchY;
	static float endTouchX;
	static float endTouchY;
	float[] touchX = { -1000, -1000 };
	float[] touchY = { -1000, -1000 };
	static boolean touching = false;
	static boolean initialTouch = false;
	static boolean wasTouching = false;
	public static boolean beginContact = false;
	public static boolean endContact = false;
	public static boolean isIntroBall = true;

	public static Circle ball = null;

	//	public static Platform platform;
	Paint ballPaint = new Paint();
	Paint platformPaint = new Paint();
	Paint hollowShapesPaint = new Paint();
	Paint lineInBallPaint = new Paint();
	Paint startPosPaint = new Paint();
	public static boolean showLine;

	private int offScreenCounter = 0;

	//	public static boolean makeBounce = true;
	public static boolean makeBounceOnstart = true;

	public static boolean intro;
	protected static ArrayList<Platform> introPlatforms = new ArrayList<Platform>();
	public static Circle introBall = null;

	IntroActivity ia;
	MainActivity activity;

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	protected void setup()
	{
		ia = new IntroActivity();
		PPM = (float) (getResources().getDisplayMetrics().ydpi / 2.0);
		float ballRadius = 0.1f;
		originalStartBallX = toMeters((float) (this.getWidth() / 2.0));
		originalStartBallY = ballRadius;
		startBallX = originalStartBallX;
		startBallY = originalStartBallY;
		ball = new Circle(BodyType.DYNAMIC, startBallX, startBallY, ballRadius, 1.0f, ballFriction, ballRestitution);
	}

	public static void reset()
	{
		startBallX = originalStartBallX;
		startBallY = originalStartBallY;
		ball.setPosition(new Vec2(originalStartBallX, originalStartBallY));
		ball.setAngle(0);
		ball.setAngularVelocity(0);
		ball.setVelocity(new Vec2(0, 0));
	}

	//	private static int screenW;
	//	private static int screenH;

	private static float PPM;

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
		updateColors();
		drawBackground(c);
		if (alreadyStarted == false)
		{
			setup();
			alreadyStarted = true;
		}
		if (mode == MODE_BALL || intro)
		{
			WorldManager.step();
			if (beginContact && !endContact)
			{
				if (MyView.makeBounceOnstart)
				{
					IntroActivity.spoolBounce.play(IntroActivity.bounce, MainActivity.bounceVolume, MainActivity.bounceVolume, 0, 0, 1);
					if (!isIntroBall)
					{
						if (MyView.touching)
						{
							MyView.touching = false;
							MyView.wasTouching = true;
						}
						if (MainActivity.activity != null)
						{
							MainActivity.MyTask mt = MainActivity.activity.new MyTask();
							mt.execute();
						}
					}
				} else
				{
					MyView.makeBounceOnstart = true;
				}
			}
			if (!intro)
			{
				if (touching)
				{
					startBallX = ball.getX();
					startBallY = ball.getY();
					touchX[1] = touchX[0];
					touchY[1] = touchY[0];
					touchX[0] = currentTouchX;
					touchY[0] = currentTouchY;
					if (initialTouch)
					{
						initialTouch = false;
						WorldManager.setGravityTemporarily(new Vec2(0f, 0f));
					} else if (knowEnoughtouch())
					{
						ball.setVelocity(new Vec2(toMeters(touchX[0] - touchX[1]) * 60, toMeters(touchY[0] - touchY[1]) * 60));
						startBallXSpeed = toMeters(touchX[0] - touchX[1]) * 60;
						startBallYSpeed = toMeters(touchY[0] - touchY[1]) * 60;
					}
				} else if (wasTouching)
				{
					initialTouch = false;
					wasTouching = false;
					startBallX = ball.getX();
					startBallY = ball.getY();
					startBallXSpeed = ball.getVelocity().x;
					startBallYSpeed = ball.getVelocity().y;
					WorldManager.undoTemporaryGravitySet();
				} else if (!touching)
				{
					if (ballIsOffScreen() && !accelerometer)
					{
						offScreenCounter++;
					} else
					{
						offScreenCounter = 0;
					}
					if (offScreenCounter >= 60)
					{
						resetBallPosition();
					}
				}
			}
		} else if (mode == MODE_PLATFORM)
		{
			if (touching)
			{
				if (initialTouch)
				{
					initialTouch = false;
				}
				c.drawLine(startTouchX, startTouchY, currentTouchX, currentTouchY, platformPaint);
			} else if (wasTouching && platformIsLongEnough())
			{
				wasTouching = false;
				shapes.add(new Platform(BodyType.STATIC, toMeters(startTouchX), toMeters(startTouchY), toMeters(endTouchX), toMeters(endTouchY), 0, 1, 0));
			}
		} else if (mode == MODE_OVAL)
		{
			if (touching)
			{
				if (initialTouch)
				{
					initialTouch = false;
				}
				c.drawOval(new RectF(Math.min(startTouchX, currentTouchX), Math.min(startTouchY, currentTouchY), Math.max(startTouchX, currentTouchX), Math.max(startTouchY, currentTouchY)), hollowShapesPaint);
			} else if (wasTouching)
			{
				HollowOval oval = new HollowOval(BodyType.STATIC, toMeters((float) ((endTouchX + startTouchX) / 2.0)), toMeters((float) ((endTouchY + startTouchY) / 2.0)), toMeters(Math.abs(endTouchX - startTouchX)), toMeters(Math.abs(endTouchY - startTouchY)), 0, 1, 0);
				wasTouching = false;
				if (!oval.hasFailed())
				{
					shapes.add(oval);
				}
			}
		} else if (mode == MODE_RECT)
		{
			if (touching)
			{
				if (initialTouch)
				{
					initialTouch = false;
				}
				c.drawRect(Math.min(startTouchX, currentTouchX), Math.min(startTouchY, currentTouchY), Math.max(startTouchX, currentTouchX), Math.max(startTouchY, currentTouchY), hollowShapesPaint);
			} else if (wasTouching)
			{
				wasTouching = false;
				HollowRectangle rect = new HollowRectangle(BodyType.STATIC, toMeters((float) ((endTouchX + startTouchX) / 2.0)), toMeters((float) ((endTouchY + startTouchY) / 2.0)), toMeters(Math.abs(endTouchX - startTouchX)), toMeters(Math.abs(endTouchY - startTouchY)), 0, 1, 0);
				if (!rect.hasFailed())
				{
					shapes.add(rect);
				}
			}
		}
		Circle theBall;
		if (!intro)
		{
			theBall = ball;
			c.drawCircle(toPixels(startBallX), toPixels(startBallY), toPixels(theBall.getRadius()), startPosPaint);
		} else
		{
			theBall = introBall;
		}
		c.drawCircle(toPixels(theBall.getX()), toPixels(theBall.getY()), toPixels(theBall.getRadius()), ballPaint);
		if (!intro && showLine == true)
		{
			c.drawLine(toPixels((float) (theBall.getX() - theBall.getRadius() * Math.cos(theBall.getAngle()))), toPixels((float) (theBall.getY() - theBall.getRadius() * Math.sin(theBall.getAngle()))), toPixels((float) (theBall.getX() + theBall.getRadius() * Math.cos(theBall.getAngle()))), toPixels((float) (theBall.getY() + theBall.getRadius() * Math.sin(theBall.getAngle()))), lineInBallPaint);
		}
		drawPlatforms(c);
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
		makeBounceOnstart = true;
		beginContact = false;
		endContact = false;
		invalidate();
	}

	public static void resetBallPosition()
	{
		ball.setAngle(0);
		ball.setPosition(new Vec2(startBallX, startBallY));
		ball.setAngularVelocity(0);
		ball.setVelocity(new Vec2(startBallXSpeed, startBallYSpeed));
	}

	private boolean ballIsOffScreen()
	{
		return ball.getX() - ball.getRadius() > toMeters(this.getWidth()) || ball.getX() + ball.getRadius() < 0 || ball.getY() - ball.getRadius() > toMeters(this.getHeight());
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!intro)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				initialTouch = true;
				touching = true;
				currentTouchX = event.getX();
				currentTouchY = event.getY();
				startTouchX = currentTouchX;
				startTouchY = currentTouchY;
				if (mode == MODE_BALL)
				{
					ball.setPosition(new Vec2(toMeters(currentTouchX), toMeters(currentTouchY)));
					ball.setAngle(0);
					ball.setAngularVelocity(0);
					ball.setVelocity(new Vec2(0f, 0f));
					startBallXSpeed = 0;
					startBallYSpeed = 0;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP)
			{
				if (touching)
				{
					touching = false;
					wasTouching = true;
				}
				for (int i = 0; i < 2; i++)
				{
					touchX[i] = -1000;
					touchY[i] = -1000;
				}
				endTouchX = event.getX();
				endTouchY = event.getY();

			} else if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				currentTouchX = event.getX();
				currentTouchY = event.getY();
			}
			return true;
		}
		return false;
	}

	private void drawBackground(Canvas c)
	{
		c.drawColor(Color.BLACK);
	}

	private boolean knowEnoughtouch()
	{
		for (int i = 0; i < 2; i++)
		{
			if (touchX[i] == -1000 || touchY[i] == -1000)
			{
				return false;
			}
		}
		return true;
	}

	private boolean platformIsLongEnough()
	{
		return Math.sqrt((endTouchY - startTouchY) * (endTouchY - startTouchY) + (endTouchX - startTouchX) * (endTouchX - startTouchX)) > toPixels(0.1f);
	}

	private void drawPlatforms(Canvas c)
	{
		if (!intro)
		{
			for (Shape shape : shapes)
			{
				if (shape instanceof Platform)
				{
					Platform platform = (Platform) shape;
					c.drawLine(toPixels(platform.getStartX()), toPixels(platform.getStartY()), toPixels(platform.getEndX()), toPixels(platform.getEndY()), platformPaint);
				} else if (shape instanceof HollowOval)
				{
					HollowOval oval = (HollowOval) shape;
					c.drawOval(new RectF(toPixels(oval.getLeft()), toPixels(oval.getTop()), toPixels(oval.getRight()), toPixels(oval.getBottom())), hollowShapesPaint);
				} else if (shape instanceof HollowRectangle)
				{
					HollowRectangle rect = (HollowRectangle) shape;
					c.drawRect(toPixels(rect.getLeft()), toPixels(rect.getTop()), toPixels(rect.getRight()), toPixels(rect.getBottom()), hollowShapesPaint);
				}
			}
		} else
		{
			for (Platform platform : introPlatforms)
			{
				c.drawLine(toPixels(platform.getStartX()), toPixels(platform.getStartY()), toPixels(platform.getEndX()), toPixels(platform.getEndY()), platformPaint);
			}
		}
	}

	public static void clearPlatforms()
	{
		for (Shape shape : shapes)
		{
			shape.destroy();
		}
		shapes.clear();
	}

	public static void destroyLastPlatform()
	{
		shapes.get(shapes.size() - 1).destroy();
		oldPlatforms.add(shapes.get(shapes.size() - 1));
		shapes.remove(shapes.size() - 1);
	}

	public static void reCreatePlatform()
	{
		shapes.add(oldPlatforms.get(oldPlatforms.size() - 1));
		oldPlatforms.remove(oldPlatforms.size() - 1);
		shapes.get(shapes.size() - 1).create();
	}

	public static void makePlatformsReal()
	{
		for (Shape shape : shapes)
		{
			shape.create();
		}
	}

	public static void makePlatformsUnreal()
	{
		for (Shape shape : shapes)
		{
			shape.destroy();
		}
	}

	public static void makeBallUnreal()
	{
		if (ball != null)
		{
			ball.destroy();
		}
	}

	public static void makeBallReal()
	{
		ball.reCreate();
	}

	private void updateColors()
	{
		platformPaint.setColor(Color.WHITE);
		platformPaint.setStrokeWidth(toPixels(0.02f));
		hollowShapesPaint.setColor(Color.WHITE);
		hollowShapesPaint.setStyle(Style.STROKE);
		ballPaint.setColor(ballColor);
		lineInBallPaint.setColor(Color.BLACK);
		lineInBallPaint.setAlpha(79);
		lineInBallPaint.setStrokeWidth(toPixels(0.04f));
		startPosPaint.setColor(Color.rgb(31, 31, 31));
	}
}
