package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Rectangle
{
	private float x;
	private float y;
	private float width;
	private float height;

	private float density;
	private float friction;
	private float restitution;
	Body body;

	public Rectangle(BodyType bt, float x, float y, float width, float height, float density, float friction, float restitution)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;

		BodyDef bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = bt;
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width, height);
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		body = WorldManager.world.createBody(bd);
		body.createFixture(fd);
	}

	public float getX()
	{
		return body.getPosition().x;
	}

	public float getY()
	{
		return body.getPosition().y;
	}

	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}
}
