package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;

public class Configuration
{
	String name;
	float startBallX;
	float startBallY;
	float startBallXSpeed;
	float startBallYSpeed;
	ArrayList<PlatformDef> platforms;

	public Configuration(String name, float startBallX, float startBallY, float startBallXSpeed, float startBallYSpeed, ArrayList<PlatformDef> platforms)
	{
		this.name = name;
		this.startBallX = startBallX;
		this.startBallY = startBallY;
		this.startBallXSpeed = startBallXSpeed;
		this.startBallYSpeed = startBallYSpeed;
		this.platforms = platforms;
	}

	public String getName()
	{
		return name;
	}

	public float getStartBallX()
	{
		return startBallX;
	}

	public float getStartBallY()
	{
		return startBallY;
	}

	public float getStartBallXSpeed()
	{
		return startBallXSpeed;
	}

	public float getStartBallYSpeed()
	{
		return startBallYSpeed;
	}

	public ArrayList<PlatformDef> getPlatforms()
	{
		return platforms;
	}
}
