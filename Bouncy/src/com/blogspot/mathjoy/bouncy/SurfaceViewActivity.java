package com.blogspot.mathjoy.bouncy;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class SurfaceViewActivity extends Activity implements OnTouchListener
{
	MySurfaceView v;
	ArrayList<Float> touchX = new ArrayList<Float>();
	ArrayList<Float> touchY = new ArrayList<Float>();
	int lastPos = 2;
	float x = 25;
	float y = 25;
	// float px;
	// float py;
	float xSpeed = 5;
	float ySpeed = 0;
	// float touchXSpeed;
	// float touchYSpeed;
	float acceleration = (float) 1.5;
	boolean touchedBall = false;
	int width;
	int height;
	public static final int MODE_BALL = 0;
	public static final int MODE_PLATFORM = 1;
	public static final int MODE_GRAB = 2;
	public int mode = MODE_BALL;
	public ArrayList<Rect> buttons = new ArrayList<Rect>();
	public ArrayList<Boolean> isPressed = new ArrayList<Boolean>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		for (int i = 0; i <= 2; i++)
		{
			isPressed.add(false);
		}
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		v = new MySurfaceView(this);
		// Button button = new Button(this);
		setContentView(v);
		// addContentView(button, new ViewGroup.LayoutParams(100, 75));
		// button.set
		v.setOnTouchListener(this);
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		v.resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		try
		{
			Thread.sleep(20);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float tempX = event.getX();
		float tempY = event.getY();
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if (tempX > 0 && tempX < buttons.get(0).right && tempY > 0 && tempY < buttons.get(0).bottom)
			{
				isPressed.set(0, true);
			} else if (tempX > 0 && tempX < buttons.get(1).right && tempY > 0 && tempY < buttons.get(1).bottom)
			{
				isPressed.set(1, true);
			} else if (tempX > 0 && tempX < buttons.get(2).right && tempY > 0 && tempY < buttons.get(2).bottom)
			{
				isPressed.set(2, true);
			} else if (mode == MODE_BALL)
			{
				touchedBall = true;
				for (int i = 0; i < lastPos + 1; i++)
				{
					touchX.set(i, tempX);
					touchY.set(i, tempY);
				}
			} else if (mode == MODE_PLATFORM)
			{

			} else if (mode == MODE_GRAB)
			{

			}
			break;

		case MotionEvent.ACTION_UP:
			if (tempX > 0 && tempX < buttons.get(0).right && tempY > 0 && tempY < buttons.get(0).bottom && isPressed.get(0) == true)
			{
				mode = 0;
			} else if (tempX > 0 && tempX < buttons.get(1).right && tempY > 0 && tempY < buttons.get(1).bottom && isPressed.get(1) == true)
			{
				mode = 1;
			} else if (tempX > 0 && tempX < buttons.get(2).right && tempY > 0 && tempY < buttons.get(2).bottom && isPressed.get(2) == true)
			{
				mode = 2;
			}
			isPressed.set(0, false);
			isPressed.set(1, false);
			isPressed.set(2, false);
			touchedBall = false;
			break;
		}
		touchX.remove(0);
		touchY.remove(0);
		touchX.add(tempX);
		touchY.add(tempY);
		// touched = true;
		// x.remove(0);
		// y.remove(0);
		// x.add(event.getX());
		// y.add(event.getY());
		// xSpeed = x.get(lastPos) - x.get(lastPos - 1);
		// ySpeed = y.get(lastPos) - y.get(lastPos - 1);
		// for (int i = 0; i < lastPos + 1; i++)
		// {
		// x.set(i, event.getX());
		// y.set(i, event.getY());
		// }
		// ySpeed = 0;
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_MOVE:
		// try {
		// Thread.sleep(20);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// touched = true;
		// x.remove(0);
		// y.remove(0);
		// x.add(event.getX());
		// y.add(event.getY());
		// xSpeed = x.get(lastPos) - x.get(0);
		// ySpeed = y.get(lastPos) - x.get(0);
		// break;

		// case MotionEvent.ACTION_:
		// touched = true;
		// x.remove(0);
		// y.remove(0);
		// x.add(event.getX());
		// y.add(event.getY());
		// ySpeed = 0;
		// break;
		// }
		return true;
	}

	public class MySurfaceView extends SurfaceView implements Runnable
	{
		Paint paint = new Paint();
		SurfaceHolder sh;
		Thread t = null;
		boolean isOK = false;

		public MySurfaceView(Context context)
		{
			super(context);
			// TODO Auto-generated constructor stub
			sh = getHolder();
			t = new Thread(this);
			t.start();
			for (int i = 0; i < lastPos + 1; i++)
			{
				touchX.add((float) (this.getWidth() / 2.0));
				touchY.add((float) (this.getHeight() / 2.0));
			}
			width = this.getWidth();
			height = this.getHeight();
		}

		public MySurfaceView(Context context, AttributeSet attrs)
		{
			super(context, attrs);
			// TODO Auto-generated constructor stub
			sh = getHolder();
			t = new Thread(this);
			t.start();
			for (int i = 0; i < lastPos + 1; i++)
			{
				touchX.add((float) (this.getWidth() / 2.0));
				touchY.add((float) (this.getHeight() / 2.0));
			}
			width = this.getWidth();
			height = this.getHeight();
		}

		public MySurfaceView(Context context, AttributeSet attrs, int defStyle)
		{
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			sh = getHolder();
			t = new Thread(this);
			t.start();
			for (int i = 0; i < lastPos + 1; i++)
			{
				touchX.add((float) (this.getWidth() / 2.0));
				touchY.add((float) (this.getHeight() / 2.0));
			}
			width = this.getWidth();
			height = this.getHeight();
		}

		public void pause()
		{
			isOK = false;
			while (true)
			{
				try
				{
					t.join();
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume()
		{
			isOK = true;
			t = new Thread(this);
			t.start();
		}

		// public float getMyX()
		// {
		// return x;
		// }
		//
		// public float getMyY()
		// {
		// return y;
		// }
		//
		// public void setMyX(float newX)
		// {
		// x = newX;
		// }
		//
		// public void setMyY(float newY)
		// {
		// y = newY;
		// }
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while (isOK)
			{
				try
				{
					Thread.sleep(20);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!sh.getSurface().isValid())
					continue;

				Canvas c = sh.lockCanvas();
				refreshButtons();
				if (touchedBall == false)
				{
					checkForWalls();
				} else
				{
					updateFingerPosition();
				}
				if (xSpeed > 75)
				{
					xSpeed = 75;
				}
				if (ySpeed > 75)
				{
					ySpeed = 75;
				}
				if (xSpeed < -75)
				{
					xSpeed = -75;
				}
				if (ySpeed < -75)
				{
					ySpeed = -75;
				}
				x += xSpeed;
				y += ySpeed;
				c.drawColor(Color.BLACK);
				paint.setColor(Color.RED);
				c.drawCircle(x, y, 25, paint);
				paint.setColor(Color.WHITE);
				if (isPressed.get(0) == true || mode == 0)
				{
					paint.setColor(Color.GRAY);
				}
				c.drawRect(buttons.get(0), paint);
				paint.setColor(Color.WHITE);
				if (isPressed.get(1) == true || mode == 1)
				{
					paint.setColor(Color.GRAY);
				}
				c.drawRect(buttons.get(1), paint);
				paint.setColor(Color.WHITE);
				if (isPressed.get(2) == true || mode == 2)
				{
					paint.setColor(Color.GRAY);
				}
				c.drawRect(buttons.get(2), paint);
				paint.setColor(Color.BLACK);
				c.drawLine(buttons.get(0).right, 0, buttons.get(0).right, buttons.get(0).bottom, paint);
				c.drawLine(buttons.get(1).right, 0, buttons.get(1).right, buttons.get(1).bottom, paint);
				sh.unlockCanvasAndPost(c);
			}

		}

		private void refreshButtons()
		{
			buttons.clear();
			buttons.add(new Rect(0, 0, (int) (this.getWidth() / 3.0), 75));
			buttons.add(new Rect((int) (this.getWidth() / 3.0), 0, (int) (this.getWidth() / 3.0 * 2), 75));
			buttons.add(new Rect((int) (this.getWidth() / 3.0 * 2), 0, (int) (this.getWidth() / 3.0 * 3), 75));
		}

		private void updateFingerPosition()
		{
			x = touchX.get(lastPos);
			y = touchY.get(lastPos);
			xSpeed = touchX.get(lastPos) - touchX.get(lastPos - 1);
			ySpeed = touchY.get(lastPos) - touchY.get(lastPos - 1);
		}

		private void checkForWalls()
		{
			if (hitsLeftWall())
			{
				goRight();
			} else if (hitsRightWall())
			{
				goLeft();
			}
			if (hitsTopWall())
			{
				goDown();
			} else if (hitsBottomWall())
			{
				goUp();
			} else
			{
				applyGravity();
			}
		}

		private void goRight()
		{
			xSpeed = Math.abs(xSpeed);
		}

		private void goLeft()
		{
			xSpeed = Math.abs(xSpeed) * -1;
		}

		private void goDown()
		{
			ySpeed = Math.abs(ySpeed);
		}

		private void goUp()
		{
			ySpeed = Math.abs(ySpeed) * -1;
		}

		private void applyGravity()
		{
			ySpeed += acceleration;
		}

		private boolean hitsBottomWall()
		{
			return y > this.getHeight() - 25;
		}

		private boolean hitsTopWall()
		{
			return y < 25;
		}

		private boolean hitsLeftWall()
		{
			return x < 25;
		}

		private boolean hitsRightWall()
		{
			return x > this.getWidth() - 25 && xSpeed > 0;
		}

	}
}