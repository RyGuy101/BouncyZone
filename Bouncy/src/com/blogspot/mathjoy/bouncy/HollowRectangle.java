package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class HollowRectangle extends Shape
{
	private BodyType bt;
	private float x;
	private float y;
	private float width;
	private float height;
	private float density;
	private float friction;
	private float restitution;
	private Body body;
	private FixtureDef fd;
	private BodyDef bd;
	private Fixture fixture;
	private boolean failed = false;

	public HollowRectangle(BodyType bt, float x, float y, float width, float height, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
		Vec2[] vs = { new Vec2((float) (-width / 2.0), (float) (-height / 2.0)), new Vec2((float) (width / 2.0), (float) (-height / 2.0)), new Vec2((float) (width / 2.0), (float) (height / 2.0)), new Vec2((float) (-width / 2.0), (float) (height / 2.0)) };
		try
		{
			cs.createLoop(vs, 4);
			fd = new FixtureDef();
			fd.shape = cs;
			fd.density = density;
			fd.friction = friction;
			fd.restitution = restitution;
			body = WorldManager.world.createBody(bd);
			fixture = body.createFixture(fd);
		} catch (Exception e)
		{
			failed = true;
		}
	}

	public boolean hasFailed()
	{
		return failed;
	}

	@Override
	public void destroy()
	{
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
	}

	public float getLeft()
	{
		return (float) (x - width / 2.0);
	}

	public float getRight()
	{
		return (float) (x + width / 2.0);
	}

	public float getTop()
	{
		return (float) (y - height / 2.0);
	}

	public float getBottom()
	{
		return (float) (y + height / 2.0);
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
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
