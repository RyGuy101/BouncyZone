package com.blogspot.mathjoy.bouncy;

import junit.framework.TestCase;

public class MyViewTest extends TestCase
{
	public void testIntersectX() throws Exception
	{
		PlatformDef platform2 = new PlatformDef(0, 0, 10, 10);
		assertEquals(5.0, new MathBrain().intersectX(platform2, 0, 0, 5 * Math.sqrt(2)));
	}

	public void testIntersectY() throws Exception
	{
		PlatformDef platform2 = new PlatformDef(0, 0, 10, 10);
		assertEquals(5.0, new MathBrain().intersectY(platform2, 0, 0, 5 * Math.sqrt(2)));
	}
}
