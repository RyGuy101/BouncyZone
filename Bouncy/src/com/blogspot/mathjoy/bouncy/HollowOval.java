package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class HollowOval extends Shape
{
	private BodyType bt;
	private float x;
	private float y;
	private double xRadius;
	private double yRadius;
	private float density;
	private float friction;
	private float restitution;
	private Body body;
	private FixtureDef fd;
	private BodyDef bd;
	private Fixture fixture;

	public HollowOval(BodyType bt, float x, float y, float xDiameter, float yDiameter, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.xRadius = xDiameter / 2.0;
		this.yRadius = yDiameter / 2.0;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		create();
	}

	@Override
	public void create()
	{
		bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = bt;
		ChainShape cs = new ChainShape();
		Vec2[] vs = new Vec2[360];
		int i = 0;
		for (double rad = 0; rad < 2 * Math.PI; rad += (2 * Math.PI) / vs.length)
		{
			if (i < vs.length)
			{
				vs[i] = new Vec2((float) (Math.cos(rad) * xRadius), (float) (Math.sin(rad) * yRadius));
				i++;
			}
		}
		cs.createLoop(vs, vs.length);
		fd = new FixtureDef();
		fd.shape = cs;
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

	public float getLeft()
	{
		return (float) (x - xRadius);
	}

	public float getRight()
	{
		return (float) (x + xRadius);
	}

	public float getTop()
	{
		return (float) (y - yRadius);
	}

	public float getBottom()
	{
		return (float) (y + yRadius);
	}
}