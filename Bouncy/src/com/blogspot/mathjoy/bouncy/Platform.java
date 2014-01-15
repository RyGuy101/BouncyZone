package com.blogspot.mathjoy.bouncy;

public class Platform
{
	private float myStartX;
	private float myStartY;
	private float myEndX;
	private float myEndY;
	private float myLowX;
	private float myLowY;
	private float myHighX;
	private float myHighY;
	private boolean myJustWasHit;
	private double myAngle;

	public Platform(float startX, float startY, float endX, float endY)
	{
		myStartX = startX;
		myStartY = startY;
		myEndX = endX;
		myEndY = endY;
		myLowX = startX;
		myLowY = startY;
		myHighX = endX;
		myHighY = endY;
		if (endX < startX)
		{
			myHighX = startX;
			myLowX = endX;
		}
		if (endY < startY)
		{
			myHighY = startY;
			myLowY = endY;
		}
		if (myEndX - myStartX == 0)
		{
			myAngle = 90;
		} else
		{
			myAngle = Math.toDegrees(Math.atan((myEndY - myStartY) / (myEndX - myStartX)));
			if (myAngle < 0)
			{
				myAngle += 360;
			}
			if (myAngle >= 360)
			{
				myAngle -= 360;
			}
			if (myAngle > 180)
			{
				myAngle -= 180;
			}
		}
	}

	public float getStartX()
	{
		return myStartX;
	}

	public float getStartY()
	{
		return myStartY;
	}

	public float getEndX()
	{
		return myEndX;
	}

	public float getEndY()
	{
		return myEndY;
	}

	public float getLowX()
	{
		return myLowX;
	}

	public float getHighX()
	{
		return myHighX;
	}

	public float getLowY()
	{
		return myLowY;
	}

	public float getHighY()
	{
		return myHighY;
	}

	public void setStartX(float startX)
	{
		myStartX = startX;
	}

	public void setStartY(float startY)
	{
		myStartY = startY;
	}

	public void setEndX(float endX)
	{
		myEndX = endX;
	}

	public void setEndY(float endY)
	{
		myEndY = endY;
	}

	public double getAngle()
	{
		return myAngle;
	}

	public void setAngle(double angle)
	{
		myAngle = angle;
	}

	public boolean getJustWasHit()
	{
		return myJustWasHit;
	}

	public void setJustWasHit(boolean justWasHit)
	{
		myJustWasHit = justWasHit;
	}
}
