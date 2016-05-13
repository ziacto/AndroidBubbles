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

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class Bubble extends BubbleBase {

	private BubbleOnTapListener bubbleOnClickListener;

	/**
	 * Create Bubble with given ImageView
	 */
	public Bubble(ImageView imageView) {
		super();
		super.setImageView(imageView);
		bubbleOnClickListener = null;
	}

	/**
	 * Convenience constructor to create Bubble with Bitmap
	 */
	public Bubble(Bitmap imageBitmap) {
		super();
		ImageView imageView = new ImageView(BubblesManager.getManager().getContext());
		imageView.setImageBitmap(imageBitmap);
		super.setImageView(imageView);
		bubbleOnClickListener = null;
	}

	/**
	 * Convenience constructor to create Bubble with Drawable
	 */
	public Bubble(Drawable drawable) {
		super();
		ImageView imageView = new ImageView(BubblesManager.getManager().getContext());
		imageView.setImageDrawable(drawable);
		super.setImageView(imageView);
		bubbleOnClickListener = null;
	}

	/**
	 * Convenience constructor to create Bubble with Resource ID
	 */
	public Bubble(int resId) {
		super();
		ImageView imageView = new ImageView(BubblesManager.getManager().getContext());
		imageView.setImageResource(resId);
		super.setImageView(imageView);
		bubbleOnClickListener = null;
	}

	/**
	 * Convenience constructor to create Bubble with URI
	 */
	public Bubble(Uri uri) {
		super();
		ImageView imageView = new ImageView(BubblesManager.getManager().getContext());
		imageView.setImageURI(uri);
		super.setImageView(imageView);
		bubbleOnClickListener = null;
	}

	/**
	 * Sets Bubble's ImageView size
	 */
	public void setImageViewSize(int width, int height) {
		super.getImageView().getLayoutParams().width = width;
		super.getImageView().getLayoutParams().height = height;
	}

	/**
	 * Sets Bubble's OnTapListener
	 */
	public void setBubbleOnTapListener(BubbleOnTapListener bubbleOnClickListener) {
		this.bubbleOnClickListener = bubbleOnClickListener;
	}

	protected BubbleOnTapListener getBubbleOnClickListener() {
		return bubbleOnClickListener;
	}
}
