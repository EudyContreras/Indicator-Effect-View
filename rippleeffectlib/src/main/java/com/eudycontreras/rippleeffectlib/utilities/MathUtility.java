package com.eudycontreras.rippleeffectlib.utilities;

/**
 * Created by eudycontreras.
 */
public class MathUtility {

     public static float Map(
            float value,
            float fromLow,
            float fromHigh,
            float toLow,
            float toHigh) {
        float fromRangeSize = fromHigh - fromLow;
        float toRangeSize = toHigh - toLow;
        float valueScale = (value - fromLow) / fromRangeSize;
        return toLow + valueScale * toRangeSize;
    }
}
