# AndroidBubbles
Android bubbles recreates the chat bubbles as implemented by Facebook. It focuses on smooth animations for a smiliar user experience, and extremely easy implementation.

![](https://github.com/RodrigoDLPontes/AndroidBubbles/blob/master/demo.gif?raw=true)

(Higher quality version can be found [here](https://www.youtube.com/watch?v=E2966AjH6ew))

## Usage

You can start using Android Bubbles in 3 simple steps.

### 1. Add project dependency

First, add Android Bubbles to your project dependencies.

Make sure you have jcenter as one of your repositories...
```groovy
repositories {
  ...
  jcenter()
}
```
...and add Android Bubbles to your dependencies.
```groovy
dependencies {
  ...
  compile 'com.rodrigopontes:android-bubbles:0.1.0'
}
```

### 2. Create a BubblesManager

Create a BubblesManager using a Context.
```java
public class MainActivity extends Activity {

  BubblesManager bubblesManager;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    
    bubblesManager = BubblesManager.create(this);
  }
}
```
After you create it, you can also retrieve it with
```java
bubblesManager = BubblesManager.getManager();
```

### 3. Add Bubble

Add a bubble to BubblesManager
```java
ImageView imageView = new ImageView(this);
imageView.setImageResource(R.drawable.my_image);
Bubble bubble = new Bubble(imageView);
bubblesManager.addBubble(bubble);
```
You now have your first Android Bubble!

## Other methods

### Handle Bubble taps

You can handle Bubble taps just as you would with a Button.

```java
bubble.setBubbleOnTapListener(new BubbleOnTapListener {

  @Override
  public void onTap(Bubble.BubblePosition bubblePosition) {
    Log.d("Bubbles", "Hello World!");
  }
}
```

The Bubble's position can be retrieved from

```java
bubblePosition.x
bubblePosition.y
```

You can also implement
```java
public void onTapConfirmed()
public void onDoubleTap()
```

### Remove Bubble

You can remove Bubbles programatically with

```java
bubblesManager.removeBubble(bubble);
```
### Create Bubbles easily

You can save a few lines of code by using Bubble's convenience contructors.
```java
new Bubble(imageBitmap);
new Bubble(drawable);
new Bubble(resourceId);
new Bubble(imageUri);
```

### Set Bubble's image size

You can customize the size of the Bubble by using
```java
bubble.setImageViewSize(width, height);
```

### Check if BubblesManager exists

To avoid problems with activity recreations, you can wrap your BubblesManager creation with

```java
if(BubblesManager.exists()) {
  bubblesManager = BubblesManager.getManager();
} else {
  bubblesManager = BubblesManeger.create(this);
}
```

### Handle screen rotations

Screen rotations must be handled by the Activity that implements BubblesManager. That is made simple with

```java
bubblesManager.updateConfiguration();
```

## Highly customizable

You can easily customize Android Bubble's feel by tweaking the fields in BubblesProperties.

## Share your project!

Implemented Android Bubbles for your project? Send it to rodrigo.dl.pontes@gmail.com and I'll share it here!

## License

Copyright 2016 Rodrigo Pontes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
