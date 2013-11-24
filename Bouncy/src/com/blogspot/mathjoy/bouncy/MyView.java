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
	public static int timeBetweenFrames = 20;
	static float ballRadius;
	static float ballX;
	static float ballY;
	static float gravitationalAcceleration;
	static float ballXSpeed;
	static float ballYSpeed;
	static boolean alreadyStarted;
	public static int color;
	public static double gravity;
	public static boolean touching = false;
	public static float currentTouchX;
	public static float currentTouchY;
	float endTouchX0;
	float endTouchY0;
	float startTouchX1;
	float endTouchX1;
	float startTouchY1;
	float endTouchY1;
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
			Thread.sleep(timeBetweenFrames);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		super.onDraw(c);
		c.drawColor(Color.BLACK);
		ballRadius = (float) (this.getHeight() / 75.0);
		gravitationalAcceleration = (float) ((this.getHeight() / 1000.0) * gravity);
		if (alreadyStarted == false)
		{
			ballX = (float) (this.getWidth() / 2.0);
			ballY = ballRadius;
			gravitationalAcceleration = (float) ((this.getHeight() / 1000.0) * gravity);
			alreadyStarted = true;
		}
		if (mode == MODE_BALL)
		{
			if (touching == true)
			{
				ballX = currentTouchX;
				ballY = currentTouchY;
				ballXSpeed = 0;
				ballYSpeed = 0;
			} else
			{
				if (ballXSpeed == 0)
				{
					if (ballYSpeed > 0)
					{
						ballAngle = 90;
					}
					if (ballYSpeed < 0)
					{
						ballAngle = -90;
					}
				} else
				{
					ballAngle = (float) Math.toDegrees(Math.atan(ballYSpeed / ballXSpeed));
				}
				if (ballXSpeed < 0)
				{
					ballAngle += 180;
				}
				speed = (float) (Math.sqrt((ballYSpeed * ballYSpeed) + (ballXSpeed * ballXSpeed)));
				if (speed > ballRadius * 2)
				{
					speed = ballRadius * 2;
					ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
					ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
				}
				ballX += ballXSpeed;
				ballY += ballYSpeed;
				// double intersectX;
				// double intersecty;
				ArrayList<Double> HitPlatAngles = new ArrayList<Double>();
				for (int i = 0; i < platforms.size(); i++)
				{
					double relativeX1;
					double relativeX2;
					double relativeY1;
					double relativeY2;
					double dx;
					double dy;
					double dr;
					double D;
					double thisPlatAngle = platforms.get(i).getAngle();
					if (thisPlatAngle < 0)
					{
						thisPlatAngle += 360;
					}
					if (thisPlatAngle >= 360)
					{
						thisPlatAngle -= 360;
					}
					if (thisPlatAngle > 180)
					{
						thisPlatAngle -= 180;
					}
					relativeX1 = platforms.get(i).getStartX() - ballX;
					relativeX2 = platforms.get(i).getEndX() - ballX;
					relativeY1 = platforms.get(i).getStartY() - ballY;
					relativeY2 = platforms.get(i).getEndY() - ballY;
					dx = relativeX2 - relativeX1;
					dy = relativeY2 - relativeY1;
					dr = Math.sqrt((dy * dy) + (dx * dx));
					D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
					double underTheRadical = ((ballRadius * ballRadius) * (dr * dr)) - (D * D);
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
					if (underTheRadical >= 0 && ((ballX >= lowX && ballX <= highX) || (ballY >= lowY && ballY <= highY)))// ((underTheRadical >= 0 && (thisPlatAngle <= 45 || thisPlatAngle >= 135) && x >= lowX && x <= highX) || (underTheRadical >= 0 && (thisPlatAngle >= 45 || thisPlatAngle <= 135) && y >= lowY && y <= highY))
					{
						if (platforms.get(i).getJustWasHit() == false)
						{
							platforms.get(i).setJustWasHit(true);
							HitPlatAngles.add(thisPlatAngle);
						}
					} else
					{
						platforms.get(i).setJustWasHit(false);
					}
				}
				if (HitPlatAngles.size() > 0)
				{
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
						if (oppoBallAngle > 180)
						{
							for (int jj = 0; jj < HitPlatAngles.size(); jj++) // this points the angles toward where the ball came from.
							{
								HitPlatAngles.set(jj, HitPlatAngles.get(jj) + 180);
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
						double rightMinDiff = 360;
						double closestRightAngle = 0;
						for (int rr = 0; rr < anglesRightOfBall.size(); rr++)
						{
							if (oppoBallAngle < 180)
							{
								if (oppoBallAngle - anglesRightOfBall.get(rr) < rightMinDiff)
								{
									rightMinDiff = oppoBallAngle - anglesRightOfBall.get(rr);
									closestRightAngle = anglesRightOfBall.get(rr);
								}
							} else if (oppoBallAngle > 180)
							{
								if (anglesRightOfBall.get(rr) - oppoBallAngle < rightMinDiff)
								{
									rightMinDiff = anglesRightOfBall.get(rr) - oppoBallAngle;
									closestRightAngle = anglesRightOfBall.get(rr);
								}
							}
						}
						if (anglesLeftOfBall.size() == 0)
						{
							ballAngle = (float) ((closestRightAngle * 2) - ballAngle);
						}
						if (anglesRightOfBall.size() == 0)
						{
							ballAngle = (float) ((closestLeftAngle * 2) - ballAngle);
						} else
						{
							ballAngle = (float) ((((closestLeftAngle + closestRightAngle) / 2.0) * 2) - oppoBallAngle);
						}
						ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
						ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
					} else if (HitPlatAngles.size() == 1)
					{
						ballAngle = (float) ((HitPlatAngles.get(0) * 2) - ballAngle);
						ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * speed);
						ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * speed);
					}
				} else
				{
					ballYSpeed += gravitationalAcceleration;
				}
				HitPlatAngles.clear();
			}
		} else if (mode == MODE_CREATE_PLATFORM)
		{
			if (touching == true)
			{
				paint.setColor(Color.WHITE);
				c.drawLine(startTouchX1, startTouchY1, currentTouchX, currentTouchY, paint);
			}
			if (currentTouchX == endTouchX1 && currentTouchY == endTouchY1 && (endTouchX1 != startTouchX1 || endTouchY1 != startTouchY1))
			{
				platforms.add(new Platform(startTouchX1, startTouchY1, endTouchX1, endTouchY1));
				currentTouchX = -1000;
				currentTouchY = -1000;
			}
		}
		paint.setColor(color);
		c.drawCircle(ballX, ballY, ballRadius, paint);
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
				startTouchX1 = event.getX();
				startTouchY1 = event.getY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			touching = false;
			if (mode == MODE_BALL)
			{
				endTouchX0 = event.getX();
				endTouchY0 = event.getY();
			}
			if (mode == MODE_CREATE_PLATFORM)
			{
				endTouchX1 = event.getX();
				endTouchY1 = event.getY();
			}
		}
		return true;
	}
}
