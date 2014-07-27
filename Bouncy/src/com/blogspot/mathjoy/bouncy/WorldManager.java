package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class WorldManager
{
	public static World world;
	private static float timeStep = 1.0f / 60.f;
	private static int velocityIterations = 6;
	private static int positionIterations = 3;

	public static void setupWorld(Vec2 gravity)
	{
		world = new World(gravity);
		world.setAllowSleep(true);
	}

	public static void step()
	{
		world.step(timeStep, velocityIterations, positionIterations);
	}

	public static void setGravity(Vec2 gravity)
	{
		world.setGravity(gravity);
	}

}
