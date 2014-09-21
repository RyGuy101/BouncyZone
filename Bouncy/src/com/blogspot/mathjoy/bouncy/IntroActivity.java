package com.blogspot.mathjoy.bouncy;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends Activity
{
	public static SoundPool spoolBounce = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	public static int bounce;
	public static View buttons;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		bounce = spoolBounce.load(this, R.raw.bounce, 1);
		buttons = findViewById(R.id.introButtons);
	}
}
