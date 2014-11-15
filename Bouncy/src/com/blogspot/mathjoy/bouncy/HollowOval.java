package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class HollowOval
{
	private BodyType bt;
	private float x;
	private float y;
	private float xRadius;
	private float yRadius;
	private float density;
	private float friction;
	private float restitution;
	private Body body;
	private FixtureDef fd;
	private BodyDef bd;
	private Fixture fixture;

	public HollowOval(BodyType bt, float x, float y, float xRadius, float yRadius, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.xRadius = xRadius;
		this.yRadius = yRadius;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		create();
	}

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
}
