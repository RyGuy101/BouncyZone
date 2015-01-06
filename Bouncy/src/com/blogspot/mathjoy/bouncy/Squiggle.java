package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import android.graphics.Path;

public class Squiggle extends Shape
{
	private BodyType bt;
	private float x;
	private float y;
	private Vec2[] verts;
	private float density;
	private float friction;
	private float restitution;
	private Body body;
	private FixtureDef fd;
	private BodyDef bd;
	private Fixture fixture;
	private boolean failed = false;
	private Path path = new Path();

	public Squiggle(BodyType bt, float x, float y, Vec2[] verts, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.verts = verts;
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
		if (verts.length < 5)
		{
			failed = true;
		}
		if (!failed)
		{
			try
			{
				cs.createChain(verts, verts.length);
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
		getPath().moveTo(MyView.toPixels(getX()), MyView.toPixels(getY()));
		for (int i = 0; i < getVertsLength(); i++)
		{
			getPath().lineTo(MyView.toPixels(getVert(i).x + getX()), MyView.toPixels(getVert(i).y + getY()));
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

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public Vec2 getVert(int index)
	{
		return verts[index];
	}

	public int getVertsLength()
	{
		return verts.length;
	}

	Path getPath()
	{
		return path;
	}
}
