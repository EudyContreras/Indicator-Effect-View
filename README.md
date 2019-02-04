
# Ripple Effect Library

[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21s)
[![License: ISC](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/ISC)
[![](https://jitpack.io/v/EudyContreras/RippleEffect.svg)](https://jitpack.io/#EudyContreras/RippleEffect)
![Version](https://img.shields.io/github/release/EudyContreras/RippleEffect.svg?style=flat)
![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)


#### This library allows you to display a ripple effect as an overlay to any view. The ripple effect is fully customizeable and supports a variety of options.

![Ripple Effect Image][RippleImage]

[RippleImage]: https://github.com/EudyContreras/RippleEffect/blob/EudyContreras-readme/Ripple.png


## About:

In code **APIs** offered by **Rippple View**.

The ripple effect has a high number of apis that give full control of how the ripple is shown to the user. 

#### In Code Code methods:
|APIs | Description|
|---|---|
|**startRippleAnimation()**| No input|
|**startRippleAnimation(int startDelay)**| The delay before the ripple starts|
|**stopRippleAnimation(int duration)**| Stop the ripple animation|
|**stopRippleAnimation()**| Stop the ripple animation|
|**setUpWith(ViewGroup parent)**| Sets a parent root to the ripple view|
|**setTarget(Context context, View view)**|Sets the target view|
|**setTarget(View view, float radiusRatio)**|Sets the target view and the relative size ratio|
|**setTarget(int centerX, int centerY)**|Uses coordinates to position the ripple|
|**setOnEnd(Runnable onEnd)**|Executes the wrapped code upon ending the ripple animation|
|**setOnStart(Runnable onStart)**|Executes the wrapped code upon starting the ripple animation|
|**setRippleDuration(int rippleDuration)**|Sets the duration of the ripple animation|
|**setDrawListener(ViewDrawListener listener)**|Sets a listener for when the view is first drawn|
|**setRippleType(int rippleType)**|Sets the type of ripple. Ex: RippleView.RIPPLE_TYPE_INDICATOR|
|**setRippleColor(int rippleColor)**|Sets the color of the ripple effect|
|**setRippleRepeats(int rippleRepeats)**|Sets how many times the ripple will repeat|
|**setRippleRepeatMode(int rippleRepeatMode)**|Sets the repeat mode. Ex: RippleView.REPEAT_RESTART_MODE|
|**setRippleCount(int rippleCount)**|Sets the amount of ripple to display|
|**setRippleMinOpacity(float rippleMinOpacity)**|Sets the min opacity a ripple can have|
|**setRippleMaxOpacity(float rippleMaxOpacity)**|Sets the max opacity a ripple can have|
|**setRippleStrokeWidth(float rippleStrokeWidth)**|Sets the width in pixels of the stroke used for outline style|

Xml Layout **APIs** offered by the **Rippple View**.

The ripple view can be added directly on the xml layout. The properties below will allow you to modify the ripple to your taste.

#### In XML RippleView properties:

 * **rv_rippleType:**  The type of ripple to be shown, The ripple can be outlined or filled.
 * **rv_rippleRepeatMode:**  The repeat mode of how the animation should be repeated.
 * **rv_rippleShapeType:**  The ripple can be circular or rectangular.
 * **rv_autoStartAnimation:**  Determines whether the ripple animation should start automatically.
 * **rv_rippleDuration:**  Determines the amount of time that the ripple animation should last.
 * **rv_rippleColo:r** Determines the color the ripple should have.
 * **rv_rippleCount:**  Determines the amount of ripples to animate.
 * **rv_rippleStrokeWidth:**  Determine the width of the strokes that are shown if outline is chosen
 * **rv_rippleClipRadius:**  Determines how big the clipped area radius will be for the Indicator ripple
 * **rv_rippleClipWidth:**  Determines how big the clipped area width will be for the Indicator ripple
 * **rv_rippleClipHeight:** Determines how big the clipped area height will be for the Indicator ripple
 * **rv_rippleMaxOpacity:**  Determines the max amount of opacity a ripple can have
 * **rv_rippleMinOpacity:** Determines the lowest amount of opacity a ripple can have
 * **rv_rippleMaxRadius:** Determines the hightest radius a ripple can have
 * **rv_rippleMinRadius:** Determines the lowest radius a ripple can have
 * **rv_rippleMinWidth:** Determines the lowest width a rectangular ripple can have
 * **rv_rippleMaxWidth:** Determines the highest width a rectangular ripple can have
 * **rv_rippleMinHeight:** Determines the lowest height a rectangular ripple can have
 * **rv_rippleMaxHeight:** Determines the highest height a rectangular ripple can have
 * **rv_rippleRepeatCount:** Determines the amount of times a ripple will 
 * **rv_rippleCornerRadiu:s** Determines how rounded the corners of a rectangular ripple is 
 
## How to use it?

### Step 1

* Add it in your root build.gradle at the end of repositories:

``` groovy
    allprojects {
    		repositories {
    			maven { url 'http
       s://jitpack.io' }
    		}
    }
```

* Add as a dependency in you applications build.gradle.

``` groovy
   dependencies {
   	  implementation 'com.github.EudyContreras:RippleEffect:Tag'
   }
```

### Step 2

* Add the Ripple View to your layout:

``` xml
    <com.eudycontreras.rippleview
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"
        app:rv_rippleType="indicatorRipple"
        app:rv_rippleRepeatMode="restart"
        app:rv_rippleShapeType="circle"
        app:rv_autoStartAnimation="true"
        app:rv_rippleDuration="3000"
        app:rv_rippleColor="@color/blue"
        />
```

* Or add the ripple directly through code:

``` java
    RippleView ripple = new RippleView(this);
    
    ripple.setRippleType(RippleView.RIPPLE_TYPE_INDICATOR);
    ripple.setRippleClipRadius(DimensionUtility.convertDpToPixel(this,50));
    ripple.setRippleColor(ContextCompat.getColor(this,R.color.accent));
    ripple.setRippleCount(3);
    ripple.setRippleMinOpacity(0.1f);
    ripple.setRippleMaxOpacity(0.8f);
    ripple.setRippleRepeatMode(RippleView.REPEAT_RESTART_MODE);
    ripple.setRippleRepeats(RippleView.INFINITE_REPEATS);
    ripple.setRippleDuration(2500);
    ripple.setUpWith(findViewById(R.id.login_activity));
```

When using the RippleView through code it is importatnt to set a target at the right place. The target must be set once the view's dimensions and location have been computed.

``` java

    private float radiusRatio = 1.7;
    private float clipRatio = 0.5;
        
    void onPause(){
        super.onPause();
        ripple.stopRippleAnimation()
    }
    
    void onResume() {
        super.onResume()
        
        View view = findViewById(R.id.someElement);
       
        view.post {
            ripple.setTarget(this, view, radiusRatio, clipRatio)
            ripple.startRippleAnimation()
        }
    }
```  

## Authors:

**Eudy Contreras**

## License:

This project is licensed under the MIT License - see the [Licence](https://github.com/EudyContreras/RippleEffect/blob/EudyContreras-readme/LICENSE) file for details
