package com.blogspot.mathjoy.bouncy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SaveConfActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_conf);
	}

	public void goToMainSettings(View v)
	{
		onBackPressed();
	}
}
