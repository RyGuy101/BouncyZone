package com.blogspot.mathjoy.bouncy;

public class Platform
{
	private int myStartX;
	private int myStartY;
	private int myEndX;
	private int myEndY;
	private boolean myJustWasHit;

	public Platform(int startX, int startY, int endX, int endY)
	{
		myStartX = startX;
		myStartY = startY;
		myEndX = endX;
		myEndY = endY;
	}

	public int getStartX()
	{
		return myStartX;
	}

	public int getStartY()
	{
		return myStartY;
	}

	public int getEndX()
	{
		return myEndX;
	}

	public int getEndY()
	{
		return myEndY;
	}

	public void setStartX(int startX)
	{
		myStartX = startX;
	}

	public void setStartY(int startY)
	{
		myStartY = startY;
	}

	public void setEndX(int endX)
	{
		myEndX = endX;
	}

	public void setEndY(int endY)
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
