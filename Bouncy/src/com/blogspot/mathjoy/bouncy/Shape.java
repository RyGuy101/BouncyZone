package com.blogspot.mathjoy.bouncy;

public abstract class Shape
{
	private boolean real = false;

	public void create()
	{
		if (!real)
		{
			doCreate();
			real = true;
		}
	}

	public void destroy()
	{
		doDestroy();
		real = false;
	}

	protected abstract void doCreate();

	protected abstract void doDestroy();
}
