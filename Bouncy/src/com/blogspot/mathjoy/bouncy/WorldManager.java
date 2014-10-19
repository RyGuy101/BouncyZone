package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class WorldManager
{
	public static World world = null;
	private static float timeStep = 1.0f / 60.f;
	private static int velocityIterations = 6;
	private static int positionIterations = 3;
	private static Vec2 myGravity = new Vec2(0, 10.0f);

	public static void setupWorld()
	{
		world = new World(myGravity);
		world.setAllowSleep(true);
	}

	public static void step()
	{
		world.step(timeStep, velocityIterations, positionIterations);
	}

	public static void setGravity(Vec2 gravity)
	{
		myGravity = gravity;
		world.setGravity(myGravity);
	}

	public static void setGravityButDontUpdateWorld(Vec2 gravity)
	{
		myGravity = gravity;
	}

	public static void setGravityTemporarily(Vec2 gravity)
	{
		world.setGravity(gravity);
	}

	public static void undoTemporaryGravitySet()
	{
		world.setGravity(myGravity);
	}

}
