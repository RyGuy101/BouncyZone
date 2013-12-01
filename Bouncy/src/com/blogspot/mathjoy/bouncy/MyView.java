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
	public static int ballColor;
	public static double gAccelerationMultiplier;
	public static boolean touchingScreen = false;
	public static float currentTouchX;
	public static float currentTouchY;
	float endTouchXMode0;
	float endTouchYMode0;
	float startTouchXMode1;
	float endTouchXMode1;
	float startTouchYMode1;
	float endTouchYMode1;
	public static final int MODE_BALL = 0;
	public static final int MODE_CREATE_PLATFORM = 1;
	public static final int MODE_MOVE_PLATFORM = 2;
	public static final int MODE_DELETE_PLATFORM = 3;
	public static int mode = 0;
	static ArrayList<Platform> platforms = new ArrayList<Platform>();
	static float ballAngle;// Math.atan(xSpeed/ySpeed)
	static float ballSpeed;//
	ArrayList<Double> HitPlatAngles = new ArrayList<Double>();
	ArrayList<Double> anglesLeftOfBall = new ArrayList<Double>();
	ArrayList<Double> anglesRightOfBall = new ArrayList<Double>();
	
	double relativeX1;
	double relativeX2;
	double relativeY1;
	double relativeY2;
	double dx;
	double dy;
	double dr;
	double D;
	double underTheRadical;
	double highX;
	double lowX;
	double highY;
	double lowY;
	double thisPlatAngle;

	public MyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
	}

	public MyView(Context context)
	{
		super(context);
		// Platform testLine = new Platform(10, 10, 100, 100);
		// platforms.add(testLine);
	}

	@Override
	protected void onDraw(Canvas c)
	{
		sleep();
		super.onDraw(c);
		c.drawColor(Color.BLACK);
		gravitationalAcceleration = (float) ((this.getHeight() / 1000.0) * gAccelerationMultiplier);
		if (alreadyStarted == false)
		{
			setUpEssentials();
			alreadyStarted = true;
		}
		if (mode == MODE_BALL)
		{
			if (touchingScreen == true)
			{
				ballX = currentTouchX;
				ballY = currentTouchY;
				updateBallXAndYSpeedBasedOnTouch();
			} else
			{
				updateBallAngle();
				updateBallSpeed();
				checkIfBallIsTooFast();
				ballX += ballXSpeed;
				ballY += ballYSpeed;
				// double intersectX;
				// double intersecty;
				for (int i = 0; i < platforms.size(); i++)
				{
					thisPlatAngle = getThisPlatAngle(i);
					doIntersectionCalculations(i);
					establishHighAndLowValuePlatformPositions(i);
					checkForHitPlatformsAndAddThemToHitPlatList(i);
				}
				if (HitPlatAngles.size() > 0)
				{
					if (HitPlatAngles.size() > 1)
					{
						
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
						ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * ballSpeed);
						ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * ballSpeed);
					} else if (HitPlatAngles.size() == 1)
					{
						ballAngle = (float) ((HitPlatAngles.get(0) * 2) - ballAngle);
						ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * ballSpeed);
						ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * ballSpeed);
					}
				} else
				{
					ballYSpeed += gravitationalAcceleration;
				}
				HitPlatAngles.clear();
				anglesLeftOfBall.clear();
				anglesRightOfBall.clear();
			}
		} else if (mode == MODE_CREATE_PLATFORM)
		{
			if (touchingScreen == true)
			{
				paint.setColor(Color.WHITE);
				c.drawLine(startTouchXMode1, startTouchYMode1, currentTouchX, currentTouchY, paint);
			}
			if (currentTouchX == endTouchXMode1 && currentTouchY == endTouchYMode1 && (endTouchXMode1 != startTouchXMode1 || endTouchYMode1 != startTouchYMode1))
			{
				platforms.add(new Platform(startTouchXMode1, startTouchYMode1, endTouchXMode1, endTouchYMode1));
				currentTouchX = -1000;
				currentTouchY = -1000;
			}
		}
		paint.setColor(ballColor);
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

	private void checkForHitPlatformsAndAddThemToHitPlatList(int index)
	{
		if (underTheRadical >= 0 && ((ballX >= lowX && ballX <= highX) || (ballY >= lowY && ballY <= highY)))// ((underTheRadical >= 0 && (thisPlatAngle <= 45 || thisPlatAngle >= 135) && x >= lowX && x <= highX) || (underTheRadical >= 0 && (thisPlatAngle >= 45 || thisPlatAngle <= 135) && y >= lowY && y <= highY))
		{
			if (platforms.get(index).getJustWasHit() == false)
			{
				platforms.get(index).setJustWasHit(true);
				HitPlatAngles.add(thisPlatAngle);
			}
		} else
		{
			platforms.get(index).setJustWasHit(false);
		}
	}

	private void establishHighAndLowValuePlatformPositions(int index)
	{
		if (platforms.get(index).getEndX() > platforms.get(index).getStartX())
		{
			highX = platforms.get(index).getEndX();
			lowX = platforms.get(index).getStartX();
		} else
		{
			highX = platforms.get(index).getStartX();
			lowX = platforms.get(index).getEndX();
		}
		if (platforms.get(index).getEndY() > platforms.get(index).getStartY())
		{
			highY = platforms.get(index).getEndY();
			lowY = platforms.get(index).getStartY();
		} else
		{
			highY = platforms.get(index).getStartY();
			lowY = platforms.get(index).getEndY();
		}
	}

	private void doIntersectionCalculations(int index)
	{
		relativeX1 = platforms.get(index).getStartX() - ballX;
		relativeX2 = platforms.get(index).getEndX() - ballX;
		relativeY1 = platforms.get(index).getStartY() - ballY;
		relativeY2 = platforms.get(index).getEndY() - ballY;
		dx = relativeX2 - relativeX1;
		dy = relativeY2 - relativeY1;
		dr = Math.sqrt((dy * dy) + (dx * dx));
		D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
		underTheRadical = ((ballRadius * ballRadius) * (dr * dr)) - (D * D);
	}

	private double getThisPlatAngle(int index)
	{
		double thisPlatAngle = platforms.get(index).getAngle();
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
		return thisPlatAngle;
	}

	private void checkIfBallIsTooFast()
	{
		if (ballSpeed > ballRadius * 2)
		{
			ballSpeed = ballRadius * 2;
			ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * ballSpeed);
			ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * ballSpeed);
		}
	}

	private void updateBallSpeed()
	{
		ballSpeed = (float) (Math.sqrt((ballYSpeed * ballYSpeed) + (ballXSpeed * ballXSpeed)));
	}

	private void updateBallAngle()
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
		if (ballAngle < 0)
		{
			ballAngle += 360;
		} else if (ballAngle >= 360)
		{
			ballAngle -= 360;
		}
	}

	private void updateBallXAndYSpeedBasedOnTouch()
	{
		ballXSpeed = 0;
		ballYSpeed = 0;
	}

	private void setUpEssentials()
	{
		ballRadius = (float) (this.getHeight() / 75.0);
		ballX = (float) (this.getWidth() / 2.0);
		ballY = ballRadius;
		gravitationalAcceleration = (float) ((this.getHeight() / 1000.0) * gAccelerationMultiplier);
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(timeBetweenFrames);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// Junk
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		currentTouchX = event.getX();
		currentTouchY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			touchingScreen = true;
			if (mode == MODE_CREATE_PLATFORM)
			{
				startTouchXMode1 = event.getX();
				startTouchYMode1 = event.getY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			touchingScreen = false;
			if (mode == MODE_BALL)
			{
				endTouchXMode0 = event.getX();
				endTouchYMode0 = event.getY();
			}
			if (mode == MODE_CREATE_PLATFORM)
			{
				endTouchXMode1 = event.getX();
				endTouchYMode1 = event.getY();
			}
		}
		return true;
	}
}
