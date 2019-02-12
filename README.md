
# Indicator Effect Library

[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21s)
[![License: ISC](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/ISC)
[![](https://jitpack.io/v/EudyContreras/Indicator-Effect-View.svg)](https://jitpack.io/#EudyContreras/Indicator-Effect-View)
![Version](https://img.shields.io/github/release/EudyContreras/IndicatorEffect.svg?style=flat)
![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)


#### This library allows you to display a CTA Call To Action indicator effect as an overlay to any view. The indicator effect is fully customizeable and supports a variety of options.

![Indicator Effect Image][IndicatorImage]

[IndicatorImage]: https://github.com/EudyContreras/Ripple-Effect-View/blob/EudyContreras-readme/indicator.png


## About:

This indicator effect view allows for a high variety of customizations in order to fit the needs of all users. It is simple to use and easy to customize. Here are some of the features of the indicator effect view.

* Create an effect with an unlimited amount of indicator ripples given a specified time
* Create a indicator effect with a border 
* Control the minimum and maximum opacity of the indicator ripple from start to end
* Interpolate the indicator ripple between a start and end color
* Choose between a circular or rectangular indicator ripple with possibility of rounded corners.
* Choose between indicator ripples, filled indicator ripples, or outline indicator ripples.
* Make the indicator ripple animation repeat as many times as you want.
* Add an action to be perform upon starting a indicator effect animation
* Add an action to be perform upon the end of a indicator effect animation
* Add an interpolator to the ripple animation
* Etc...

## APIs and customization:

In code **APIs** offered by **Indicator View**.

The indicator effect has a high number of apis that give full control of how the ripple is shown to the user. 

### Some of the in Code Code methods:
|APIs | Description|
|---|---|
|**startIndicatorAnimation**| *No input* |
|**startIndicatornimation(int startDelay)**| *The delay before the indicator starts* |
|**stopIndicatorAnimation(int duration)**| *Stop the indicator animation* |
|**stopIndicatorAnimation()**| *Stop the ripple animation* |
|**setUpWith(ViewGroup parent)**| *Sets a parent root to the indicator view* |
|**setTarget(Context context, View view)**| *Sets the target view* |
|**setTarget(View view, float radiusRatio)**| *Sets the target view and the relative size ratio* |
|**setTarget(int centerX, int centerY)**| *Uses coordinates to position the indicator* |
|**setOnEnd(Runnable onEnd)**| *Executes the wrapped code upon ending the indicator animation* |
|**setOnStart(Runnable onStart)**| *Executes the wrapped code upon starting the indicator animation* |
|**setIndicatorDuration(int indicatorDuration)**| *Sets the duration of the indicator animation* |
|**setDrawListener(ViewDrawListener listener)**| *Sets a listener for when the view is first drawn* |
|**setIndicatorType(int indicatorType)**| *Sets the type of indicator. Ex: IndicatorView.RIPPLE_TYPE_INDICATOR* |
|**setIndicatorColor(int indicatorColor)**| *Sets the color of the indicator effect* |
|**setIndicatorRepeats(int indicatorRepeats)**| *Sets how many times the indicator will repeat* |
|**setIndicatorRepeatMode(int indicatorRepeatMode)**| *Sets the repeat mode. Ex: IndicatorView.REPEAT_RESTART_MODE* |
|**setIndicatorRippleCount(int indicatorCount)**| *Sets the amount of indicator to display* |
|**setIndicatorMinOpacity(float indicatorMinOpacity)**| *Sets the min opacity a indicator can have* |
|**setIndicatorMaxOpacity(float indicatorMaxOpacity)**| *Sets the max opacity a indicator can have* |
|**setIndicatorStrokeWidth(float indicatorStrokeWidth)**| *Sets the width in pixels of the stroke used for outline style* |



**XML** Layout **APIs** offered by the **Indicator View**.

The indicator view can be added directly on the xml layout. The properties below will allow you to modify the indicator to your taste.

### Some of the in XML IndicatorView properties:

|Property | Description|
|---|---|
| **rv_indicatorType:**  |The type of indicator to be shown, The indicator can be outlined or filled.|
| **rv_indicatorRepeatMode:**  |The repeat mode of how the animation should be repeated.|
| **rv_indicatorShapeType:**  |The indicator can be circular or rectangular.|
| **rv_autoStartAnimation:**  |Determines whether the indicator animation should start automatically.|
| **rv_indicatorDuration:**  |Determines the amount of time that the indicator animation should last.|
| **rv_indicatorColor:** |Determines the color the indicator should have.|
| **rv_indicatorStrokeColor:** |Determines the border color of the indicator if using indicator border.|
| **rv_indicatorColorStart:** |Determines the start color the indicator should have if interpolation enabled.|
| **rv_indicatorColorEnd:** |Determines the ending color the indicator should have if interpolation enabled.|
| **rv_indicatorCount:**  |Determines the amount of indicator to animate.|
| **rv_indicatorStrokeWidth:**  |Determine the width of the strokes that are shown if outline is chosen|
| **rv_indicatorClipRadius:**  |Determines how big the clipped area radius will be for the Indicator ripple|
| **rv_indicatorClipWidth:**  |Determines how big the clipped area width will be for the Indicator ripple
| **rv_indicatorClipHeight:** |Determines how big the clipped area height will be for the Indicator ripple|
| **rv_indicatoraxOpacity:**  |Determines the max amount of opacity a indicator can have|
| **rv_indicatorMinOpacity:** |Determines the lowest amount of opacity a indicator can have|
| **rv_indicatorMaxRadius:** |Determines the hightest radius a indicator can have|
| **rv_indicatorMinRadius:** |Determines the lowest radius a indicator can have|
| **rv_indicatorMinWidth:** |Determines the lowest width a rectangular indicator can have|
| **rv_indicatorMaxWidth:** |Determines the highest width a rectangular indicator can have|
| **rv_indicatorMinHeight:** |Determines the lowest height a rectangular indicator can have|
| **rv_indicatorMaxHeight:** |Determines the highest height a rectangular indicator can have|
| **rv_indicatorRepeatCount:** |Determines the amount of times a indicator will |
| **rv_indicatorCornerRadius:** |Determines how rounded the corners of a rectangular indicator is |
 
 
## How to use it?

#### Step 1

Add it in your root build.gradle at the end of repositories:

``` groovy

    dependencies {
       classpath 'com.github.dcendents:android-maven-gradle-plugin:${version}'
    }
    
    allprojects {
    		 repositories {
    			  maven { url 'https://jitpack.io' }
    		}
    }
```

Add as a dependency in you applications build.gradle.

``` groovy

   dependencies {
   	  implementation "com.github.EudyContreras:Indicator-Effect-View:v1.0"
   }
```

#### Step 2

Add the Ripple View to your layout:

``` xml
    <com.eudycontreras.indicatorview
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"
        app:iv_indicatorType="IndicatorRipple"
        app:iv_indicatorRepeatMode="restart"
        app:iv_indicatorShapeType="circle"
        app:iv_autoStartAnimation="true"
        app:iv_indicatorDuration="3000"
        app:iv_indicatorColor="@color/blue"
        />
```

Or add the indicator directly through code:

``` java
    IndicatorView indicator = new IndicatorView(this);
    
    ripple.setIndicatorType(IndicatorView.RIPPLE_TYPE_INDICATOR);
    ripple.setIndicatorClipRadius(DimensionUtility.convertDpToPixel(this,50));
    ripple.setIndicatorColor(ContextCompat.getColor(this,R.color.accent));
    ripple.setIndicatorCount(3);
    ripple.setIndicatorMinOpacity(0.1f);
    ripple.setIndicatorMaxOpacity(0.8f);
    ripple.setIndicatorRepeatMode(IndicatorView.REPEAT_RESTART_MODE);
    ripple.setIndicatorRepeats(IndicatorView.INFINITE_REPEATS);
    ripple.setIndicatorDuration(2500);
    ripple.setUpWith(findViewById(R.id.login_activity));
```


When using the IndicatorView through code it is importatnt to set a target at the right place. The target must be set once the view's dimensions and location have been computed.



``` java

    private float radiusRatio = 1.7;
    private float clipRatio = 0.5;
        
    void onPause(){
        super.onPause();
        indicator.stopIndicatorAnimation()
    }
    
    void onResume() {
        super.onResume()
        
        View view = findViewById(R.id.someElement);
       
        view.post {
            indicator.setTarget(this, view, radiusRatio, clipRatio)
            indicator.startIndicatorAnimation()
        }
    }
```  

## Authors:

**Eudy Contreras**

## Contact:

If you wish to contact you may reach me through my [Linked](https://www.linkedin.com/in/eudycontreras/) or my [Email](EudyContrerasRosario@gmail.com)

## Future works:

There are parts of this library that are yet to be finished and there are also some things which I plan to add to the library. These things will be shown here along with popular demands.

- [ ] Port the code kotlin
- [ ] Add to different repositories besides jitpack
- [ ] Allow the user to set a repeat interval/wait time
- [ ] Add builder pattern for creating indicator
- [ ] Allow the effect to translate with its target upon animations

## License:

This project is licensed under the MIT License - see the [Licence](https://github.com/EudyContreras/RippleEffect/blob/EudyContreras-readme/LICENSE) file for details
