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

import android.widget.ImageView;

public class BubbleTrash extends BubbleBase {

	private ImageView imageViewForeground;

	protected BubbleTrash() {
		super();
		ImageView imageViewBackground = new ImageView(BubblesManager.getManager().getContext());
		imageViewBackground.setImageResource(R.drawable.bubble_trash_background);
		super.setImageView(imageViewBackground);
		imageViewForeground = new ImageView(BubblesManager.getManager().getContext());
		imageViewForeground.setImageResource(R.drawable.bubble_trash_foreground);
		super.getFrameLayout().addView(imageViewForeground);
	}

	protected ImageView getImageViewForeground() {
		return imageViewForeground;
	}
}
