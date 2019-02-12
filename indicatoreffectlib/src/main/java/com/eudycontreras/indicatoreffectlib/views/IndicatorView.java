package com.eudycontreras.indicatoreffectlib.views;

import android.animation.*;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.NonNull;
import com.eudycontreras.indicatoreffectlib.Bounds;
import com.eudycontreras.indicatoreffectlib.Property;
import com.eudycontreras.indicatoreffectlib.R;
import com.eudycontreras.indicatoreffectlib.particles.ParticleIndicator;
import com.eudycontreras.indicatoreffectlib.utilities.ColorUtility;
import com.eudycontreras.indicatoreffectlib.utilities.DimensionUtility;

import java.util.ArrayList;

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
public class IndicatorView extends View {

    public static final int INDICATOR_SHAPE_CIRCLE = 0;
    public static final int INDICATOR_SHAPE_RECTANGLE = 1;

    public static final int INDICATOR_TYPE_OUTLINE = 0;
    public static final int INDICATOR_TYPE_FILLED = 1;
    public static final int INDICATOR_TYPE_AROUND = 2;

    public static final int INFINITE_REPEATS = ObjectAnimator.INFINITE;

    public static final int REPEAT_MODE_RESTART = ObjectAnimator.RESTART;
    public static final int REPEAT_MODE_REVERSE = ObjectAnimator.REVERSE;

    private int backgroundColor = Color.TRANSPARENT;

    private int indicatorShape = INDICATOR_SHAPE_CIRCLE;
    private int indicatorType;
    private int indicatorCount;
    private int indicatorColor;
    private int indicatorStrokeColor;
    private int indicatorColorStart;
    private int indicatorColorEnd;
    private int indicatorDelay;
    private int indicatorRepeats;
    private int indicatorRepeatMode;
    private int indicatorDuration;
    private int indicatorIntervalDelay;
    private int indicatorInnerOutlineColor;

    private int usableWidth;
    private int usableHeight;

    private float indicatorX = Integer.MIN_VALUE;
    private float indicatorY = Integer.MIN_VALUE;

    private float centerX = Integer.MIN_VALUE;
    private float centerY = Integer.MIN_VALUE;

    private float offsetX = 0f;
    private float offsetY = 0f;

    private float innerOutLineWidth;

    private float indicatorMinWidth;
    private float indicatorMinHeight;

    private float indicatorMaxWidth;
    private float indicatorMaxHeight;

    private float indicatorMinOpacity;
    private float indicatorMaxOpacity;

    private float indicatorClipRadius;
    private float indicatorMaxRadius;
    private float indicatorMinRadius;

    private float indicatorCornerRadius;

    private float indicatorStrokeWidth;

    private long revealDuration = 300;
    private long concealDuration = 300;

    private boolean showInnerOutline = false;
    private boolean useColorInterpolation = false;
    private boolean showBorderStroke = false;
    private boolean animationRunning = false;
    private boolean autoStartIndicator = false;
    private boolean cleanUpAfter = false;

    private ParticleIndicator[] indicators;
    private ArrayList<Animator> animators;

    private Runnable onEnd;
    private Runnable onStart;
    private Interpolator indicatorInterpolator;
    private ViewDrawListener listener;

    private AnimatorSet animatorSet;
    private ViewGroup parent;
    private Bounds bounds;
    private Paint paint;

    private ColorUtility.SoulColor color;
    private ColorUtility.SoulColor strokeColor;
    private ColorUtility.SoulColor colorStart;
    private ColorUtility.SoulColor colorEnd;
    private ColorUtility.SoulColor colorInnerOutline;

    private View target;

