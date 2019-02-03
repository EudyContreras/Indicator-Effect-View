package com.eudycontreras.rippleeffectlib.particles;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.eudycontreras.rippleeffectlib.Bounds;
import com.eudycontreras.rippleeffectlib.utilities.ColorUtility;

/**
 * Created by eudycontreras.
 */
public abstract class Particle {

    public final static float DEFAULT_LIFE_TIME = 5;
    public final static float DEFAULT_VELOCITY = 5;

    protected float centerX;
    protected float centerY;

    protected float targetX = Integer.MIN_VALUE;
    protected float targetY = Integer.MIN_VALUE;

    protected float velX;
    protected float velY;

    protected float varianceX;
    protected float varianceY;

    protected float radiusRatio = 0.0f;
    protected float actualRadius;
    protected float radius;
    protected float spacing;
    protected float opacity = 1.0f;

    protected float lifeSpan = 1.0f;
    protected float decay;

    protected boolean visible;
    protected boolean killed;
    protected boolean fade = true;
    protected boolean shrink = true;
    protected boolean checkBounds = false;

    protected Bounds bounds;
    protected Paint paint;
    protected ColorUtility.SoulColor color;

    protected Particle(float lifeTime, float x, float y, float velX, float velY, float varianceX, float varianceY, float radius, int color, Paint paint, Bounds bounds) {
        this.centerX = x;
        this.centerY = y;
        this.velX = velX;
        this.velY = velY;
        this.varianceX = varianceX;
        this.varianceY = varianceY;
        this.radius = radius;
        this.color = ColorUtility.toSoulColor(color);
        this.bounds = bounds;
        this.decay = 0.016f / lifeTime;
        this.paint = paint;
    }

    protected Particle(float lifeTime, float x, float y, float velX, float velY, float radius, int color, Paint paint, Bounds bounds) {
        this(lifeTime, x, y, velX, velY, 0, 0, radius, color, paint, bounds);
    }

    protected Particle(float lifeTime, float x, float y, float radius, int color, Paint paint, Bounds bounds) {
        this(lifeTime, x, y,0, 0, radius, color,paint, bounds);
    }

    protected Particle(float x, float y, float radius, int color, Paint paint, Bounds bounds) {
        this(Integer.MAX_VALUE, x, y,0, 0, radius, color,paint, bounds);
    }

    protected Particle(float x, float y, float radius, int color, Bounds bounds) {
        this(Integer.MAX_VALUE, x, y,0, 0, radius, color,new Paint(), bounds);
    }

    public abstract void init();

    protected abstract void draw(Canvas canvas);

    public void update(){
        centerX += (velX + varianceX);
        centerY += (velY + varianceY);

        if(killed) {
            lifeSpan -= decay;
        }
    }

    public void update(float duration, float time){

        velX = targetX != Integer.MIN_VALUE ? ((targetX - centerX) / duration) : velX;
        velY = targetY != Integer.MIN_VALUE ? ((targetY - centerY) / duration) : velY;

        centerX += ((velX + varianceX) * time);
        centerY += ((velY + varianceY) * time);

        if(shrink){
            radiusRatio = time;
            radius = actualRadius * radiusRatio;
        }

        if(killed) {
            lifeSpan -= (decay * time);
        }else{
            if(fade) {
                opacity = time;
            }
        }
    }

    public float checkDistanceTo(Particle particle) {

        float distanceX = this.centerX+radius - particle.getCenterX()+radius;
        float distanceY = this.centerY+radius - particle.getCenterY()+radius;

        return distanceX*distanceX + distanceY*distanceY;
    }

    public boolean isAlive() {
        if(bounds != null) {
            return (!checkBounds || bounds.inRange(centerX, centerY, (radius * 2))) && (lifeSpan > 0) && (radius > 0) && (opacity > 0);
        }
        return lifeSpan > 0;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isFade() {
        return fade;
    }

    public void setFade(boolean fade) {
        this.fade = fade;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean isShrink() {
        return shrink;
    }

    public void setShrink(boolean shrink) {
        this.shrink = shrink;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public float getVarianceX() {
        return varianceX;
    }

    public void setVarianceX(float varianceX) {
        this.varianceX = varianceX;
    }

    public float getVarianceY() {
        return varianceY;
    }

    public void setVarianceY(float varianceY) {
        this.varianceY = varianceY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.actualRadius = radius;
    }

    public ColorUtility.SoulColor getColor() {
        return color;
    }

    public void setColor(ColorUtility.SoulColor color) {
        this.color = color;
    }

    public float getDecay() {
        return decay;
    }

    public void setDecay(float decay) {
        this.decay =  0.016f / decay;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public boolean isCheckBounds() {
        return checkBounds;
    }

    public void setCheckBounds(boolean checkBounds) {
        this.checkBounds = checkBounds;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getSpacing(){
        return spacing;
    }
}
