package com.eudycontreras.indicatoreffectlib.particles;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import androidx.annotation.RestrictTo;
import com.eudycontreras.indicatoreffectlib.Bounds;
import com.eudycontreras.indicatoreffectlib.utilities.ColorUtility;
import com.eudycontreras.indicatoreffectlib.views.IndicatorView;

/**
 * <b>Note:</b> Unlicensed private property of the author and creator
 * unauthorized use of this class outside of the Indicator Effect project
 * by the author may result on legal prosecution.
 * <p>
 * Created by <B>Eudy Contreras</B>
 *
 * @author Eudy Contreras
 * @version 1.0
 * @since 2018-03-31
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ParticleIndicator extends Particle {

    public static final int RIPPLE_TYPE_OUTLINE = 0;
    public static final int RIPPLE_TYPE_FILLED = 1;
    public static final int RIPPLE_TYPE_INDICATOR = 2;

    private float cornerRadius = 0;

    private float x;
    private float y;

    private float width;
    private float height;

    private float maxWidth;
    private float maxHeight;

    private float minWidth;
    private float minHeight;

    private float maxRadius;
    private float minRadius;

    private float minOpacity;
    private float maxOpacity;

    private float clipRadius;

    private float clipWidhtRatio;
    private float clipHeightRatio;

    private float strokeWidth;

    private float innerOutlineWidth;

    private float interpolation;

    private int shapeType;
    private int type;

    private Path clipPath;

    public ParticleIndicator() {
        super(0, 0, 0, 0, null, null);
    }

    public ParticleIndicator(float x, float y, float radius, int color, Paint paint, Bounds bounds) {
        super(x, y, radius, color, paint, bounds);
    }

    public ParticleIndicator(float x, float y, float radius, int color, Bounds bounds) {
        super(x, y, radius, color, bounds);
    }

    public void init() {
        clipPath = new Path();
        clipPath.reset();
        if (shapeType == IndicatorView.INDICATOR_SHAPE_CIRCLE) {
            clipPath.addCircle(centerX, centerY, clipRadius, Path.Direction.CCW);
        } else {
            float top = y - (minHeight / 2);
            float left = x - (minWidth / 2);
            float bottom = y + (minHeight / 2);
            float right = x + (minWidth / 2);
            clipPath.addRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, Path.Direction.CCW);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void update(float duration, float time) {
        if (shapeType == IndicatorView.INDICATOR_SHAPE_CIRCLE) {
            radius = minRadius + ((maxRadius - minRadius) * time);
        } else {
            width = minWidth + ((maxWidth - minWidth) * time);
            height = minHeight + ((maxHeight - minHeight) * time);

            x = x - (width / 2);
            y = y - (height / 2);
        }

        opacity = minOpacity + ((maxOpacity - minOpacity) * (maxOpacity - minOpacity - time));

        if (opacity < 0f)
            opacity = 0f;
        if (opacity > 1f)
            opacity = 1f;

        if (colorStart != null && colorEnd != null) {
            ColorUtility.interpolateColor(colorStart, colorEnd, time, color);
        }
    }

    @Override
    public boolean isAlive() {
        return (opacity > 0f && (radius > 0 || width > 0 || height > 0)) || alwaysAlive;
    }

    public void draw(Canvas canvas) {
        switch (type) {
            case RIPPLE_TYPE_FILLED:
                drawFilledRipple(canvas);
                break;
            case RIPPLE_TYPE_INDICATOR:
                drawIndicatorRipple(canvas);
                break;
            case RIPPLE_TYPE_OUTLINE:
                drawOutLineRipple(canvas);
                break;
        }

        if (innerOutlineColor != null) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(innerOutlineWidth);
            paint.setColor(innerOutlineColor.toColor());

            canvas.drawCircle(centerX, centerY, minRadius, paint);
        }
    }

    private void drawFilledRipple(Canvas canvas) {
        color.setAlpha(opacity);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color.toColor());

        if (shapeType == IndicatorView.INDICATOR_SHAPE_CIRCLE) {
            canvas.drawCircle(centerX, centerY, radius, paint);

            if (strokeColor != null) {
                strokeColor.setAlpha(opacity);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                paint.setColor(strokeColor.toColor());

                canvas.drawCircle(centerX, centerY, radius, paint);
            }

        } else {
            float top = y;
            float left = x;
            float bottom = y + height;
            float right = x + width;
            canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, paint);
        }
    }

    private void drawOutLineRipple(Canvas canvas) {
        color.setAlpha(opacity);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color.toColor());

        if (shapeType == IndicatorView.INDICATOR_SHAPE_CIRCLE) {
            canvas.drawCircle(centerX, centerY, radius, paint);
        } else {
            float top = y;
            float left = x;
            float bottom = y + height;
            float right = x + width;
            canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, paint);
        }
    }

    @SuppressWarnings("Deprecated")
    @SuppressLint("Deprecated")
    private void drawIndicatorRipple(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(clipPath);
        } else {
            canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
        }

        color.setAlpha(opacity);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color.toColor());

        if (shapeType == IndicatorView.INDICATOR_SHAPE_CIRCLE) {
            canvas.drawCircle(centerX, centerY, radius, paint);

            if (strokeColor != null) {
                strokeColor.setAlpha(opacity);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                paint.setColor(strokeColor.toColor());

                canvas.drawCircle(centerX, centerY, radius, paint);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(clipPath);
            } else {
                canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
            }
        } else {
            float top = y;
            float left = x;
            float bottom = y + height;
            float right = x + width;
            canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, paint);
        }
    }

    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
    }

    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
    }

    public void setMinOpacity(float minOpacity) {
        this.minOpacity = minOpacity;
    }

    public void setMaxOpacity(float minOpacity) {
        this.maxOpacity = minOpacity;
    }

    public void setClipRadius(float clipRadius) {
        this.clipRadius = clipRadius;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setInnerOutlineWidth(float innerOutlineWidth) {
        this.innerOutlineWidth = innerOutlineWidth;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }

    public void setMaxWidth(float width) {
        this.maxWidth = width;
    }

    public void setMaxHeight(float height) {
        this.maxHeight = height;
    }

    public void setMinWidth(float width) {
        this.minWidth = width;
    }

    public void setMinHeight(float height) {
        this.minHeight = height;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }
}
