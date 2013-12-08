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
	double underTheRadical;
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
					Platform thisPlat = platforms.get(i);
					thisPlatAngle = thisPlat.getAngle();
					doIntersectionCalculations(thisPlat);
					checkIfPlatformIsHitAndAddItToHitPlatList(thisPlat);
				}
				if (HitPlatAngles.size() > 0)
				{
					if (HitPlatAngles.size() > 1)
					{
						double oppoBallAngle = oppositeBallAngle();
						if (oppoBallAngle > 180)
						{
							for (int jj = 0; jj < HitPlatAngles.size(); jj++) // this points the angles toward where the ball came from.
							{
								HitPlatAngles.set(jj, HitPlatAngles.get(jj) + 180);
							}
						}
						for (int jj = 0; jj < HitPlatAngles.size(); jj++)
						{
							sortAngleToRightOrLeftOfBall(oppoBallAngle, HitPlatAngles.get(jj));
						}
						double closestLeftAngle = closestLeftAngle(oppoBallAngle, anglesLeftOfBall);
						double closestRightAngle = closestRightAngle(oppoBallAngle, anglesRightOfBall);
						updateBallAngleBasedOnTwoPlatforms(oppoBallAngle, closestLeftAngle, closestRightAngle);
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

	private void updateBallAngleBasedOnTwoPlatforms(double oppoBallAngle, double leftAngle, double rightAngle)
	{
		if (leftAngle == -1)
		{
			ballAngle = (float) ((rightAngle * 2) - ballAngle);
		} else if (rightAngle == -1)
		{
			ballAngle = (float) ((leftAngle * 2) - ballAngle);
		} else
		{
			ballAngle = (float) ((((leftAngle + rightAngle) / 2.0) * 2) - oppoBallAngle);
		}
	}

	private double closestRightAngle(double compareAngle, ArrayList<Double> angles)
	{
		double closestRightAngle = -1;
		double rightMinDiff = 360;
		for (int rr = 0; rr < angles.size(); rr++)
		{
			if (compareAngle < 180)
			{
				if (compareAngle - angles.get(rr) < rightMinDiff)
				{
					rightMinDiff = compareAngle - angles.get(rr);
					closestRightAngle = angles.get(rr);
				}
			} else if (compareAngle > 180)
			{
				if (angles.get(rr) - compareAngle < rightMinDiff)
				{
					rightMinDiff = angles.get(rr) - compareAngle;
					closestRightAngle = angles.get(rr);
				}
			}
		}
		return closestRightAngle;
	}

	private double closestLeftAngle(double compareAngle, ArrayList<Double> angles)
	{
		double closestLeftAngle = -1;
		double leftMinDiff = 360;
		for (int ll = 0; ll < angles.size(); ll++)
		{
			if (compareAngle < 180)
			{
				if (angles.get(ll) - compareAngle < leftMinDiff)
				{
					leftMinDiff = angles.get(ll) - compareAngle;
					closestLeftAngle = angles.get(ll);
				}
			} else if (compareAngle > 180)
			{
				if (compareAngle - angles.get(ll) < leftMinDiff)
				{
					leftMinDiff = compareAngle - angles.get(ll);
					closestLeftAngle = angles.get(ll);
				}
			}
		}
		return closestLeftAngle;
	}

	private void sortAngleToRightOrLeftOfBall(double compareAngle, Double platAngle)
	{
		if (compareAngle < 180)
		{
			if (platAngle < compareAngle)
			{
				anglesRightOfBall.add(platAngle);
			} else if (platAngle > compareAngle)
			{
				anglesLeftOfBall.add(platAngle);
			}
		} else if (compareAngle > 180)
		{
			if (platAngle > compareAngle)
			{
				anglesRightOfBall.add(platAngle);
			} else if (platAngle < compareAngle)
			{
				anglesLeftOfBall.add(platAngle);
			}
		}
	}

	private double oppositeBallAngle()
	{
		double oppoBallAngle = 0;
		if (ballAngle > 180)
		{
			oppoBallAngle = ballAngle - 180;
		} else if (ballAngle < 180)
		{
			oppoBallAngle = ballAngle + 180;
		}
		return oppoBallAngle;
	}

	private void checkIfPlatformIsHitAndAddItToHitPlatList(Platform platform)
	{
		if (underTheRadical >= 0 && ((ballX >= platform.getLowX() && ballX <= platform.getHighX()) || (ballY >= platform.getLowY() && ballY <= platform.getHighY())))// ((underTheRadical >= 0 && (thisPlatAngle <= 45 || thisPlatAngle >= 135) && x >= lowX && x <= highX) || (underTheRadical >= 0 && (thisPlatAngle >= 45 || thisPlatAngle <= 135) && y >= lowY && y <= highY))
		{
			if (platform.getJustWasHit() == false)
			{
				platform.setJustWasHit(true);
				HitPlatAngles.add(platform.getAngle());
			}
		} else
		{
			platform.setJustWasHit(false);
		}
	}

	private void doIntersectionCalculations(Platform platform)
	{
		double relativeX1;
		double relativeX2;
		double relativeY1;
		double relativeY2;
		double dx;
		double dy;
		double dr;
		double D;
		relativeX1 = platform.getStartX() - ballX;
		relativeX2 = platform.getEndX() - ballX;
		relativeY1 = platform.getStartY() - ballY;
		relativeY2 = platform.getEndY() - ballY;
		dx = relativeX2 - relativeX1;
		dy = relativeY2 - relativeY1;
		dr = Math.sqrt((dy * dy) + (dx * dx));
		D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
		underTheRadical = ((ballRadius * ballRadius) * (dr * dr)) - (D * D);
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