    private AnimatorListenerAdapter animationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            show(revealDuration);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            dismiss(concealDuration);
        }
    };

    public IndicatorView(Context context) {
        super(context);
        initialize(null);
    }

    public IndicatorView(Context context, @NonNull ViewGroup parent) {
        super(context);
        initialize(parent);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    private void initialize(ViewGroup parentView) {

        paint = new Paint();
        paint.setAntiAlias(true);

        color = new ColorUtility.SoulColor();

        indicatorInterpolator = new LinearInterpolator();

        animatorSet = new AnimatorSet();
        animators = new ArrayList<>();

        parent = parentView;
    }

    public void setUpAttributes(TypedArray typedArray) {
        if (typedArray != null) {
            indicatorType = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorType, INDICATOR_TYPE_FILLED);
            indicatorCount = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorCount, 3);
            indicatorDuration = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorDuration, 2000);
            indicatorIntervalDelay = typedArray.getInt(R.styleable.IndicatorView_iv_intervalDelay, 0);
            indicatorColor = typedArray.getColor(R.styleable.IndicatorView_iv_indicatorColor, 0xFFFFFFFF);
            indicatorStrokeColor = typedArray.getColor(R.styleable.IndicatorView_iv_indicatorStrokeColor, 0xFFFFFFFF);
            indicatorColorStart = typedArray.getColor(R.styleable.IndicatorView_iv_indicatorColorStart, 0xFFFFFFFF);
            indicatorColorEnd = typedArray.getColor(R.styleable.IndicatorView_iv_indicatorColorEnd, 0xFFFFFFFF);
            indicatorMinOpacity = typedArray.getFloat(R.styleable.IndicatorView_iv_indicatorMinOpacity, 0f);
            indicatorMaxOpacity = typedArray.getFloat(R.styleable.IndicatorView_iv_indicatorMaxOpacity, 1f);
            indicatorClipRadius = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorClipRadius, 0f);
            indicatorStrokeWidth = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorStrokeWidth, DimensionUtility.convertDpToPixel(getContext(), 2.5f));
            indicatorMinRadius = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMinRadius, 0);
            indicatorMaxRadius = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMaxRadius, DimensionUtility.convertDpToPixel(getContext(), 30));
            indicatorRepeats = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorRepeatCount, ObjectAnimator.INFINITE);
            indicatorRepeatMode = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorRepeatMode, 0);
            autoStartIndicator = typedArray.getBoolean(R.styleable.IndicatorView_iv_autoStartAnimation, false);
            useColorInterpolation = typedArray.getBoolean(R.styleable.IndicatorView_iv_useColorInterpolation, false);
            showBorderStroke = typedArray.getBoolean(R.styleable.IndicatorView_iv_showBorderStroke, false);
            indicatorShape = typedArray.getInt(R.styleable.IndicatorView_iv_indicatorShapeType, INDICATOR_SHAPE_CIRCLE);
            indicatorCornerRadius = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorCornerRadius, 0f);
            indicatorMinWidth = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMinWidth, 0f);
            indicatorMinHeight = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMinHeight, 0f);
            indicatorMaxWidth = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMaxWidth, DimensionUtility.convertDpToPixel(getContext(), 60));
            indicatorMaxHeight = typedArray.getDimension(R.styleable.IndicatorView_iv_indicatorMaxHeight, DimensionUtility.convertDpToPixel(getContext(), 30));
            initializeIndicator();
        }
    }

    private void initializeIndicator() {
        setAlpha(0);

        indicatorDelay = (int) ((double) indicatorDuration / (double) indicatorCount);
        indicators = new ParticleIndicator[indicatorCount];

        for (Animator animator : animators) {
            animator.cancel();
        }

        animators.clear();
        color.setColor(indicatorColor);

        if (useColorInterpolation) {
            if (colorStart == null || colorEnd == null) {
                colorStart = new ColorUtility.SoulColor();
                colorEnd = new ColorUtility.SoulColor();
            }
            colorStart.setColor(indicatorColorStart);
            colorEnd.setColor(indicatorColorEnd);
        } else {
            colorStart = null;
            colorEnd = null;
        }

        if (showBorderStroke) {
            if (strokeColor == null) {
                strokeColor = new ColorUtility.SoulColor();
            }
            strokeColor.setColor(indicatorStrokeColor);
        } else {
            strokeColor = null;
        }

        if (showInnerOutline) {
            if (colorInnerOutline == null) {
                colorInnerOutline = new ColorUtility.SoulColor();
            }

            colorInnerOutline.setColor(indicatorInnerOutlineColor);
        }

        for (int i = 0; i < indicators.length; i++) {
            ParticleIndicator indicator = new ParticleIndicator();
            indicator.setPaint(new Paint());
            indicator.setShapeType(indicatorShape);
            indicator.setCornerRadius(indicatorCornerRadius);
            indicator.setMinRadius(indicatorMinRadius);
            indicator.setMaxRadius(indicatorMaxRadius);
            indicator.setMinOpacity(indicatorMinOpacity);
            indicator.setMaxOpacity(indicatorMaxOpacity);
            indicator.setClipRadius(indicatorClipRadius);
            indicator.setStrokeWidth(indicatorStrokeWidth);
            indicator.setMinWidth(indicatorMinWidth);
            indicator.setMinHeight(indicatorMinHeight);
            indicator.setMaxWidth(indicatorMaxWidth);
            indicator.setMaxHeight(indicatorMaxHeight);
            indicator.setColor(color);
            indicator.setStrokeColor(strokeColor);
            indicator.setInnerOutlineColor(colorInnerOutline);
            indicator.setInnerOutlineWidth(innerOutLineWidth);
            indicator.setColorStart(colorStart);
            indicator.setColorEnd(colorEnd);
            indicator.setType(indicatorType);
            indicator.setX(indicatorX + offsetX);
            indicator.setY(indicatorY + offsetY);
            indicator.setCenterX(centerX + offsetX);
            indicator.setCenterY(centerY + offsetY);
            indicator.setVisible(true);
            indicator.setAlwaysAlive(true);
            indicator.init();

            indicators[i] = indicator;

            final int index = i;
            final long delay = (long) (index * indicatorDelay);
            final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

            animator.setStartDelay(delay);
            animator.setRepeatCount(indicatorRepeats);
            animator.setRepeatMode(indicatorRepeatMode);
            animator.setDuration(indicatorDuration);
            animator.addUpdateListener(animation -> {
                if (indicators[index] == null)
                    return;

                indicators[index].setShapeType(indicatorShape);
                indicators[index].setColor(color);
                indicators[index].setColorStart(colorStart);
                indicators[index].setColorEnd(colorEnd);
                indicators[index].setStrokeColor(strokeColor);
                indicators[index].setInnerOutlineColor(colorInnerOutline);
                indicators[index].setCornerRadius(indicatorCornerRadius);
                indicators[index].setMinOpacity(indicatorMinOpacity);
                indicators[index].setMaxOpacity(indicatorMaxOpacity);
                indicators[index].setMinRadius(indicatorMinRadius);
                indicators[index].setMaxRadius(indicatorMaxRadius);
                indicators[index].setClipRadius(indicatorClipRadius);
                indicators[index].setStrokeWidth(indicatorStrokeWidth);
                indicators[index].setInnerOutlineWidth(innerOutLineWidth);
                indicators[index].setCenterX(centerX + offsetX);
                indicators[index].setCenterY(centerY + offsetY);
                indicators[index].setX(indicatorX + offsetX);
                indicators[index].setY(indicatorY + offsetY);
                indicators[index].setMinWidth(indicatorMinWidth);
                indicators[index].setMinHeight(indicatorMinHeight);
                indicators[index].setMaxWidth(indicatorMaxWidth);
                indicators[index].setMaxHeight(indicatorMaxHeight);
                indicators[index].update(indicatorDuration, (float) animation.getAnimatedValue());

                if (!indicators[index].isAlive()) {
                    indicators[index] = null;
                }
                invalidate();
            });

            animators.add(animator);
        }
    }

    public void startIndicatorAnimation() {
        startIndicatorAnimation(0);
    }

    public void startIndicatorAnimation(int indicatorDelay) {
        startIndicatorAnimation(indicatorDelay, indicatorInterpolator);
    }

    public void startIndicatorAnimation(int indicatorDelay, Interpolator interpolator) {
        animatorSet.removeListener(animationListener);
        animatorSet.cancel();
        animatorSet = null;

        initializeIndicator();

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(interpolator);
        animatorSet.playTogether(animators);
        animatorSet.setStartDelay(indicatorDelay);
        animatorSet.addListener(animationListener);
        animatorSet.start();
        animationRunning = true;
    }

    public void stopIndicatorAnimation() {
        stopIndicatorAnimation(concealDuration);
    }

    public void stopIndicatorAnimation(long duration) {
        if (animationRunning) {
            dismiss(duration);
        }
    }

    private void show(long duration) {
        if (duration == 0) {
            if (onStart != null) {
                onStart.run();
            }
            setAlpha(1);
            return;
        }

        ValueAnimator showAnimator = ValueAnimator.ofFloat(0f, 1f);
        showAnimator.addUpdateListener(animation -> setAlpha((float) animation.getAnimatedValue()));
        showAnimator.setDuration(duration);
        showAnimator.setInterpolator(new DecelerateInterpolator());
        showAnimator.start();
    }

    private void dismiss(long duration) {
        if (duration == 0) {
            animatorSet.cancel();
            animationRunning = false;
            if (onEnd != null) {
                onEnd.run();
            }
            return;
        }

        ValueAnimator showAnimator = ValueAnimator.ofFloat(1f, 0f);
        showAnimator.addUpdateListener(animation -> setAlpha((float) animation.getAnimatedValue()));
        showAnimator.setDuration(duration);
        showAnimator.setInterpolator(new LinearInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.cancel();
                animationRunning = false;
                if (onEnd != null) {
                    onEnd.run();
                }
            }
        });

        showAnimator.start();
    }

    public void removeIndicator() {
        stopIndicatorAnimation();
        parent.removeView(this);
    }

    private void initializeValues() {
        int width = getWidth();
        int height = getHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        usableWidth = width - (paddingLeft + paddingRight);
        usableHeight = height - (paddingTop + paddingBottom);

        bounds = new Bounds(0, 0, usableWidth, usableHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);

        if (bounds == null) {

            initializeValues();

            for (ParticleIndicator indicator : indicators) {
                if (indicator == null)
                    return;

                indicator.setBounds(bounds);
                indicator.setColor(color);
                indicator.setStrokeColor(strokeColor);
                indicator.setColorStart(colorStart);
                indicator.setColorEnd(colorEnd);
                indicator.setInnerOutlineColor(colorInnerOutline);
                indicator.setCornerRadius(indicatorCornerRadius);
                indicator.setMinOpacity(indicatorMinOpacity);
                indicator.setMaxOpacity(indicatorMaxOpacity);
                indicator.setMinRadius(indicatorMinRadius);
                indicator.setMaxRadius(indicatorMaxRadius);
                indicator.setClipRadius(indicatorClipRadius);
                indicator.setStrokeWidth(indicatorStrokeWidth);
                indicator.setInnerOutlineWidth(innerOutLineWidth);
                indicator.setX(indicatorX + offsetX);
                indicator.setY(indicatorY + offsetY);
                indicator.setCenterX(centerX + offsetX);
                indicator.setCenterY(centerY + offsetY);
                indicator.setMaxWidth(indicatorMaxWidth);
                indicator.setMaxHeight(indicatorMaxHeight);
                indicator.setMinWidth(indicatorMinWidth);
                indicator.setMinHeight(indicatorMinHeight);
                indicator.update();
                indicator.init();
            }

            if (listener != null) {
                listener.onViewDrawn(this);
            }

            if (autoStartIndicator) {
                startIndicatorAnimation();
            }

            invalidate();
        }

        for (ParticleIndicator indicator : indicators) {
            if (indicator != null) {
                indicator.draw(canvas);
            }
        }
    }

    public void setTarget(View view) {
        setTarget(view, 2);
    }

    public void setTarget(View view, float radiusRatio) {
        setTarget(view, 1.25f, 1.5f, radiusRatio, 0.5f);
    }

    public void setTarget(View view, float radiusRatio, float clipRatio) {
        setTarget(view, 1.25f, 1.5f, radiusRatio, clipRatio);
    }

    public void setTarget(View view, float widthRatio, float heightRatio, float radiusRatio, float clipRatio) {
        if (target == view)
            return;

        target = view;

        stopIndicatorAnimation();

        int width = ((ViewGroup) view.getParent()).getWidth();
        int height = ((ViewGroup) view.getParent()).getHeight();

        setLayoutParams(new ViewGroup.LayoutParams(width, height));

        parent.removeView(this);
        parent.addView(this);

        int[] locationView = new int[2];

        view.getLocationOnScreen(locationView);

        locationView[0] = locationView[0] - getCalculatedOffsetX(parent);
        locationView[1] = locationView[1] - getCalculatedOffsetY(parent);

        centerX = (locationView[0] + view.getMeasuredWidth() / 2f);
        centerY = (locationView[1] + view.getMeasuredHeight() / 2f);

        indicatorX = (locationView[0] + view.getMeasuredWidth() / 2f);
        indicatorY = (locationView[1] + view.getMeasuredHeight() / 2f);

        indicatorMinWidth = view.getWidth();
        indicatorMinHeight = view.getHeight();

        indicatorMaxWidth = indicatorMinWidth * widthRatio;
        indicatorMaxHeight = indicatorMinHeight * heightRatio;

        indicatorClipRadius = Math.max(indicatorMinWidth, indicatorMinHeight) * clipRatio;
        indicatorMinRadius = indicatorClipRadius;
        indicatorMaxRadius = (indicatorMinRadius * radiusRatio);

        setElevation(view.getElevation() - 1f);
        setTranslationZ(view.getTranslationZ() - 1f);
    }

    private int getCalculatedOffsetY(ViewGroup parent) {
        Property<Integer> property = new Property<>(parent.getTop());

        getCalculatedOffsetY(parent.getParent(), property);

        return property.getValue();
    }

    private int getCalculatedOffsetX(ViewGroup parent) {
        Property<Integer> property = new Property<>(parent.getLeft());

        getCalculatedOffsetX(parent.getParent(), property);

        return property.getValue();
    }

    private void getCalculatedOffsetY(ViewParent parent, Property<Integer> offset) {
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            offset.setValue(offset.getValue() + group.getTop());
            if (group.getParent() != null) {
                getCalculatedOffsetY(group.getParent(), offset);
            }
        }
    }

    private void getCalculatedOffsetX(ViewParent parent, Property<Integer> offset) {
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            offset.setValue(offset.getValue() + group.getLeft());
            if (group.getParent() != null) {
                getCalculatedOffsetX(group.getParent(), offset);
            }
        }
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public int getIndicatorStrokeColor() {
        return indicatorStrokeColor;
    }

    public void setIndicatorStrokeColor(int indicatorStrokeColor) {
        this.indicatorStrokeColor = indicatorStrokeColor;
    }

    public int getIndicatorColorStart() {
        return indicatorColorStart;
    }

    public void setIndicatorColorStart(int indicatorColorStart) {
        this.indicatorColorStart = indicatorColorStart;
    }

    public int getIndicatorInnerOutlineColor() {
        return indicatorInnerOutlineColor;
    }

    public void setIndicatorInnerOutlineColor(int indicatorInnerOutlineColor) {
        this.indicatorInnerOutlineColor = indicatorInnerOutlineColor;
    }

    public int getIndicatorColorEnd() {
        return indicatorColorEnd;
    }

    public void setIndicatorColorEnd(int indicatorColorEnd) {
        this.indicatorColorEnd = indicatorColorEnd;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    public boolean isShowBorderStroke() {
        return showBorderStroke;
    }

    public void setShowBorderStroke(boolean showBorderStroke) {
        this.showBorderStroke = showBorderStroke;
    }

    public boolean isShowInnerOutline() {
        return showInnerOutline;
    }

    public void setShowInnerOutline(boolean showInnerOutline) {
        this.showInnerOutline = showInnerOutline;
    }

    public float getInnerOutLineWidth() {
        return innerOutLineWidth;
    }

    public void setInnerOutLineWidth(float innerOutLineWidth) {
        this.innerOutLineWidth = innerOutLineWidth;
    }

    public int getIndicatorIntervalDelay() {
        return indicatorIntervalDelay;
    }

    public void setIndicatorIntervalDelay(int indicatorIntervalDelay) {
        this.indicatorIntervalDelay = indicatorIntervalDelay;
    }

    public long getRevealDuration() {
        return revealDuration;
    }

    public void setRevealDuration(long revealDuration) {
        this.revealDuration = revealDuration;
    }

    public long getConcealDuration() {
        return concealDuration;
    }

    public void setConcealDuration(long concealDuration) {
        this.concealDuration = concealDuration;
    }

    public void setTarget(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setDrawListener(ViewDrawListener listener) {
        this.listener = listener;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getIndicatorMinWidth() {
        return indicatorMinWidth;
    }

    public void setIndicatorMinWidth(float indicatorMinWidth) {
        this.indicatorMinWidth = indicatorMinWidth;
    }

    public float getIndicatorMinHeight() {
        return indicatorMinHeight;
    }

    public void setIndicatorMinHeight(float indicatorMinHeight) {
        this.indicatorMinHeight = indicatorMinHeight;
    }

    public float getIndicatorX() {
        return indicatorX;
    }

    public void setIndicatorX(float x) {
        this.indicatorX = x;
    }

    public float getIndicatorY() {
        return indicatorY;
    }

    public void setIndicatorY(float y) {
        this.indicatorY = y;
    }

    public float getIndicatorMaxWidth() {
        return indicatorMaxWidth;
    }

    public void setIndicatorMaxWidth(float indicatorMaxWidth) {
        this.indicatorMaxWidth = indicatorMaxWidth;
    }

    public float getIndicatorMaxHeight() {
        return indicatorMaxHeight;
    }

    public void setIndicatorMaxHeight(float indicatorMaxHeight) {
        this.indicatorMaxHeight = indicatorMaxHeight;
    }

    public boolean isUseColorInterpolation() {
        return useColorInterpolation;
    }

    public void setUseColorInterpolation(boolean useColorInterpolation) {
        this.useColorInterpolation = useColorInterpolation;
    }

    public Runnable getOnEnd() {
        return onEnd;
    }

    public void setOnEnd(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    public Runnable getOnStart() {
        return onStart;
    }

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public boolean isCleanUpAfter() {
        return cleanUpAfter;
    }

    public void setCleanUpAfter(boolean cleanUpAfter) {
        this.cleanUpAfter = cleanUpAfter;
    }

    public int getIndicatorShape() {
        return indicatorShape;
    }

    public void setIndicatorShape(int indicatorShape) {
        this.indicatorShape = indicatorShape;
    }

    public float getIndicatorCornerRadius() {
        return indicatorCornerRadius;
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.indicatorCornerRadius = indicatorCornerRadius;
    }

    public float getIndicatorStrokeWidth() {
        return indicatorStrokeWidth;
    }

    public void setIndicatorStrokeWidth(float indicatorStrokeWidth) {
        this.indicatorStrokeWidth = indicatorStrokeWidth;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        this.color.setColor(indicatorColor);
    }

    public int getIndicatorRepeats() {
        return indicatorRepeats;
    }

    public void setIndicatorRepeats(int indicatorRepeats) {
        this.indicatorRepeats = indicatorRepeats;
    }

    public int getIndicatorRepeatMode() {
        return indicatorRepeatMode;
    }

    public void setIndicatorRepeatMode(int indicatorRepeatMode) {
        this.indicatorRepeatMode = indicatorRepeatMode;
    }

    public int getIndicatorDuration() {
        return indicatorDuration;
    }

    public void setIndicatorDuration(int indicatorDuration) {
        this.indicatorDuration = indicatorDuration;
    }

    public float getIndicatorMinOpacity() {
        return indicatorMinOpacity;
    }

    public void setIndicatorMinOpacity(float indicatorMinOpacity) {
        this.indicatorMinOpacity = indicatorMinOpacity;
    }

    public float getIndicatorMaxOpacity() {
        return indicatorMaxOpacity;
    }

    public void setIndicatorMaxOpacity(float indicatorMaxOpacity) {
        this.indicatorMaxOpacity = indicatorMaxOpacity;
    }

    public float getIndicatorClipRadius() {
        return indicatorClipRadius;
    }

    public void setIndicatorClipRadius(float indicatorClipRadius) {
        this.indicatorClipRadius = indicatorClipRadius;
    }

    public float getIndicatorMaxRadius() {
        return indicatorMaxRadius;
    }

    public void setIndicatorMaxRadius(float indicatorMaxRadius) {
        this.indicatorMaxRadius = indicatorMaxRadius;
    }

    public float getIndicatorMinRadius() {
        return indicatorMinRadius;
    }

    public void setIndicatorMinRadius(float indicatorMinRadius) {
        this.indicatorMinRadius = indicatorMinRadius;
    }

    public int getIndicatorCount() {
        return indicatorCount;
    }

    public void setIndicatorCount(int indicatorCount) {
        if (indicatorCount == this.indicatorCount)
            return;

        this.indicatorCount = indicatorCount;
    }

    public int getUsableWidth() {
        return usableWidth;
    }

    public int getUsableHeight() {
        return usableHeight;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public interface ViewDrawListener {
        void onViewDrawn(View view);
    }
}