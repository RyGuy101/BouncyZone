package com.blogspot.mathjoy.bouncy;

public class MathBrain
{
	public double intersectX(PlatformDef platform, int ballX, int ballY, double ballRadius)
	{
		double intersectX = 0;
		double relativeX1;
		double relativeX2;
		double relativeY1;
		double relativeY2;
		double dx;
		double dy;
		double dr;
		double D;
		double r = ballRadius;
		relativeX1 = platform.getStartX() - ballX;
		relativeX2 = platform.getEndX() - ballX;
		relativeY1 = platform.getStartY() - ballY;
		relativeY2 = platform.getEndY() - ballY;
		dx = relativeX2 - relativeX1;
		dy = relativeY2 - relativeY1;
		dr = Math.sqrt((dy * dy) + (dx * dx));
		D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
		intersectX = ((D * dy + dx * Math.sqrt(r * r * dr * dr - D * D)) / (dr * dr));
		return intersectX;
	}

	public double intersectY(PlatformDef platform, int ballX, int ballY, double ballRadius)
	{
		double intersectY = 0;
		double relativeX1;
		double relativeX2;
		double relativeY1;
		double relativeY2;
		double dx;
		double dy;
		double dr;
		double D;
		double r = ballRadius;
		relativeX1 = platform.getStartX() - ballX;
		relativeX2 = platform.getEndX() - ballX;
		relativeY1 = platform.getStartY() - ballY;
		relativeY2 = platform.getEndY() - ballY;
		dx = relativeX2 - relativeX1;
		dy = relativeY2 - relativeY1;
		dr = Math.sqrt((dy * dy) + (dx * dx));
		D = (relativeX1 * relativeY2) - (relativeX2 * relativeY1);
		intersectY = ((-D * dx + Math.abs(dy) * Math.sqrt(r * r * dr * dr - D * D)) / (dr * dr));
		return intersectY;
	}
}
