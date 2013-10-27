package com.blogspot.mathjoy.bouncy;

public class Platform
{
	private float myStartX;
	private float myStartY;
	private float myEndX;
	private float myEndY;
	private boolean myJustWasHit;

	public Platform(float startX, float startY, float endX, float endY)
	{
		myStartX = startX;
		myStartY = startY;
		myEndX = endX;
		myEndY = endY;
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
		double angle;
		if (myEndX - myStartX == 0)
		{
			if (myEndY - myStartY > 0)
			{
				angle = 90;
			}
			if (myEndY - myStartY < 0)
			{
				angle = -90;
			}
		}
		angle = Math.toDegrees(Math.atan((myEndY - myStartY) / (myEndX - myStartX)));
//		if (angle < 0)
//		{
//			angle += 360;
//		}
		return angle;
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
