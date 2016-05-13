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

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class BubbleBase {

	private FrameLayout frameLayout;
	private ImageView imageView;

	protected BubbleBase() {
		frameLayout = new FrameLayout(BubblesManager.getManager().getContext());
	}

	protected void setImageView(ImageView imageView) {
		this.imageView = imageView;
		this.imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		frameLayout.addView(this.imageView);
	}

	public FrameLayout getFrameLayout() {
		return frameLayout;
	}

	public ImageView getImageView() {
		return imageView;
	}
}
