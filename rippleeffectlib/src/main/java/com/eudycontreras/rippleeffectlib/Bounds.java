package com.eudycontreras.rippleeffectlib;

/**
 * <b>Note:</b> Unlicensed private property of the author and creator
 * unauthorized use of this class outside of the Ripple Effect project
 * by the author may result on legal prosecution.
 * <p>
 * Created by <B>Eudy Contreras</B>
 *
 * @author  Eudy Contreras
 * @version 1.0
 * @since   2018-03-31
 */
public class Bounds {

    private final float x;
    private final float y;

    private final float width;
    private final float height;

    public Bounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean inRange(float sourceX, float sourceY){
        return (sourceX >= x && sourceX < width) && (sourceY >= y && sourceY < height);
    }

    public boolean inRange(float sourceX, float sourceY, float radius){
        return (sourceX >= (x-radius) && sourceX < (width + radius)) && (sourceY >= (y-radius) && sourceY < (height + radius));
    }
}
