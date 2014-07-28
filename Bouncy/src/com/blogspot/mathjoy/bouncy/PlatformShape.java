package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PlatformShape
{
	private float startX;
	private float startY;
	private float width;
	private float height;

	private float density;
	private float friction;
	private float restitution;
	Body body;

	public PlatformShape(BodyType bt, float startX, float startY, float endX, float endY, float width, float height, float density, float friction, float restitution)
	{
		this.startX = startX;
		this.startY = startY;
		this.startX = endX;
		this.startY = endY;
		this.width = width;
		this.height = height;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;

		BodyDef bd = new BodyDef();
		float x = endX - startX / 2.0f;
		float y = endY - startY / 2.0f;
		float angle;
		if (endX - startX == 0)
		{
			angle = 90.0f;
		} else
		{
			angle = (float) Math.toDegrees(Math.atan((endY - startY) / (endX - startX)));
		}
		bd.position.set(x, y);
		bd.type = bt;
		PolygonShape ps = new PolygonShape();
		Vec2[] vertices = { new Vec2(startX - x, startY - y), new Vec2(startX + x, startY + y) };
		ps.set(vertices, vertices.length / 2);
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
