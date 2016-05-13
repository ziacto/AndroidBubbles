/*
 Copyright 2016 Rodrigo Deleu Lopes Pontes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.rodrigopontes.androidbubblesexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rodrigopontes.androidbubbles.Bubble;
import com.rodrigopontes.androidbubbles.BubbleOnTapListener;
import com.rodrigopontes.androidbubbles.BubblesManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, ScreenOrientationService.class));
	}

	public void onAddBubble(View view) {
		Bubble bubble = new Bubble(123);
		bubble.setBubbleOnTapListener(new BubbleOnTapListener() {
			@Override
			public void onTap() {

			}
		});
		BubblesManager.getManager().addBubble(new Bubble(R.drawable.example_bubble));
	}
}
