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

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

public class BubblesManager {

	private static BubblesManager instance;
	private ArrayList<BubbleController> bubblesList;
	private Context context;
	private WindowManager windowManager;
	private static short screenWidth;
	private static short screenHeight;
	private static short statusBarHeight;
	private BubbleTrash bubbleTrash;
	private WindowManager.LayoutParams bubbleTrashParams;
	private static short bubbleTrashWidth;
	private static short bubbleTrashHeight;
	private AsyncTask trashEnterInXAnimation;
	private AsyncTask trashEnterInYAnimation;
	private AsyncTask trashExitAnimation;
	private AsyncTask trashFollowXMovement;
	private AsyncTask trashFollowYMovement;
	private boolean trashOnScreen;

	/**
	 * Creates BubblesManager with given Context
	 */
	public static BubblesManager create(Context context) {
		if(instance == null) {
			instance = new BubblesManager(context);
			instance.initialize();
			instance.bubblesList = new ArrayList<>();
			return instance;
		} else {
			throw new BubblesManagerNotNullException();
		}
	}

	/**
	 * Returns created BubblesManager
	 */
	public static BubblesManager getManager() {
		if(instance != null) {
			return instance;
		} else {
			throw new NullPointerException("BubblesManager has not yet been created! Use .create() first.");
		}
	}

	private BubblesManager(Context context) {
		this.context = context;
	}

	// Initializes BubbleManager

