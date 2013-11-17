package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	float sumOfAngles;
	double relativeX1;
	double relativeX2;
	double relativeY1;
	double relativeY2;
	double dx;
	double dy;
	double dr;
	double D;
	double intersectX;
	double intersecty;
	ArrayList<Double> HitPlatAngles = new ArrayList<Double>();
	ArrayList<Double> anglesLeftOfBall;
	ArrayList<Double> anglesRightOfBall;
	static boolean alreadyStarted;
	public static int color;
	public static double gravity;
	public static boolean touching = false;
	public static float currentTouchX;
	public static float currentTouchY;
	float startTouchX;
	float endTouchX;
	float startTouchY;
	float endTouchY;
	public static final int MODE_BALL = 0;
	public static final int MODE_CREATE_PLATFORM = 1;
	public static final int MODE_MOVE_PLATFORM = 2;
	public static final int MODE_DELETE_PLATFORM = 3;
	public static int mode = 0;
	static ArrayList<Platform> platforms = new ArrayList<Platform>();
	float ballAngle = 90;// Math.atan(xSpeed/ySpeed)
	float speed = 0;//

	public MyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
	}

	public MyView(Context context)
	{
		super(context);
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
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
		if (mode == MODE_BALL)
		{
			if (touching == true)
			{
				x = currentTouchX;
				y = currentTouchY;
				xSpeed = 0;
				ySpeed = 0;
			} else
			{
				// if (y >= this.getHeight() - radius)
				// {
				// ySpeed *= -1;
				// } else
				// {
				for (int i = 0; i < platforms.size(); i++)
				{
					relativeX1 = platforms.get(i).getStartX() - x;
					relativeX2 = platforms.get(i).getEndX() - x;
					relativeY1 = platforms.get(i).getStartY() - y;
					relativeY2 = platforms.get(i).getEndY() - y;
					dx = relativeX2 - relativeX1;
					dy = relativeY2 - relativeY1;
					dr = Math.sqrt((dy * dy) + (dx * dx));
					D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
					double underTheRadical = ((radius * radius) * (dr * dr)) - (D * D);
					double highX;
					double lowX;
					double highY;
					double lowY;
					if (platforms.get(i).getEndX() > platforms.get(i).getStartX())
					{
						highX = platforms.get(i).getEndX();
						lowX = platforms.get(i).getStartX();
					} else
					{
						highX = platforms.get(i).getStartX();
						lowX = platforms.get(i).getEndX();
					}
					if (platforms.get(i).getEndY() > platforms.get(i).getStartY())
					{
						highY = platforms.get(i).getEndY();
						lowY = platforms.get(i).getStartY();
					} else
					{
						highY = platforms.get(i).getStartY();
						lowY = platforms.get(i).getEndY();
					}
					if (underTheRadical >= 0 && ((x >= lowX && x <= highX) || (y >= lowY && y <= highY)))
					{
						if (platforms.get(i).getJustWasHit() == false)
						{
							platforms.get(i).setJustWasHit(true);
							HitPlatAngles.add(platforms.get(i).getAngle());
							// if (angle < 0)
							// {
							// angle += 360;
							// }
							// sumOfAngles += (float) ((platforms.get(i).getAngle() * 2) - angle);
							// angle = sumOfAngles / numPrevHitPlat;
							// xSpeed = (float) (Math.cos(Math.toRadians(angle)) * speed);
							// ySpeed = (float) (Math.sin(Math.toRadians(angle)) * speed);
							// break;
						}
					} else
					{
						platforms.get(i).setJustWasHit(false);
					}
				}
				if (HitPlatAngles.size() > 0)
				{
					if (xSpeed == 0)
					{
						if (ySpeed > 0)
						{
							ballAngle = 90;
						}
						if (ySpeed < 0)
						{
							ballAngle = -90;
						}
					} else
					{
						ballAngle = (float) Math.toDegrees(Math.atan(ySpeed / xSpeed));
					}
					speed = (float) (Math.sqrt((ySpeed * ySpeed) + (xSpeed * xSpeed)));
					if (xSpeed < 0)
					{
						speed *= -1;
					}
					if (HitPlatAngles.size() > 1)
					{
						if (ballAngle < 0)
						{
							ballAngle += 360;
						} else if (ballAngle >= 360)
						{
							ballAngle -= 360;
						}
						double oppoBallAngle = 0;
						if (ballAngle > 180)
						{
							oppoBallAngle = ballAngle - 180;
						} else if (ballAngle < 180)
						{
							oppoBallAngle = ballAngle + 180;
						}
						for (int jj = 0; jj < HitPlatAngles.size(); jj++)
						{
							if (HitPlatAngles.get(jj) < 0)
							{
								HitPlatAngles.set(jj, HitPlatAngles.get(jj) + 360);
							} else if (HitPlatAngles.get(jj) >= 360)
							{
								HitPlatAngles.set(jj, HitPlatAngles.get(jj) - 360);
							}
						}
						for (int jj = 0; jj < HitPlatAngles.size(); jj++) // this points the angles toward where the ball came from.
						{
							if (oppoBallAngle < 180)
							{
								if (HitPlatAngles.get(jj) > 180)
								{
									HitPlatAngles.set(jj, HitPlatAngles.get(jj) - 180);
								}
							} else if (oppoBallAngle > 180)
							{
								if (HitPlatAngles.get(jj) < 180)
								{
									HitPlatAngles.set(jj, HitPlatAngles.get(jj) + 180);
								}
							}
						}
						ArrayList<Double> anglesLeftOfBall = new ArrayList<Double>();
						ArrayList<Double> anglesRightOfBall = new ArrayList<Double>();
						for (int jj = 0; jj < HitPlatAngles.size(); jj++)
						{
							if (oppoBallAngle < 180)
							{
								if (HitPlatAngles.get(jj) < oppoBallAngle)
								{
									anglesRightOfBall.add(HitPlatAngles.get(jj));
								} else if (HitPlatAngles.get(jj) > oppoBallAngle)
								{
									anglesLeftOfBall.add(HitPlatAngles.get(jj));
								}
							} else if (oppoBallAngle > 180)
							{
								if (HitPlatAngles.get(jj) > oppoBallAngle)
								{
									anglesRightOfBall.add(HitPlatAngles.get(jj));
								} else if (HitPlatAngles.get(jj) < oppoBallAngle)
								{
									anglesLeftOfBall.add(HitPlatAngles.get(jj));
								}
							}
						}
						double leftMinDiff = 360;
						double closestLeftAngle = 0;
						for (int ll = 0; ll < anglesLeftOfBall.size(); ll++)
						{
							if (oppoBallAngle < 180)
							{
								if (anglesLeftOfBall.get(ll) - oppoBallAngle < leftMinDiff)
								{
									leftMinDiff = anglesLeftOfBall.get(ll) - oppoBallAngle;
									closestLeftAngle = anglesLeftOfBall.get(ll);
								}
							} else if (oppoBallAngle > 180)
							{
								if (oppoBallAngle - anglesLeftOfBall.get(ll) < leftMinDiff)
								{
									leftMinDiff = oppoBallAngle - anglesLeftOfBall.get(ll);
									closestLeftAngle = anglesLeftOfBall.get(ll);
								}
							}
						}
						double RightMinDiff = 360;
						double closestRightAngle = 0;
						for (int rr = 0; rr < anglesRightOfBall.size(); rr++)
						{
							if (oppoBallAngle < 180)
							{
								if (oppoBallAngle - anglesRightOfBall.get(rr) < RightMinDiff)
								{
									RightMinDiff = oppoBallAngle - anglesRightOfBall.get(rr);
									closestRightAngle = anglesRightOfBall.get(rr);
								}
							} else if (oppoBallAngle > 180)
							{
								if (anglesRightOfBall.get(rr) - oppoBallAngle < RightMinDiff)
								{
									RightMinDiff = anglesRightOfBall.get(rr) - oppoBallAngle;
									closestRightAngle = anglesRightOfBall.get(rr);
								}
							}
						}
						speed *= -1;
						ballAngle = (float) (((closestLeftAngle + closestRightAngle) / 2.0) * 2.0) - ballAngle;
						xSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
						ySpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
					} else if (HitPlatAngles.size() == 1)
					{
						ballAngle = (float) ((HitPlatAngles.get(0) * 2) - ballAngle);
						xSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
						ySpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
					}
				} else
				{
					ySpeed += acceleration;
				}
				HitPlatAngles.clear();
				speed = (float) (Math.sqrt((ySpeed * ySpeed) + (xSpeed * xSpeed)));
				if (xSpeed < 0)
				{
					speed *= -1;
				}
				if (speed > radius * 2)
				{
					speed = radius * 2;
				} else if (speed < -radius * 2)
				{
					speed = -radius * 2;
				}
				xSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
				ySpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
				x += xSpeed;
				y += ySpeed;
			}
		} else if (mode == MODE_CREATE_PLATFORM)
		{
			if (touching == true)
			{
				paint.setColor(Color.WHITE);
				c.drawLine(startTouchX, startTouchY, currentTouchX, currentTouchY, paint);
			}
			if (currentTouchX == endTouchX && currentTouchY == endTouchY && (endTouchX != startTouchX || endTouchY != startTouchY))
			{
				platforms.add(new Platform(startTouchX, startTouchY, endTouchX, endTouchY));
				currentTouchX = -1000;
				currentTouchY = -1000;
			}
		}
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
		currentTouchX = event.getX();
		currentTouchY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touching = true;
			if (mode == MODE_CREATE_PLATFORM)
			{
				startTouchX = event.getX();
				startTouchY = event.getY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			touching = false;
			if (mode == MODE_CREATE_PLATFORM)
			{
				endTouchX = event.getX();
				endTouchY = event.getY();
			}
		}
		return true;
	}
}
