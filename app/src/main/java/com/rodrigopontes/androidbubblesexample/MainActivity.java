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
import com.rodrigopontes.androidbubbles.BubblesManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// Action for "Create Bubbles Manager" button
	public void onCreateBubblesManager(View view) {
		// Creates a service that wraps the creation of Bubbles Manager.
		// This is a good approach if you want to keep track of screen orientation changes
		// while your app is in the background.
		startService(new Intent(this, ScreenOrientationService.class));
	}

	// Action for "Add Bubble" button
	public void onAddBubble(View view) {
		BubblesManager.getManager().addBubble(new Bubble(R.drawable.example_bubble));
	}
}
