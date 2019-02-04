
# Ripple Effect Library

[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21s)
[![License: ISC](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/ISC)

#### This library allows you to display a ripple effect as an overlay to any view. The ripple effect is fully customizeable and supports a variety of options.

![Ripple Effect Image][RippleImage]

[RippleImage]: https://github.com/EudyContreras/RippleEffect/blob/EudyContreras-readme/Ripple.png

## Usage

### Step 1

Add dependencies in build.gradle.

```groovy
    dependencies {
       compile 'com.wang.avi:library:2.1.3'
    }
```

### Step 2

Add the Ripple View to your layout:

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

Or add the ripple directly through code:

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
    public void onResume() {
        super.onResume()
        
        View view = findViewById(R.id.someElement);
        float radiusRatio = 1.7;
        float clipRatio = 0.5;
       
        view.post {
            ctaView.setTarget(this, view, radiusRatio, clipRatio)
            //ctaView.stopRippleAnimation()
            ctaView.startRippleAnimation()
        }
    }
```  
## License:

This project is licensed under the MIT License - see the [Licence](https://github.com/EudyContreras/RippleEffect/blob/master/LICENSE.md) file for details
