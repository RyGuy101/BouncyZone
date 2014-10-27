package com.blogspot.mathjoy.bouncy;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class Circle
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
	private Vec2 lastPos;
	private Vec2 lastVel;
	private float lastAngle;
	private float lastAVel;

	public Circle(BodyType bt, float x, float y, float radius, float density, float friction, float restitution)
	{
		this.bt = bt;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		initialCreate();
	}

	public void reCreate()
	{
		bd.position.set(lastPos);
		bd.linearVelocity.set(lastVel);
		bd.angle = lastAngle;
		bd.angularVelocity = lastAVel;
		body = WorldManager.world.createBody(bd);
		fixture = body.createFixture(fd);
		MyView.makeBounceOnstart = false;
	}

	public void initialCreate()
	{
		bd = new BodyDef();
		bd.position.set(x, y);
		bd.type = bt;
		CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		fd = new FixtureDef();
		fd.shape = cs;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		body = WorldManager.world.createBody(bd);
		fixture = body.createFixture(fd);
	}

	public void destroy()
	{
		lastPos = new Vec2(getPosition());
		lastVel = getVelocity();
		lastAngle = getAngle();
		lastAVel = body.getAngularVelocity();
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

	public Vec2 getPosition()
	{
		return body.getPosition();
	}

	public Vec2 getVelocity()
	{
		return body.getLinearVelocity();
	}

	public float getRadius()
	{
		return radius;
	}

	public float getAngle()
	{
		return body.getAngle();
	}

	public void setRestitution(float restitution)
	{
		Vec2 pos = new Vec2(getPosition());
		Vec2 vel = getVelocity();
		float aVel = body.getAngularVelocity();
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
		bd.position.set(pos);
		bd.linearVelocity.set(vel);
		bd.angularVelocity = aVel;
		body = WorldManager.world.createBody(bd);
		fd.restitution = restitution;
		body.createFixture(fd);
		MyView.makeBounceOnstart = false;
	}

	public void setFriction(float friction)
	{
		Vec2 pos = new Vec2(getPosition());
		Vec2 vel = getVelocity();
		float aVel = body.getAngularVelocity();
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
		bd.position.set(pos);
		bd.linearVelocity.set(vel);
		bd.angularVelocity = aVel;
		body = WorldManager.world.createBody(bd);
		fd.friction = friction;
		body.createFixture(fd);
		MyView.makeBounceOnstart = false;
	}

	public void setVelocity(Vec2 velocity)
	{
		body.setLinearVelocity(velocity);
	}

	public void setAngle(float angle)
	{
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
		bd.angle = angle;
		body = WorldManager.world.createBody(bd);
		body.createFixture(fd);
	}

	public void setAngularVelocity(float w)
	{
		body.setAngularVelocity(w);
	}

	public void setPosition(Vec2 position)
	{
		body.destroyFixture(fixture);
		WorldManager.world.destroyBody(body);
		bd.position.set(position);
		body = WorldManager.world.createBody(bd);
		body.createFixture(fd);
	}

	public boolean isAwake()
	{
		return body.isAwake();
	}

	public Body getBody()
	{
		return body;
	}
}
