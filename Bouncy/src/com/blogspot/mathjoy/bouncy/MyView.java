package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyView extends View implements OnTouchListener
{
	// static MediaPlayer bounce = MainActivity.bounce;
	static SoundPool sp = MainActivity.spool;
	static int bounce = MainActivity.bounce;
	public static float bounceVolume = (float) 0.6;
	Paint platformPaint = new Paint();
	Paint ballPaint = new Paint();
	public static int timeBetweenFrames = 20;
	static int offScreenCounter = 0;
	static float ballRadius;
	static float theoreticalRadius;
	static float ballX;
	static float ballY;
	static float drawBallX = -1000;
	static float drawBallY = -1000;
	public static float startBallX;
	public static float startBallY;
	public static float startBallXSpeed = 0;
	public static float startBallYSpeed = 0;
	static float gravitationalAcceleration;
	static float ballXSpeed;
	static float ballYSpeed;
	static boolean alreadyStarted;
	public static int ballColor;
	public static double gAccelerationMultiplier;
	public static double bounceFactor;
	public static boolean touchingScreen = false;
	public static boolean wasJustTouchingScreen = false;// doesn't affect anything right now
	int touchMemory = 2;
	public static float currentTouchX;
	public static float currentTouchY;
	ArrayList<Float> TouchXMode0 = new ArrayList<Float>();
	ArrayList<Float> TouchYMode0 = new ArrayList<Float>();
	float startTouchXMode1;
	float endTouchXMode1;
	float startTouchYMode1;
	float endTouchYMode1;
	public static final int MODE_BALL = 0;
	public static final int MODE_CREATE_PLATFORM = 1;
	// public static final int MODE_MOVE_PLATFORM = 2;
	// public static final int MODE_DELETE_PLATFORM = 3;
	public static int mode = 0;
	public static ArrayList<Platform> platforms = new ArrayList<Platform>();
	static double ballAngle;
	static float ballSpeed;
	static float ballSpeedBeforeBounce;
	static ArrayList<Platform> HitPlats = new ArrayList<Platform>();
	static boolean rolling;

	public MyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		for (int i = 0; i < touchMemory; i++)
		{
			TouchXMode0.add(null);
			TouchYMode0.add(null);
		}
	}

	public MyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		for (int i = 0; i < touchMemory; i++)
		{
			TouchXMode0.add(null);
			TouchYMode0.add(null);
		}
	}

	public MyView(Context context)
	{
		super(context);
		for (int i = 0; i < touchMemory; i++)
		{
			TouchXMode0.add(null);
			TouchYMode0.add(null);
		}
	}

	@Override
	protected void onDraw(Canvas c)
	{
		sleep();
		super.onDraw(c);
		drawBackground(c);
		updateGravity();
		updateColors();
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
				startBallX = currentTouchX;
				startBallY = currentTouchY;
				for (int i = touchMemory - 1; i > 0; i--)
				{
					TouchXMode0.set(i, TouchXMode0.get(i - 1));
					TouchYMode0.set(i, TouchYMode0.get(i - 1));
				}
				TouchXMode0.set(0, currentTouchX);
				TouchYMode0.set(0, currentTouchY);
				updateBallXAndYSpeedBasedOnTouch();
				startBallXSpeed = ballXSpeed;
				startBallYSpeed = ballYSpeed;
				wasJustTouchingScreen = true;
			} // else
			{
				updateBallAngle();
				updateBallSpeed();
				updateTheoreticalRadius();
				if (!touchingScreen)
				{
					updateBallPosition();
					if (ballX - theoreticalRadius > this.getWidth() || ballX + theoreticalRadius < 0 || ballY - theoreticalRadius > this.getHeight())
					{
						offScreenCounter++;
					} else
					{
						offScreenCounter = 0;
					}
					if (offScreenCounter >= 50)
					{
						ballX = startBallX;
						ballY = startBallY;
						ballXSpeed = startBallXSpeed;
						ballYSpeed = startBallYSpeed;
						updateBallAngle();
						updateBallSpeed();
						updateTheoreticalRadius();
					}
				}
				for (int i = 0; i < platforms.size(); i++)
				{
					Platform thisPlat = platforms.get(i);
					checkIfPlatformIsHitAndAddItToHitPlatList(thisPlat);
				}
				if (HitPlats.size() > 0)
				{
					touchingScreen = false;
					if (HitPlats.size() > 1)
					{
						double oppoBallAngle = oppositeOfBallAngle();
						int initialSize = HitPlats.size();
						for (int jj = 0; jj < initialSize; jj++)
						{
							if (oppoBallAngle - HitPlats.get(jj).getAngle() <= 180 && oppoBallAngle - HitPlats.get(jj).getAngle() >= 0)
							{
								HitPlats.add(new Platform(HitPlats.get(jj).getStartX(), HitPlats.get(jj).getStartY(), HitPlats.get(jj).getEndX(), HitPlats.get(jj).getEndY()));
								HitPlats.get(HitPlats.size() - 1).setAngle(HitPlats.get(HitPlats.size() - 1).getAngle() + 180);
							} else if (oppoBallAngle - HitPlats.get(jj).getAngle() > 180)
							{
								HitPlats.add(new Platform(HitPlats.get(jj).getStartX(), HitPlats.get(jj).getStartY(), HitPlats.get(jj).getEndX(), HitPlats.get(jj).getEndY()));
								HitPlats.get(HitPlats.size() - 1).setAngle(HitPlats.get(HitPlats.size() - 1).getAngle() + 360);
								HitPlats.get(jj).setAngle(HitPlats.get(jj).getAngle() + 180);
							} else if (oppoBallAngle - HitPlats.get(jj).getAngle() < 0)
							{
								HitPlats.add(new Platform(HitPlats.get(jj).getStartX(), HitPlats.get(jj).getStartY(), HitPlats.get(jj).getEndX(), HitPlats.get(jj).getEndY()));
								HitPlats.get(HitPlats.size() - 1).setAngle(HitPlats.get(HitPlats.size() - 1).getAngle() - 180);
							}
						}
						for (int jj = 0; jj < HitPlats.size(); jj++)
						{
							HitPlats.get(jj).setAngle(HitPlats.get(jj).getAngle() - oppoBallAngle);
						}
						ArrayList<Platform> platformsLeftOfBall = new ArrayList<Platform>();
						ArrayList<Platform> platformsRightOfBall = new ArrayList<Platform>();
						for (int jj = 0; jj < HitPlats.size(); jj++)
						{
							if (HitPlats.get(jj).getAngle() < 0)
							{
								platformsLeftOfBall.add(HitPlats.get(jj));
							} else
							{
								platformsRightOfBall.add(HitPlats.get(jj));
							}
						}
						Platform closestLeftPlatform = null;
						if (!platformsLeftOfBall.isEmpty())
						{
							closestLeftPlatform = platformsLeftOfBall.get(0);
							for (int jj = 1; jj < platformsLeftOfBall.size(); jj++)
							{
								if (platformsLeftOfBall.get(jj).getAngle() > closestLeftPlatform.getAngle())
								{
									closestLeftPlatform = platformsLeftOfBall.get(jj);
								}
							}
						}
						Platform closestRightPlatform = null;
						if (!platformsRightOfBall.isEmpty())
						{
							closestRightPlatform = platformsRightOfBall.get(0);
							for (int jj = 1; jj < platformsRightOfBall.size(); jj++)
							{
								if (platformsRightOfBall.get(jj).getAngle() < closestRightPlatform.getAngle())
								{
									closestRightPlatform = platformsRightOfBall.get(jj);
								}
							}
						}
						// closestLeftPlatform.setAngle(fixAngleToLessThan180(closestLeftPlatform.getAngle()));
						// closestRightPlatform.setAngle(fixAngleToLessThan180(closestRightPlatform.getAngle()));
						if (Math.abs(Math.sin(Math.toRadians(ballAngle - closestLeftPlatform.getAngle())) * ballSpeed) >= gravitationalAcceleration || Math.abs(Math.sin(Math.toRadians(ballAngle - closestRightPlatform.getAngle())) * ballSpeed) >= gravitationalAcceleration)
						{
							// bounce.start();
							sp.play(bounce, bounceVolume, bounceVolume, 0, 0, 1);
						}
						updateBallAngleBasedOnTwoPlatforms(closestLeftPlatform, closestRightPlatform);
						double backupBallAngle = ballAngle;
						// float oldBallSpeed = ballSpeed;
						updateSpeedWithBounceFactor();
						float backupBallSpeed = ballSpeed;
						updateBallXAndYSpeed();
						float backupBallX = ballX;
						float backupBallY = ballY;
						// boolean checkForIntersection = true;
						// double averageAngle = ((closestLeftPlatform.getAngle() + closestRightPlatform.getAngle()) / 2.0);
						// averageAngle = fixAngleToLessThan180(averageAngle);
						// if (ballAngle > averageAngle)
						// {
						// while (ballAngle - averageAngle > 0)
						// {
						// simulateBall();
						// if (ballY > closestLeftPlatform.getHighY() && ballY > closestRightPlatform.getHighY())
						// {
						// checkForIntersection = false;
						// break;
						// }
						// }
						// } else if (ballAngle < averageAngle)
						// {
						// while (ballAngle - averageAngle < 0)
						// {
						// simulateBall();
						// if (ballY > closestLeftPlatform.getHighY() && ballY > closestRightPlatform.getHighY())
						// {
						// checkForIntersection = false;
						// break;
						// }
						// }
						// }
						// if (((isOnInfintiteLine(closestLeftPlatform) && isWithinBoundsOfPlatform(closestLeftPlatform)) || (isOnInfintiteLine(closestRightPlatform) && isWithinBoundsOfPlatform(closestRightPlatform))) && checkForIntersection == true)
						// {
						// float ballXDiff = ballX - backupBallX;
						// float ballYDiff = ballY - backupBallY;
						// if (oppoBallAngle > 180)
						// {
						/*
						 * WORK IN PROGRESS
						 * closestLeftPlatform.setAngle(closestLeftPlatform.getAngle() + oppoBallAngle);
						 * closestRightPlatform.setAngle(closestRightPlatform.getAngle() + oppoBallAngle);
						 * double averageAngle = (closestLeftPlatform.getAngle() + closestRightPlatform.getAngle()) / 2.0;
						 * if (oppoBallAngle > 180)
						 * {
						 * closestLeftPlatform.setAngle(fixAngleToMoreThan180(closestLeftPlatform.getAngle()));
						 * closestRightPlatform.setAngle(fixAngleToMoreThan180(closestRightPlatform.getAngle()));
						 * averageAngle = fixAngleToMoreThan180(averageAngle);
						 * } else if (oppoBallAngle < 180)
						 * {
						 * closestLeftPlatform.setAngle(fixAngleToLessThan180(closestLeftPlatform.getAngle()));
						 * closestRightPlatform.setAngle(fixAngleToLessThan180(closestRightPlatform.getAngle()));
						 * averageAngle = fixAngleToLessThan180(averageAngle);
						 * }
						 * float ballXDiff = ballX - backupBallX;
						 * float ballYDiff = ballY - backupBallY;
						 * simulateBall();
						 * while ((isOnInfintiteLine(closestLeftPlatform) && isWithinBoundsOfPlatform(closestLeftPlatform)) || (isOnInfintiteLine(closestRightPlatform) && isWithinBoundsOfPlatform(closestRightPlatform)))
						 * {
						 * ballX += Math.cos(Math.toRadians(averageAngle)) / 100.0;
						 * ballY += Math.sin(Math.toRadians(averageAngle)) / 100.0;
						 * }
						 * 
						 * ballX -= ballXDiff;
						 * ballY -= ballYDiff;
						 * ballAngle = backupBallAngle;
						 * ballSpeed = backupBallSpeed;
						 * updateBallXAndYSpeed();
						 * rolling = true;
						 */
						// }
						// // updateBallYSpeedBasedOnGravity();
						// // updateBallAngle();
						// // updateBallSpeed();
						// // updateBallXAndYSpeed();
						// rolling = true;
						// } else
						// {
						// ballX = backupBallX;
						// ballY = backupBallY;
						// ballAngle = backupBallAngle;
						// ballSpeed = backupBallSpeed;
						// updateBallXAndYSpeed();
						// rolling = false;
						// }
					} else if (HitPlats.size() == 1)
					{
						Platform platform = HitPlats.get(0);
						if (Math.abs(Math.sin(Math.toRadians(ballAngle - platform.getAngle())) * ballSpeed) >= gravitationalAcceleration)
						{
							// bounce.start();
							sp.play(bounce, bounceVolume, bounceVolume, 0, 0, 1);
							rolling = false;
							updateBallAngleBasedOnOnePlatform(platform.getAngle());
							double backupBallAngle = ballAngle;
							// float oldBallSpeed = ballSpeed;
							updateSpeedWithBounceFactor();
							float backupBallSpeed = ballSpeed;
							updateBallXAndYSpeed();
							float backupBallX = ballX;
							float backupBallY = ballY;
							boolean checkForIntersection = true;
							if (gravitationalAcceleration != 0)
							{
								if (ballAngle > platform.getAngle())
								{
									while (ballAngle - platform.getAngle() > 0)
									{
										simulateBall();
										if (ballY > platform.getHighY())
										{
											checkForIntersection = false;
											break;
										}
									}
								} else if (ballAngle < platform.getAngle())
								{
									while (ballAngle - platform.getAngle() < 0)
									{
										simulateBall();
										if (ballY > platform.getHighY())
										{
											checkForIntersection = false;
											break;
										}
									}
								}
								if (isOnInfintiteLine(platform) && isWithinBoundsOfPlatform(platform) && checkForIntersection == true)
								{
									float ballXDiff = ballX - backupBallX;
									float ballYDiff = ballY - backupBallY;
									if (backupBallAngle > 180)
									{
										platform.setAngle(fixAngleToMoreThan180(platform.getAngle()));
									}
									while (isOnInfintiteLine(platform) && isWithinBoundsOfPlatform(platform))
									{
										if (platform.getAngle() > backupBallAngle)
										{
											ballX += Math.cos(Math.toRadians(platform.getAngle() - 90)) / 100.0;
											ballY += Math.sin(Math.toRadians(platform.getAngle() - 90)) / 100.0;
										} else
										{
											ballX += Math.cos(Math.toRadians(platform.getAngle() + 90)) / 100.0;
											ballY += Math.sin(Math.toRadians(platform.getAngle() + 90)) / 100.0;
										}
									}
									ballX -= ballXDiff;
									ballY -= ballYDiff;
									ballAngle = backupBallAngle;
									ballSpeed = backupBallSpeed;
									updateBallXAndYSpeed();
									// updateBallYSpeedBasedOnGravity();
									// updateBallAngle();
									// updateBallSpeed();
									// updateBallXAndYSpeed();
									rolling = true;
								} else
								{
									ballX = backupBallX;
									ballY = backupBallY;
									ballAngle = backupBallAngle;
									ballSpeed = backupBallSpeed;
									updateBallXAndYSpeed();
									rolling = false;
								}
							}
							if (Math.abs(Math.sin(Math.toRadians(ballAngle - platform.getAngle())) * ballSpeed) < gravitationalAcceleration)
							{
								platform.setAngle(fixAngleToLessThan180(platform.getAngle()));
								ballSpeed = (float) (Math.abs(Math.cos(Math.toRadians(ballAngle - platform.getAngle())) * ballSpeed));
								if (ballAngle > 180)
								{
									ballAngle = platform.getAngle() + 180;
								} else
								{
									ballAngle = platform.getAngle();
								}
							}
						} else
						{
							rolling = true;
							updateBallAngleBasedOnOnePlatform(platform.getAngle());
							updateBallXAndYSpeed();
						}
						if (ballSpeed > ballRadius * 2)
						{
							drawBallX = (float) ((minusIntersectX(platform) + plusIntersectX(platform)) / 2.0);// + Math.cos(Math.toRadians(platform.getAngle() + 90) * ballRadius * Math.signum(ballXSpeed)));
							drawBallY = (float) ((minusIntersectY(platform) + plusIntersectY(platform)) / 2.0);// + Math.sin(Math.toRadians(platform.getAngle() + 90) * ballRadius * Math.signum(ballYSpeed)));
							// try
							// {
							// Thread.sleep(250);
							// } catch (InterruptedException e)
							// {
							// e.printStackTrace();
							// }
						}
					}
				} else
				{
					updateBallYSpeedBasedOnGravity();
				}
				HitPlats.clear();
				wasJustTouchingScreen = false;
			}
		} else if (mode == MODE_CREATE_PLATFORM)
		{
			if (touchingScreen == true)
			{
				drawLineToFinger(c);
			}
			if (stoppedTouching() && platformIsLongEnough())
			{
				addPlatformWhereFingerWasTouching();
				makeCurrentTouchXAndYAbstract();
			}
		}
		drawBall(c);
		drawAllPlatforms(c);
		drawBallX = -1000;
		drawBallY = -1000;
		invalidate();
	}

	private void simulateBall()
	{
		updateBallPosition();
		updateBallYSpeedBasedOnGravity();
		updateBallAngle();
		updateBallSpeed();
		updateTheoreticalRadius();
	}

	private void backtrackBallPosition()
	{
		ballX -= ballXSpeed;
		ballY -= ballYSpeed;
	}

	private void updateTheoreticalRadius()
	{
		theoreticalRadius = (float) (ballSpeed / 2.0);
		if (theoreticalRadius < ballRadius)
		{
			theoreticalRadius = ballRadius;
		}
	}

	private void updateSpeedWithBounceFactor()
	{
		ballSpeed *= bounceFactor;
	}

	private void updateColors()
	{
		platformPaint.setColor(Color.WHITE);
		ballPaint.setColor(ballColor);
	}

	private void drawBackground(Canvas c)
	{
		c.drawColor(Color.BLACK);
	}

	private void updateGravity()
	{
		gravitationalAcceleration = (float) ((this.getHeight() / 1000.0) * gAccelerationMultiplier);
	}

	private void drawBall(Canvas c)
	{
		// c.drawCircle(ballX, ballY, theoreticalRadius, ballPaint);
		if (drawBallX == -1000)
		{
			drawBallX = ballX;
		}
		if (drawBallY == -1000)
		{
			drawBallY = ballY;
		}
		c.drawCircle(drawBallX, drawBallY, ballRadius, ballPaint);
	}

	private void drawAllPlatforms(Canvas c)
	{
		for (int i = 0; i < platforms.size(); i++)
		{
			c.drawLine(platforms.get(i).getStartX(), platforms.get(i).getStartY(), platforms.get(i).getEndX(), platforms.get(i).getEndY(), platformPaint);
		}
	}

	private void makeCurrentTouchXAndYAbstract()
	{
		currentTouchX = -1000;
		currentTouchY = -1000;
	}

	private void addPlatformWhereFingerWasTouching()
	{
		platforms.add(new Platform(startTouchXMode1, startTouchYMode1, endTouchXMode1, endTouchYMode1));
	}

	private boolean platformIsLongEnough()
	{
		return Math.sqrt((endTouchYMode1 - startTouchYMode1) * (endTouchYMode1 - startTouchYMode1) + (endTouchXMode1 - startTouchXMode1) * (endTouchXMode1 - startTouchXMode1)) > this.getHeight() / 50.0;
		// return endTouchXMode1 != startTouchXMode1 || endTouchYMode1 != startTouchYMode1;
	}

	private boolean stoppedTouching()
	{
		return currentTouchX == endTouchXMode1 && currentTouchY == endTouchYMode1;
	}

	private void drawLineToFinger(Canvas c)
	{
		c.drawLine(startTouchXMode1, startTouchYMode1, currentTouchX, currentTouchY, platformPaint);
	}

	private boolean angleIsRightOfAnotherAngle(double compareAngle, double angleThatMayBeRight)
	{
		boolean isRight = false;
		if (compareAngle < 180)
		{
			if (angleThatMayBeRight < compareAngle)
			{
				isRight = true;
			}
		} else if (compareAngle > 180)
		{
			if (angleThatMayBeRight > compareAngle)
			{
				isRight = true;
			}
		}
		return isRight;
	}

	private boolean angleIsLeftOfAnotherAngle(double compareAngle, double angleThatMayBeLeft)
	{
		boolean isLeft = false;
		if (compareAngle < 180)
		{
			if (angleThatMayBeLeft > compareAngle)
			{
				isLeft = true;
			}
		} else if (compareAngle > 180)
		{
			if (angleThatMayBeLeft < compareAngle)
			{
				isLeft = true;
			}
		}
		return isLeft;
	}

	private void updateBallYSpeedBasedOnGravity()
	{
		ballYSpeed += gravitationalAcceleration;
	}

	private void updateBallAngleBasedOnOnePlatform(double angle)
	{
		ballAngle = (angle * 2) - ballAngle;
	}

	private void updateBallXAndYSpeed()
	{
		ballXSpeed = (float) (Math.cos(Math.toRadians(ballAngle)) * ballSpeed);
		ballYSpeed = (float) (Math.sin(Math.toRadians(ballAngle)) * ballSpeed);
	}

	private double flipAngle(double angle)
	{
		double newAngle = angle + 180;
		if (newAngle > 360)
		{
			newAngle -= 360;
		}
		return newAngle;
	}

	private void updateBallPosition()
	{
		ballX += ballXSpeed;
		ballY += ballYSpeed;
	}

	private void updateBallAngleBasedOnTwoPlatforms(Platform leftAngle, Platform rightAngle)
	{
		double oppoBallAngle = oppositeOfBallAngle();
		if (leftAngle == null)
		{
			rightAngle.setAngle(fixAngleToLessThan180(rightAngle.getAngle()));
			ballAngle = (rightAngle.getAngle() * 2) + ballAngle;
		} else if (rightAngle == null)
		{
			leftAngle.setAngle(fixAngleToLessThan180(leftAngle.getAngle()));
			ballAngle = (leftAngle.getAngle() * 2) + ballAngle;
		} else
		{
			leftAngle.setAngle(fixAngleToLessThan180(leftAngle.getAngle()));
			rightAngle.setAngle(fixAngleToLessThan180(rightAngle.getAngle()));
			double resultAngle = ((leftAngle.getAngle() + rightAngle.getAngle()) / 2.0);
			resultAngle = fixAngleToLessThan180(resultAngle);
			// ballAngle = (((leftAngle.getAngle() + rightAngle.getAngle()) / 2.0) * 2) + oppoBallAngle;
			ballAngle = (resultAngle * 2) + ballAngle;
		}
	}

	private Platform closestRightAngle(double compareAngle, ArrayList<Platform> angles)
	{
		Platform closestRightAngle = null;
		double rightMinDiff = 360;
		for (int rr = 0; rr < angles.size(); rr++)
		{
			if (compareAngle < 180)
			{
				if (compareAngle - angles.get(rr).getAngle() < rightMinDiff)
				{
					rightMinDiff = compareAngle - angles.get(rr).getAngle();
					closestRightAngle = angles.get(rr);
				}
			} else if (compareAngle > 180)
			{
				if (angles.get(rr).getAngle() - compareAngle < rightMinDiff)
				{
					rightMinDiff = angles.get(rr).getAngle() - compareAngle;
					closestRightAngle = angles.get(rr);
				}
			}
		}
		return closestRightAngle;
	}

	private Platform closestLeftAngle(double compareAngle, ArrayList<Platform> angles)
	{
		Platform closestLeftAngle = null;
		double leftMinDiff = 360;
		for (int ll = 0; ll < angles.size(); ll++)
		{
			if (compareAngle < 180)
			{
				if (angles.get(ll).getAngle() - compareAngle < leftMinDiff)
				{
					leftMinDiff = angles.get(ll).getAngle() - compareAngle;
					closestLeftAngle = angles.get(ll);
				}
			} else if (compareAngle > 180)
			{
				if (compareAngle - angles.get(ll).getAngle() < leftMinDiff)
				{
					leftMinDiff = compareAngle - angles.get(ll).getAngle();
					closestLeftAngle = angles.get(ll);
				}
			}
		}
		return closestLeftAngle;
	}

	private double oppositeOfBallAngle()
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
		if (isOnInfintiteLine(platform) && isWithinBoundsOfPlatform(platform))// ((underTheRadical >= 0 && (thisPlatAngle <= 45 || thisPlatAngle >= 135) && x >= lowX && x <= highX) || (underTheRadical >= 0 && (thisPlatAngle >= 45 || thisPlatAngle <= 135) && y >= lowY && y <= highY))
		{
			if (platform.getJustWasHit() == false)
			{
				platform.setJustWasHit(true);
				HitPlats.add(new Platform(platform.getStartX(), platform.getStartY(), platform.getEndX(), platform.getEndY()));
			}
		} else
		{
			platform.setJustWasHit(false);
		}
	}

	private boolean isWithinBoundsOfPlatform(Platform platform)
	{
		return ((plusIntersectX(platform) >= platform.getLowX() || minusIntersectX(platform) >= platform.getLowX()) && (plusIntersectX(platform) <= platform.getHighX() || minusIntersectX(platform) <= platform.getHighX())) && ((plusIntersectY(platform) >= platform.getLowY() || minusIntersectY(platform) >= platform.getLowY()) && (plusIntersectY(platform) <= platform.getHighY() || minusIntersectY(platform) <= platform.getHighY()));
	}

	// private void doIntersectionCalculations(Platform platform)
	// {
	// double relativeX1;
	// double relativeX2;
	// double relativeY1;
	// double relativeY2;
	// double dx;
	// double dy;
	// double dr;
	// double D;
	// relativeX1 = platform.getStartX() - ballX;
	// relativeX2 = platform.getEndX() - ballX;
	// relativeY1 = platform.getStartY() - ballY;
	// relativeY2 = platform.getEndY() - ballY;
	// dx = relativeX2 - relativeX1;
	// dy = relativeY2 - relativeY1;
	// dr = Math.sqrt((dy * dy) + (dx * dx));
	// D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
	// underTheRadical = ((theoreticalRadius * theoreticalRadius) * (dr * dr)) - (D * D);
	// }
	private boolean isOnInfintiteLine(Platform platform)
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
		double underTheRadical = ((theoreticalRadius * theoreticalRadius) * (dr * dr)) - (D * D);
		return underTheRadical >= 0;
	}

	// private void checkIfBallIsTooFast()
	// {
	// if (ballSpeed > ballRadius * 2)
	// {
	// ballSpeed = (float) (ballRadius * 2);
	// updateBallXAndYSpeed();
	// }
	// }
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
				ballAngle = 270;
			}
		} else
		{
			ballAngle = Math.toDegrees(Math.atan(ballYSpeed / ballXSpeed));
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
		if (TouchXMode0.get(1) == null || TouchXMode0.get(0) == null)
		{
			ballXSpeed = 0;
		} else
		{
			ballXSpeed = (float) ((TouchXMode0.get(0) - TouchXMode0.get(1)));
		}
		if (TouchYMode0.get(1) == null || TouchYMode0.get(0) == null)
		{
			ballYSpeed = 0;
		} else
		{
			ballYSpeed = (float) ((TouchYMode0.get(0) - TouchYMode0.get(1)));
		}
	}

	private void setUpEssentials()
	{
		ballRadius = (float) (this.getHeight() / 75.0);
		ballX = (float) (this.getWidth() / 2.0);
		ballY = ballRadius;
		startBallX = (float) (this.getWidth() / 2.0);
		updateGravity();
		theoreticalRadius = ballRadius;
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
			for (int i = 0; i < touchMemory; i++)
			{
				TouchXMode0.set(i, null);
				TouchYMode0.set(i, null);
			}
			if (mode == MODE_CREATE_PLATFORM)
			{
				endTouchXMode1 = event.getX();
				endTouchYMode1 = event.getY();
			}
		}
		return true;
	}

	public double plusIntersectX(Platform platform)
	{
		double plusIntersectX = 0;
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
		plusIntersectX = ((D * dy + dx * Math.sqrt(theoreticalRadius * theoreticalRadius * dr * dr - D * D)) / (dr * dr));
		return plusIntersectX + ballX;
	}

	public double minusIntersectX(Platform platform)
	{
		double minusIntersectX = 0;
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
		minusIntersectX = ((D * dy - dx * Math.sqrt(theoreticalRadius * theoreticalRadius * dr * dr - D * D)) / (dr * dr));
		return minusIntersectX + ballX;
	}

	public double plusIntersectY(Platform platform)
	{
		double plusIntersectY = 0;
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
		plusIntersectY = ((-D * dx + Math.abs(dy) * Math.sqrt(theoreticalRadius * theoreticalRadius * dr * dr - D * D)) / (dr * dr));
		return plusIntersectY + ballY;
	}

	public double minusIntersectY(Platform platform)
	{
		double minusIntersectY = 0;
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
		minusIntersectY = ((-D * dx - Math.abs(dy) * Math.sqrt(theoreticalRadius * theoreticalRadius * dr * dr - D * D)) / (dr * dr));
		return minusIntersectY + ballY;
	}

	private double fixAngleToLessThan180(double angle)
	{
		while (angle > 180)
		{
			angle -= 180;
		}
		while (angle < 0)
		{
			angle += 180;
		}
		return angle;
	}

	private double fixAngleToMoreThan180(double angle)
	{
		while (angle > 360)
		{
			angle -= 180;
		}
		while (angle < 180)
		{
			angle += 180;
		}
		return angle;
	}

	private double fixAngle(double angle)
	{
		while (angle > 360)
		{
			angle -= 360;
		}
		while (angle < 0)
		{
			angle += 360;
		}
		return angle;
	}
}
