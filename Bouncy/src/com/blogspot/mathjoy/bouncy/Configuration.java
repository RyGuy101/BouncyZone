package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;

public class Configuration
{
	String name;
	float startBallX;
	float startBallY;
	float startBallXSpeed;
	float startBallYSpeed;
	ArrayList<Platform> platforms;

	public Configuration(String name, float startBallX, float startBallY, float startBallXSpeed, float startBallYSpeed, ArrayList<Platform> platforms)
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

	public ArrayList<Platform> getPlatforms()
	{
		return platforms;
	}
}
