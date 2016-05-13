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

package com.rodrigopontes.androidbubbles;

public class BubblesProperties {

	// Internal parameters. Do not change!
	final static byte MODE_STATIC_POSITION = 0;
	final static byte MODE_PARAMS_POSITION = 1;
	final static byte MODE_TRASH_POSITION = 2;
	final static byte MODE_TOUCH_POSITION = 3;
	final static byte AXIS_X = 0;
	final static byte AXIS_Y = 1;

	// Customizable fields
	final static byte BUBBLE_ENTER_Y_POSITION = 8;  // Fraction of screen height bubble will enter.
	final static short BUBBLE_ENTER_SPEED = 200;
	final static float BUBBLE_EDGE_OFFSET_LEFT = 0.2f; // Percentage of bubble that will remain off-screen
	final static float BUBBLE_EDGE_OFFSET_RIGHT = 0.8f; // Percentage of bubble that will remain on-screen (should be 1 minus above value)
	final static float BUBBLE_SHRINK_SIZE = 0.8f; // Percentage of size after shrink

	final static short TRASH_ENTER_SPEED = 200;
	final static float TRASH_INCREASE_SIZE = 1.2f; // Percentage of size after increase
	final static short TRASH_VIBRATION_DURATION = 50;

	final static float SPRING_ANIMATION_RESISTANCE = 2f;
	final static byte SPRING_ANIMATION_LONG_DURATION = 12; // Use roots of spring function in BubblesManager
	final static float SPRING_ANIMATION_SHORT_DURATION = 2.5f; // Use roots of spring function in BubblesManager

	final static short SCALE_ANIMATION_DURATION = 100;

	final static float FLING_DECELERATION_FACTOR = 0.9f;

	final static byte EXIT_ANIMATION_SPEED = 1;
}
