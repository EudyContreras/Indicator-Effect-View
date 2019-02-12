package com.eudycontreras.indicatoreffectlib.utilities;

import android.graphics.Color;
import androidx.annotation.ColorInt;

/**
 * <b>Note:</b> Unlicensed private property of the author and creator
 * unauthorized use of this class outside of the Indicator Effect project
 * by the author may result on legal prosecution.
 * <p>
 * Created by <B>Eudy Contreras</B>
 *
 * @author  Eudy Contreras
 * @version 1.0
 * @since   2018-03-31
 */
public class ColorUtility {

    public static int colorDecToHex(int r, int g, int b) {
        return Color.parseColor(colorDecToHexString(r, g, b));
    }

    public static int colorDecToHex(int a, int r, int g, int b) {
        return Color.parseColor(colorDecToHexString(a, r, g, b));
    }

    public static String colorDecToHexString(int r, int g, int b) {
        return colorDecToHexString(255,r,g,b);
    }

    public static String colorDecToHexString(int a, int r, int g, int b) {
        String red = Integer.toHexString(r);
        String green = Integer.toHexString(g);
        String blue = Integer.toHexString(b);
        String alpha = Integer.toHexString(a);

        if (red.length() == 1) {
            red = "0" + red;
        }
        if (green.length() == 1) {
            green = "0" + green;
        }
        if (blue.length() == 1) {
            blue = "0" + blue;
        }
        if (alpha.length() == 1){
            alpha = "0" + alpha;
        }

        return "#" + alpha + red + green + blue;
    }

    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static void adjustAlpha(SoulColor color, float factor) {
       color.setAlpha(Math.round(color.getAlpha() * factor));
    }

    public static void interpolateColor(SoulColor start, SoulColor end, float amount, SoulColor result) {
        result.setColor(start);

        result.setRed((int)(start.red + ((end.red - start.red) * amount)));
        result.setGreen((int)(start.green + ((end.green - start.green) * amount)));
        result.setBlue((int)(start.blue + ((end.blue - start.blue) * amount)));
    }

    public static SoulColor toSoulColor(int color){
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return new SoulColor(alpha, red, green, blue);
    }

    public static class SoulColor{

        private int tempColor;

        private int red;
        private int green;
        private int blue;
        private int alpha;

        public SoulColor(int alpha, int red, int green, int blue) {
            this.alpha = alpha;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public SoulColor(int red, int green, int blue) {
            this(1,red,green,blue);
        }

        public SoulColor(int color){
            this.alpha = Color.alpha(color);
            this.red = Color.red(color);
            this.green = Color.green(color);
            this.blue = Color.blue(color);
        }

        public SoulColor(){
            this(0x000000);
        }

        public void setColor(SoulColor color){
            this.alpha = color.alpha;
            this.red = color.red;
            this.green = color.green;
            this.blue = color.blue;
        }

        public void setColor(int color){
            this.alpha = Color.alpha(color);
            this.red = Color.red(color);
            this.green = Color.green(color);
            this.blue = Color.blue(color);
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = Math.round(255f * alpha);
        }

        public int toColor(){
            if(tempColor == -1){
                tempColor = Color.argb(alpha,red,green,blue);
                return tempColor;
            }else{
                if(colorChanged()){
                    tempColor = Color.argb(alpha,red,green,blue);
                    return tempColor;
                }
            }

            return tempColor;
        }

        private boolean colorChanged(){
            return true;
        }

        public static SoulColor copy(SoulColor color){
            return new SoulColor(color.alpha,color.red,color.green,color.blue);
        }
    }
}
