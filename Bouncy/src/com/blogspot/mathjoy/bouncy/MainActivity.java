package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// OLD MAINACTIVITY
public class MainActivity extends Activity// implements OnTouchListener
{
	Intent intent;
	String thisPickedColor;
	MyView v;
	static int theRealColor = Color.RED;
	static boolean justOpened = true;
	Button ball;
	// Button grab;
	// Button platform;
	// Button delete;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		v = (MyView) findViewById(R.id.myView);
		ball = (Button) findViewById(R.id.Ball);
		// grab = (Button) this.findViewById(R.id.Grab);
		// platform = (Button) this.findViewById(R.id.Platform);
		// delete = (Button) this.findViewById(R.id.Delete);
		if (justOpened)
		{
			justOpened = false;
			v.color = Color.RED;
			v.gravity = 1;
		} else
		{
			intent = getIntent();
			thisPickedColor = intent.getExtras().getString("selectedColor");
			if (thisPickedColor.equals("red"))
			{
				theRealColor = Color.RED;
			} else if (thisPickedColor.equals("orange"))
			{
				theRealColor = Color.rgb(225, 127, 0);
			} else if (thisPickedColor.equals("yellow"))
			{
				theRealColor = Color.YELLOW;
			} else if (thisPickedColor.equals("green"))
			{
				theRealColor = Color.GREEN;
			} else if (thisPickedColor.equals("blue"))
			{
				theRealColor = Color.BLUE;
			} else if (thisPickedColor.equals("purple"))
			{
				theRealColor = Color.rgb(191, 63, 255);
			} else if (thisPickedColor.equals("pink"))
			{
				theRealColor = Color.rgb(225, 63, 255);
			} else if (thisPickedColor.equals("brown"))
			{
				theRealColor = Color.rgb(127, 63, 15);
			} else if (thisPickedColor.equals("white"))
			{
				theRealColor = Color.WHITE;
			} else if (thisPickedColor.equals("gray"))
			{
				theRealColor = Color.GRAY;
			}
			v.color = theRealColor;
			v.gravity = intent.getExtras().getDouble("gravityValue");
		}
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void goToMenu(View v)
	{
		Intent intent = new Intent(this, MyMenu.class);
		startActivity(intent);
	}

	public void modeBall(View view)
	{
		v.mode = MyView.MODE_BALL;
	}
	// @Override
	// public boolean onTouch(View view, MotionEvent event)
	// {
	// // TODO Auto-generated method stub
	// v.setTouchX(event.getX());
	// v.setTouchY(event.getY());
	// if (event.getAction() == MotionEvent.ACTION_DOWN)
	// {
	// v.setTouching(true);
	// } else if (event.getAction() == MotionEvent.ACTION_UP)
	// {
	// v.setTouching(false);
	// }
	// return true;
	// }
}
