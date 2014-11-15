package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class Platform extends Shape
{
	private BodyType bt;
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private float density;
	private float friction;
	private float restitution;
	private Vec2[] vertices;
	private Body body;
	private Fixture fixture;

	public Platform(BodyType bt, float startX, float startY, float endX, float endY, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		create();
	}

	@Override
	public void create()
	{
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
		Vec2[] vertices = { new Vec2((float) (startX - x - (0.01 * Math.cos(Math.toRadians(angle + 90)))), (float) (startY - y - (0.01 * Math.sin(Math.toRadians(angle + 90))))), new Vec2((float) (startX - x + (0.01 * Math.cos(Math.toRadians(angle + 90)))), (float) (startY - y + (0.01 * Math.sin(Math.toRadians(angle + 90))))), new Vec2((float) (endX - x - (0.01 * Math.cos(Math.toRadians(angle + 90)))), (float) (endY - y - (0.01 * Math.sin(Math.toRadians(angle + 90))))),
				new Vec2((float) (endX - x + (0.01 * Math.cos(Math.toRadians(angle + 90)))), (float) (endY - y + (0.01 * Math.sin(Math.toRadians(angle + 90))))) };
		this.vertices = vertices;
		ps.set(vertices, vertices.length);
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		body = WorldManager.world.createBody(bd);
		fixture = body.createFixture(fd);
	}

	@Override
	public void destroy()
	{
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
	}

	public float getX()
	{
		return body.getPosition().x;
	}

	public float getY()
	{
		return body.getPosition().y;
	}

	public float getStartX()
	{
		return startX;
	}

	public float getStartY()
	{
		return startY;
	}

	public float getEndX()
	{
		return endX;
	}

	public float getEndY()
	{
		return endY;
	}

	public Vec2[] getVerticesVec2Arr()
	{
		return vertices;
	}

	public float[] getVerticesFloatArr()
	{
		float[] verts = { vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, vertices[2].x, vertices[2].y, vertices[3].x, vertices[3].y };
		return verts;
	}
}
