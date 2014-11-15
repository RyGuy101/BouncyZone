package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class HollowCircle
{
	private BodyType bt;
	private float x;
	private float y;
	private float radius;
	private float density;
	private float friction;
	private float restitution;
	private Body body;
	private FixtureDef fd;
	private BodyDef bd;
	private Fixture fixture;

	public HollowCircle(BodyType bt, float x, float y, float radius, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		create();
	}

	public void create()
	{
		
	}
}
