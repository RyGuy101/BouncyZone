package com.blogspot.mathjoy.bouncy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View
{
	Paint backgroundPaint = new Paint();
	Paint ballPaint = new Paint();
	Paint lineInBallPaint = new Paint();
	public int ballColor;

	public ColorView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		updateSize();
	}

	public ColorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		updateSize();
	}

	public ColorView(Context context)
	{
		super(context);
		updateSize();
	}

	@Override
	protected void onDraw(Canvas c)
	{
		super.onDraw(c);
		updateSize();
		backgroundPaint.setColor(Color.BLACK);
		ballPaint.setColor(ballColor);
		lineInBallPaint.setColor(Color.BLACK);
		lineInBallPaint.setAlpha(79);
		lineInBallPaint.setStrokeWidth(MyView.toPixels(0.04f));
		c.drawRect(0, 0, MyView.toPixels(MyView.ball.getRadius() * 2 + 0.1f), MyView.toPixels(MyView.ball.getRadius() * 2 + 0.1f), backgroundPaint);
		c.drawCircle(MyView.toPixels((MyView.ball.getRadius() * 2 + 0.1f) / 2.0f), MyView.toPixels((MyView.ball.getRadius() * 2 + 0.1f) / 2.0f), MyView.toPixels(MyView.ball.getRadius()), ballPaint);
		c.drawLine(0.05f, MyView.toPixels((MyView.ball.getRadius() * 2 + 0.1f) / 2.0f), MyView.toPixels(MyView.ball.getRadius() * 2 + 0.05f), MyView.toPixels((MyView.ball.getRadius() * 2 + 0.1f) / 2.0f), lineInBallPaint);
		//		try
		//		{
		//			Thread.sleep(20);
		//		} catch (InterruptedException e)
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		invalidate();
	}

	public void updateSize()
	{
		setMeasuredDimension((int) (MyView.toPixels(MyView.ball.getRadius() * 2 + 0.1f)), (int) MyView.toPixels(MyView.ball.getRadius() * 2 + 0.1f));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		updateSize();
	}

}
