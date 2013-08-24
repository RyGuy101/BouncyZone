package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// OLD MAINACTIVITY

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button ball = (Button) this.findViewById(R.id.Ball);
		ball.setOnClickListener(this);
		Button grab = (Button) this.findViewById(R.id.Grab);
		grab.setOnClickListener(this);
		Button platform = (Button) this.findViewById(R.id.Platform);
		ball.setOnClickListener(this);
		Button delete = (Button) this.findViewById(R.id.Delete);
		delete.setOnClickListener(this);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
	}
}