	private void initialize() {
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		windowManager.getDefaultDisplay().getSize(point);
		screenWidth = (short)point.x;
		screenHeight = (short)point.y;
		int statusBarResId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		statusBarHeight = (short)context.getResources().getDimensionPixelSize(statusBarResId);

		bubbleTrash = new BubbleTrash();
		bubbleTrashParams = new WindowManager.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
						WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				PixelFormat.TRANSLUCENT);
		bubbleTrashParams.gravity = Gravity.TOP | Gravity.START;
		bubbleTrash.getFrameLayout().measure(screenWidth, screenHeight);
		bubbleTrashWidth = (short)(bubbleTrash.getFrameLayout().getMeasuredWidth() * BubblesProperties.TRASH_INCREASE_SIZE);
		bubbleTrashHeight = (short)(bubbleTrash.getFrameLayout().getMeasuredHeight() * BubblesProperties.TRASH_INCREASE_SIZE);
		bubbleTrash.getFrameLayout().setMinimumWidth(bubbleTrashWidth);
		bubbleTrash.getFrameLayout().setMinimumHeight(bubbleTrashHeight);
		bubbleTrash.getImageView().measure(screenWidth, screenHeight);
		bubbleTrash.getImageView().setX(bubbleTrashWidth / 2 - bubbleTrash.getImageView().getMeasuredWidth() / 2);
		bubbleTrash.getImageView().setY(bubbleTrashHeight / 2 - bubbleTrash.getImageView().getMeasuredHeight() / 2);
		bubbleTrash.getImageViewForeground().measure(screenWidth, screenHeight);
		bubbleTrash.getImageViewForeground().setX(bubbleTrashWidth / 2 - bubbleTrash.getImageViewForeground().getMeasuredWidth() / 2);
		bubbleTrash.getImageViewForeground().setY(bubbleTrashHeight / 2 - bubbleTrash.getImageViewForeground().getMeasuredHeight() / 2);
		bubbleTrashParams.x = (screenWidth - bubbleTrashWidth) / 2;
		bubbleTrashParams.y = screenHeight + BubblesProperties.TRASH_ENTER_SPEED;
		windowManager.addView(bubbleTrash.getFrameLayout(), bubbleTrashParams);
	}

	/**
	 * Checks whether BubblesManager has been created
	 */
	public static boolean exists() {
		return instance != null;
	}

	/**
	 * Adds new Bubble to BubblesManager
	 */
	public void addBubble(Bubble bubble) {
		if(windowManager != null) {
			WindowManager.LayoutParams params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
					PixelFormat.TRANSLUCENT);
			params.gravity = Gravity.TOP | Gravity.START;
			params.y = screenHeight / BubblesProperties.BUBBLE_ENTER_Y_POSITION;
			BubbleController bubbleController = new BubbleController(bubble, params);
			bubble.getFrameLayout().setOnTouchListener(bubbleController.new BubbleTouchListener());
			bubblesList.add(bubbleController);
			windowManager.addView(bubble.getFrameLayout(), params);
			bubbleController.createdAnimation = new SpringAnimation(
					new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, screenWidth + BubblesProperties.BUBBLE_ENTER_SPEED),
					new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, screenWidth - (int)(bubbleController.bubbleWidth * BubblesProperties.BUBBLE_EDGE_OFFSET_RIGHT)),
					params,
					BubblesProperties.AXIS_X,
					BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
					bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			throw new NullPointerException("BubblesManager has not yet been created! Use .create() first.");
		}
	}

	/**
	 * Removes Bubble from BubblesManager
	 */
	public void removeBubble(BubbleController bubbleController) {
		if(windowManager != null) {
			bubblesList.remove(bubbleController);
			windowManager.removeViewImmediate(bubbleController.bubble.getFrameLayout());
		} else {
			throw new NullPointerException("BubblesManager has not yet been created! Use .create() first.");
		}
	}

	/**
	 * Updates BubblesManager for orientation change
	 */
	public void updateConfiguration() {
		int oldScreenWidth = screenWidth;
		int oldScreenHeight = screenHeight;
		cancelAsyncTasks(trashEnterInXAnimation, trashEnterInYAnimation,
				trashExitAnimation, trashFollowXMovement, trashFollowYMovement);
		windowManager.removeViewImmediate(bubbleTrash.getFrameLayout());
		for(BubbleController bubbleController : bubblesList) {
			cancelAsyncTasks(bubbleController.createdAnimation,
					bubbleController.edgeAnimation,
					bubbleController.flingAnimation,
					bubbleController.bubbleEnteringTrashInXAnimation,
					bubbleController.bubbleEnteringTrashInYAnimation,
					bubbleController.bubbleExitingTrashInXAnimation,
					bubbleController.bubbleExitingTrashInYAnimation);
			windowManager.removeViewImmediate(bubbleController.bubble.getFrameLayout());
		}
		initialize();
		for(BubbleController bubbleController : bubblesList) {
			BubbleController newBubbleController = new BubbleController(bubbleController.bubble, bubbleController.params);
			bubbleController.params.y = (int)(((float)bubbleController.params.y / (float)oldScreenHeight) * (float)screenHeight);
			if(bubbleController.params.x < oldScreenWidth / 2) {
				bubbleController.params.x = (int)(-newBubbleController.bubbleWidth * BubblesProperties.BUBBLE_EDGE_OFFSET_LEFT);
			} else {
				bubbleController.params.x = (int)(screenWidth - newBubbleController.bubbleWidth * BubblesProperties.BUBBLE_EDGE_OFFSET_RIGHT);
			}
			newBubbleController.bubble.getFrameLayout().setOnTouchListener(newBubbleController.new BubbleTouchListener());
			bubblesList.set(bubblesList.indexOf(bubbleController), newBubbleController);
			windowManager.addView(newBubbleController.bubble.getFrameLayout(), bubbleController.params);
		}
	}

	// Helper methods

	protected Context getContext() {
		return context;
	}

	private void playScaleAnimation(ScaleAnimation scaleAnimation, int duration, BubbleBase bubble) {
		scaleAnimation.setDuration(duration);
		scaleAnimation.setFillAfter(true);
		bubble.getImageView().startAnimation(scaleAnimation);
	}

	private short getCenter(int paramValue, int size) {
		return (short)(paramValue + size / 2);
	}

	private void cancelAsyncTasks(AsyncTask... animations) {
		for(AsyncTask animation : animations) {
			if(animation != null && !animation.getStatus().name().equals("FINISHED")) {
				animation.cancel(true);
			}
		}
	}

	private boolean haveAsyncTasksFinished(AsyncTask... animations) {
		for(AsyncTask animation : animations) {
			if(!(animation == null || !animation.getStatus().name().equals("RUNNING"))) {
				return false;
			}
		}
		return true;
	}

	// Controls all bubble behaviours

	private class BubbleController {

		private Bubble bubble;
		private short bubbleWidth;
		private short bubbleHeight;
		private short leftEdgeLimit;
		private short rightEdgeLimit;
		private short bottomEdgeLimit;
		private WindowManager.LayoutParams params;
		private GestureDetectorCompat bubbleGestureDetector;
		private AsyncTask createdAnimation;
		private AsyncTask edgeAnimation;
		private AsyncTask flingAnimation;
		private AsyncTask bubbleEnteringTrashInXAnimation;
		private AsyncTask bubbleEnteringTrashInYAnimation;
		private AsyncTask bubbleExitingTrashInXAnimation;
		private AsyncTask bubbleExitingTrashInYAnimation;
		private boolean inTrash;

		private BubbleController(Bubble bubble, WindowManager.LayoutParams params) {
			this.bubble = bubble;
			bubble.getFrameLayout().measure(screenWidth, screenHeight);
			bubbleWidth = (short)bubble.getFrameLayout().getMeasuredWidth();
			bubbleHeight = (short)bubble.getFrameLayout().getMeasuredHeight();
			leftEdgeLimit = (short)(-bubbleWidth * BubblesProperties.BUBBLE_EDGE_OFFSET_LEFT);
			rightEdgeLimit = (short)(screenWidth - bubbleWidth * BubblesProperties.BUBBLE_EDGE_OFFSET_RIGHT);
			bottomEdgeLimit = (short)(screenHeight - statusBarHeight - bubbleHeight);
			this.params = params;
			bubbleGestureDetector = new GestureDetectorCompat(context, new BubbleGestureListener());
		}

		class BubbleTouchListener implements View.OnTouchListener {

			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, final MotionEvent event) {
				bubbleGestureDetector.onTouchEvent(event);
				switch(event.getAction()) {

					/*
						ACTION DOWN
					 */

					case MotionEvent.ACTION_DOWN:
						// Initializes bubble movement and shrinks bubble icon
						cancelAsyncTasks(createdAnimation, edgeAnimation, flingAnimation);
						initialX = params.x;
						initialY = params.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						playScaleAnimation(new ScaleAnimation(1, BubblesProperties.BUBBLE_SHRINK_SIZE,
										1, BubblesProperties.BUBBLE_SHRINK_SIZE,
										bubbleWidth / 2, bubbleHeight / 2),
								BubblesProperties.SCALE_ANIMATION_DURATION,
								bubble);
						return true;

					/*
						ACTION CANCEL & UP
					 */

					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						if(!inTrash) {
							// If bubble not inside trash, resize it back to normal, move it to screen edge, and hide trash
							playScaleAnimation(new ScaleAnimation(BubblesProperties.BUBBLE_SHRINK_SIZE, 1,
											BubblesProperties.BUBBLE_SHRINK_SIZE, 1,
											bubbleWidth / 2, bubbleHeight / 2),
									BubblesProperties.SCALE_ANIMATION_DURATION,
									bubble);
							if(getCenter(params.x, bubbleWidth) < screenWidth / 2) {
								edgeAnimation = new SpringAnimation(
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, params.x),
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, leftEdgeLimit),
										params,
										BubblesProperties.AXIS_X,
										BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
										bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							} else {
								edgeAnimation = new SpringAnimation(
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, params.x),
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, rightEdgeLimit),
										params,
										BubblesProperties.AXIS_X,
										BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
										bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							}
							if(trashOnScreen) {
								trashOnScreen = false;
								cancelAsyncTasks(trashEnterInXAnimation, trashEnterInYAnimation, trashFollowXMovement, trashFollowYMovement);
								trashExitAnimation = new ExitAnimation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							}
						} else {
							// If bubble inside trash, hide both and delete bubble
							trashOnScreen = false;
							cancelAsyncTasks(trashEnterInXAnimation, trashEnterInYAnimation, bubbleEnteringTrashInXAnimation,
									bubbleEnteringTrashInYAnimation, flingAnimation, trashFollowXMovement, trashFollowYMovement);
							trashExitAnimation = new ExitAnimation() {
								@Override
								protected void onPostExecute(Void aVoid) {
									super.onPostExecute(aVoid);
									playScaleAnimation(new ScaleAnimation(BubblesProperties.TRASH_INCREASE_SIZE, 1,
													BubblesProperties.TRASH_INCREASE_SIZE, 1,
													bubbleTrashWidth / 2, bubbleTrashHeight / 2),
											BubblesProperties.SCALE_ANIMATION_DURATION,
											bubbleTrash);
									bubbleTrashParams.y = screenHeight + BubblesProperties.TRASH_ENTER_SPEED;
									windowManager.updateViewLayout(bubbleTrash.getFrameLayout(), bubbleTrashParams);
								}
							}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							new ExitAnimation(params, bubble){

								@Override
								protected void onPostExecute(Void aVoid) {
									super.onPostExecute(aVoid);
									removeBubble(BubbleController.this);
								}
							}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
						return true;

					/*
						ACTION MOVE
					 */

					case MotionEvent.ACTION_MOVE:
						// Minimum threshold to consider movement
						if(Math.abs(event.getRawX() - initialTouchX) > 10 || Math.abs(event.getRawY() - initialTouchY) > 10) {
							// Display trash
							if(!trashOnScreen) {
								trashOnScreen = true;
								cancelAsyncTasks(trashExitAnimation);
								trashEnterInXAnimation = new SpringAnimation(
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, bubbleTrashParams.x),
										new AnimationPosition(BubblesProperties.MODE_TRASH_POSITION, params, BubblesProperties.AXIS_X, bubbleWidth),
										bubbleTrashParams,
										BubblesProperties.AXIS_X,
										BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
										bubbleTrash).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
								trashEnterInYAnimation = new SpringAnimation(
										new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, bubbleTrashParams.y),
										new AnimationPosition(BubblesProperties.MODE_TRASH_POSITION, params, BubblesProperties.AXIS_Y),
										bubbleTrashParams,
										BubblesProperties.AXIS_Y,
										BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
										bubbleTrash).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							}
							if(!inTrash) {
								//Bubble is not inside trash
								if(haveAsyncTasksFinished(bubbleExitingTrashInYAnimation)) {
									// Move trash alongside the bubble
									if(haveAsyncTasksFinished(trashEnterInYAnimation, bubbleEnteringTrashInYAnimation, trashFollowYMovement, trashExitAnimation)) {
										bubbleTrashParams.x = screenWidth / 2 - bubbleTrashWidth / 2 +
												((params.x + bubbleWidth / 2) - screenWidth / 2) / 10;
										bubbleTrashParams.y = (int)(screenHeight * 0.725f) + params.y / 10;
										windowManager.updateViewLayout(bubbleTrash.getFrameLayout(), bubbleTrashParams);
									}
									// Bubble movement
									params.x = initialX + (int)(event.getRawX() - initialTouchX);
									params.y = initialY + (int)(event.getRawY() - initialTouchY);
									// Limit movement to screen edges
									if(params.x < leftEdgeLimit) {
										params.x = leftEdgeLimit;
									} else if(params.x > rightEdgeLimit) {
										params.x = rightEdgeLimit;
									}
									if(params.y < 0) {
										params.y = 0;
									} else if(params.y > bottomEdgeLimit) {
										params.y = bottomEdgeLimit;
									}
									windowManager.updateViewLayout(bubble.getFrameLayout(), params);
									// Check if bubble has entered trash bounds
									if(Math.abs(getCenter(params.y, bubbleHeight) - getCenter(bubbleTrashParams.y, bubbleTrashHeight)) < bubbleHeight * 2) {
										if(!inTrash) {
											// Send bubble to trash
											inTrash = true;
											((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(BubblesProperties.TRASH_VIBRATION_DURATION);
											playScaleAnimation(new ScaleAnimation(1, BubblesProperties.TRASH_INCREASE_SIZE,
															1, BubblesProperties.TRASH_INCREASE_SIZE,
															bubbleTrashWidth / 2, bubbleTrashHeight / 2),
													BubblesProperties.SCALE_ANIMATION_DURATION,
													bubbleTrash);
											cancelAsyncTasks(trashEnterInXAnimation, trashEnterInYAnimation, trashFollowXMovement, trashFollowYMovement);
											bubbleEnteringTrashInXAnimation = new SpringAnimation(
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, params.x),
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, getCenter(bubbleTrashParams.x, bubbleTrashWidth) - bubbleWidth / 2),
													params,
													BubblesProperties.AXIS_X,
													BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
													bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
											bubbleEnteringTrashInYAnimation = new SpringAnimation(
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, params.y),
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, getCenter(bubbleTrashParams.y, bubbleHeight) - bubbleHeight / 2),
													params,
													BubblesProperties.AXIS_Y,
													BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
													bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
										}
									}
								}
							} else {
								// Bubble is inside trash, and user has moved finger away from trash
								if(!(Math.abs(event.getRawY() - getCenter(bubbleTrashParams.y, bubbleTrashHeight)) < bubbleHeight * 2.1f)) {
									// Remove bubble from trash
									inTrash = false;
									playScaleAnimation(new ScaleAnimation(BubblesProperties.TRASH_INCREASE_SIZE, 1,
													BubblesProperties.TRASH_INCREASE_SIZE, 1,
													bubbleWidth / 2, bubbleHeight / 2),
											BubblesProperties.SCALE_ANIMATION_DURATION,
											bubbleTrash);
									cancelAsyncTasks(bubbleEnteringTrashInXAnimation, bubbleEnteringTrashInYAnimation);
									bubbleExitingTrashInXAnimation = new SpringAnimation(
											new AnimationPosition(BubblesProperties.MODE_PARAMS_POSITION, params, BubblesProperties.AXIS_X),
											new AnimationPosition(BubblesProperties.MODE_TOUCH_POSITION, event, initialX, initialTouchX, BubblesProperties.AXIS_X),
											params,
											BubblesProperties.AXIS_X,
											BubblesProperties.SPRING_ANIMATION_SHORT_DURATION,
											bubble).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
									bubbleExitingTrashInYAnimation = new SpringAnimation(
											new AnimationPosition(BubblesProperties.MODE_PARAMS_POSITION, params, BubblesProperties.AXIS_Y),
											new AnimationPosition(BubblesProperties.MODE_TOUCH_POSITION, event, initialY, initialTouchY, BubblesProperties.AXIS_Y),
											params,
											BubblesProperties.AXIS_Y,
											BubblesProperties.SPRING_ANIMATION_SHORT_DURATION,
											bubble) {

										@Override
										protected void onPostExecute(Void aVoid) {
											trashFollowXMovement = new SpringAnimation(
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, bubbleTrashParams.x),
													new AnimationPosition(BubblesProperties.MODE_TRASH_POSITION, params, BubblesProperties.AXIS_X, bubbleWidth),
													bubbleTrashParams,
													BubblesProperties.AXIS_X,
													BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
													bubbleTrash).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
											trashFollowYMovement = new SpringAnimation(
													new AnimationPosition(BubblesProperties.MODE_STATIC_POSITION, bubbleTrashParams.y),
													new AnimationPosition(BubblesProperties.MODE_TRASH_POSITION, params, BubblesProperties.AXIS_Y),
													bubbleTrashParams,
													BubblesProperties.AXIS_Y,
													BubblesProperties.SPRING_ANIMATION_LONG_DURATION,
													bubbleTrash).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
										}
									}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
								}
							}
						}
						return true;
				}
				return false;
			}
		}

		class BubbleGestureListener extends GestureDetector.SimpleOnGestureListener {

			// Fling inertia
			@Override
			public boolean onFling(final MotionEvent e1, final MotionEvent e2, float velocityX, float velocityY) {
				flingAnimation = new AsyncTask<Void, Integer, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						float yAcceleration = e2.getY() - e1.getY();
						while(Math.abs(yAcceleration) > 1) {
							publishProgress((int)yAcceleration);
							yAcceleration *= BubblesProperties.FLING_DECELERATION_FACTOR;
							try {
								Thread.sleep(10);
							} catch(InterruptedException e) {
								e.printStackTrace();
							}
						}
						return null;
					}

					@Override
					protected void onProgressUpdate(Integer... values) {
						params.y += values[0];
						if(params.y < 0) {
							params.y = 0;
						} else if(params.y > bottomEdgeLimit) {
							params.y = bottomEdgeLimit;
						}
						windowManager.updateViewLayout(bubble.getFrameLayout(), params);
					}
				}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				return super.onFling(e1, e2, velocityX, velocityY);
			}

			// Bubble has been tapped
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				if(bubble.getBubbleOnClickListener() != null) {
					bubble.getBubbleOnClickListener().onTap();
				}
				return super.onSingleTapUp(e);
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if(bubble.getBubbleOnClickListener() != null) {
					bubble.getBubbleOnClickListener().onTapConfirmed();
				}
				return super.onSingleTapConfirmed(e);
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				if(bubble.getBubbleOnClickListener() != null) {
					bubble.getBubbleOnClickListener().onDoubleTap();
				}
				return super.onDoubleTap(e);
			}
		}
	}

	// Wrapper for positions to use in animations
	private class AnimationPosition {

		private byte mode;
		private int value;
		private WindowManager.LayoutParams params;
		private byte axis;
		private int bubbleWidth;
		private MotionEvent event;
		private int initialPos;
		private float initialTouchPos;

		// Regular static position (use mode MODE_STATIC_POSITION)
		protected AnimationPosition(byte mode, int position) {
			this.mode = mode;
			value = position;
		}

		// Dynamic position based on params (use mode MODE_PARAMS_POSITION)
		protected AnimationPosition(byte mode, WindowManager.LayoutParams params, byte axis) {
			this.mode = mode;
			this.params = params;
			this.axis = axis;
		}

		// Dynamic position based on params with offset for use with trash (use mode MODE_TRASH_POSITION)
		protected AnimationPosition(byte mode, WindowManager.LayoutParams params, byte axis, int bubbleWidth) {
			this.mode = mode;
			this.params = params;
			this.bubbleWidth = bubbleWidth;
			this.axis = axis;
		}

		// Dynamic position based on touch (use mode MODE_TOUCH_POSITION)
		protected AnimationPosition(byte mode, MotionEvent event, int initialPos, float initialTouchPos, byte axis) {
			this.mode = mode;
			this.event = event;
			this.initialPos = initialPos;
			this.initialTouchPos = initialTouchPos;
			this.axis = axis;
		}

		protected int get() {
			switch(mode) {
				case 0: // Regular static position
					return value;
				case 1: // Dynamic position based on params
					if(axis == 0) {
						return params.x;
					} else {
						return params.y;
					}
				case 2: // Dynamic position based on params with offset for use with trash
					if(axis == 0) {
						return screenWidth / 2 - bubbleTrashWidth / 2 + ((params.x + bubbleWidth / 2) - screenWidth / 2) / 10;
					} else {
						return (int)(screenHeight * 0.725f) + params.y / 10;
					}
				case 3: // Dynamic position based on touch
					if(axis == 0) {
						return initialPos + (int)(event.getRawX() - initialTouchPos);
					} else {
						return initialPos + (int)(event.getRawY() - initialTouchPos);
					}
			}
			// Should never get here
			return -1;
		}
	}

	//Defines spring animation
	private class SpringAnimation extends AsyncTask<Void, Integer, Void> {

		private AnimationPosition initialPosition;
		private AnimationPosition finalPosition;
		private WindowManager.LayoutParams animParams;
		private byte axis;
		private float duration;
		private BubbleBase bubble;

		protected SpringAnimation(AnimationPosition initialPosition,
		                          AnimationPosition finalPosition,
		                          WindowManager.LayoutParams animParams,
		                          byte axis, float duration, BubbleBase bubble) {
			this.initialPosition = initialPosition;
			this.finalPosition = finalPosition;
			this.animParams = animParams;
			this.axis = axis;
			this.duration = duration;
			this.bubble = bubble;
		}

		@Override
		protected Void doInBackground(Void... params) {
			for(float i = 1 ; i < duration ; i += 0.1f) {
				/*
					Equation that defines spring animation

					      cos(x - 1)
					b + (------------ * (a - b))
					         x^2

					Where x is time, a is initial position and b is final position
				 */
				publishProgress((int)(finalPosition.get() +
						((Math.cos(i - 1) / Math.pow(i, BubblesProperties.SPRING_ANIMATION_RESISTANCE)) *
								(initialPosition.get() - finalPosition.get()))));
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					return null;
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(axis == 0) {
				animParams.x = values[0];
			} else {
				animParams.y = values[0];
			}
			windowManager.updateViewLayout(bubble.getFrameLayout(), animParams);
		}
	}

	// Simple animation for hiding bubbles
	private class ExitAnimation extends AsyncTask<Void, Integer, Void> {

		Bubble bubble;
		WindowManager.LayoutParams params;

		protected ExitAnimation() {
			params = bubbleTrashParams;
		}

		protected ExitAnimation(WindowManager.LayoutParams params, Bubble bubble) {
			this.params = params;
			this.bubble = bubble;
		}

		@Override
		protected Void doInBackground(Void... args) {
			int initialY;
			if(bubble != null) {
				initialY = params.y;
			} else {
				initialY = bubbleTrashParams.y;
			}
			for(int i = 1 ; params.y < screenHeight ; i += BubblesProperties.EXIT_ANIMATION_SPEED) {
				if(isCancelled()) return null;
				publishProgress((int)(initialY + i * i));
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					return null;
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(bubble != null) {
				params.y = values[0];
				windowManager.updateViewLayout(bubble.getFrameLayout(), params);
			} else {
				bubbleTrashParams.y = values[0];
				windowManager.updateViewLayout(bubbleTrash.getFrameLayout(), bubbleTrashParams);
			}
		}
	}

	private static class BubblesManagerNotNullException extends RuntimeException {

		public BubblesManagerNotNullException() {
			super("BubblesManager already exists! Use .getManager() instead.");
		}
	}
}