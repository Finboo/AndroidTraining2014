/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igatessample_color_animation;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{

	private int RED = 0xFFFF0000;
	private int BLUE = 0xff0000ff;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View container = findViewById(R.id.container);

		ValueAnimator alphaAnim = ObjectAnimator.ofFloat(container, "alpha", 0.0f, 0.9f);
		alphaAnim.setDuration(5000);
		alphaAnim.setEvaluator(new FloatEvaluator());
		alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
		alphaAnim.setRepeatMode(ValueAnimator.REVERSE);
		alphaAnim.start();

		ValueAnimator colorAnim = ObjectAnimator.ofInt(container, "backgroundColor", BLUE, RED);
		colorAnim.setDuration(5000);
		colorAnim.setEvaluator(new ArgbEvaluator());
		colorAnim.setRepeatCount(ValueAnimator.INFINITE);
		colorAnim.setRepeatMode(ValueAnimator.REVERSE);
		colorAnim.start();
	}
}