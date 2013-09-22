package com.blogspot.mathjoy.bouncy;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// OLD MAINACTIVITY

public class MainActivity extends Activity
{
	MyView v;
	int theRealColor = Color.RED;
	Button ball;

	// Button grab;
	// Button platform;
	// Button delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		v = (MyView) findViewById(R.id.myView);
		ball = (Button) this.findViewById(R.id.Ball);
		// grab = (Button) this.findViewById(R.id.Grab);
		// platform = (Button) this.findViewById(R.id.Platform);
		// delete = (Button) this.findViewById(R.id.Delete);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		String thisPickedColor = intent.getStringExtra(MyMenu.NAME);
		if (thisPickedColor == "blue")
		{
			theRealColor = Color.BLUE;
		} else
		{
			theRealColor = Color.MAGENTA;
		}
		v.color = theRealColor;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void goToMenu(View v) {
		Intent intent = new Intent(this, MyMenu.class);
		startActivity(intent);
	}
	// public void setTheTag()
	// {
	// v.setTag(new Object());
	// }

}
