package com.eudycontreras.rippleeffectlib.views;

import android.animation.*;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.eudycontreras.rippleeffectlib.Bounds;
import com.eudycontreras.rippleeffectlib.Property;
import com.eudycontreras.rippleeffectlib.R;
import com.eudycontreras.rippleeffectlib.particles.ParticleRipple;
import com.eudycontreras.rippleeffectlib.utilities.ColorUtility;
import com.eudycontreras.rippleeffectlib.utilities.DimensionUtility;

import java.util.ArrayList;

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
public class RippleView extends View {

    public static final int RIPPLE_CIRCLE = 0;
    public static final int RIPPLE_RECTANGLE = 1;

    public static final int RIPPLE_TYPE_OUTLINE = 0;
    public static final int RIPPLE_TYPE_FILLED = 1;
    public static final int RIPPLE_TYPE_INDICATOR = 2;

    public static final int INFINITE_REPEATS = ObjectAnimator.INFINITE;

    public static final int REPEAT_RESTART_MODE = ObjectAnimator.RESTART;
    public static final int REPEAT_REVERSE_MODE = ObjectAnimator.REVERSE;

    private int backgroundColor = Color.TRANSPARENT;

    private int rippleShape = RIPPLE_CIRCLE;
    private int rippleType;
    private int rippleCount;
    private int rippleColor;
    private int rippleStrokeColor;
    private int rippleColorStart;
    private int rippleColorEnd;
    private int rippleDelay;
    private int rippleRepeats;
    private int rippleRepeatMode;
    private int rippleDuration;
    private int rippleIntervalDelay;

    private int usableWidth;
    private int usableHeight;

    private float rippleX = Integer.MIN_VALUE;
    private float rippleY = Integer.MIN_VALUE;

    private float centerX = Integer.MIN_VALUE;
    private float centerY = Integer.MIN_VALUE;

    private float offsetX = 0f;
    private float offsetY = 0f;

    private float rippleMinWidth;
    private float rippleMinHeight;

    private float rippleMaxWidth;
    private float rippleMaxHeight;

    private float rippleMinOpacity;
    private float rippleMaxOpacity;

    private float rippleClipRadius;
    private float rippleMaxRadius;
    private float rippleMinRadius;

    private float rippleCornerRadius;

    private float rippleStrokeWidth;

    private long revealDuration = 300;
    private long concealDuration = 300;

    private boolean useColorInterpolation = false;
    private boolean showBorderStroke = false;
    private boolean animationRunning = false;
    private boolean autoStartRipple = false;
    private boolean cleanUpAfter = false;

    private ParticleRipple[] ripples;
    private ArrayList<Animator> animators;

    private Runnable onEnd;
    private Runnable onStart;
    private Runnable runLater;
    private ViewDrawListener listener;

    private Handler handler;
    private AnimatorSet animatorSet;
    private ViewGroup parent;
    private Bounds bounds;
    private Paint paint;
    private ColorUtility.SoulColor color;
    private ColorUtility.SoulColor strokeColor;
    private ColorUtility.SoulColor colorStart;
    private ColorUtility.SoulColor colorEnd;

    private View target;

    public interface ViewDrawListener{
        void onViewDraw(View view);
    }

    public RippleView(Context context) {
        super(context);
        initialize();
    }

    public RippleView(Context context, @NonNull ViewGroup parent){
        super(context);
        initialize();
        runLater = ()-> setUpWith(parent);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        try {
            setUpAttributes(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    private void initialize() {
        paint = new Paint();
        paint.setAntiAlias(true);

        color =  new ColorUtility.SoulColor();

        handler = new Handler();

        animatorSet = new AnimatorSet();
        animators = new ArrayList<>();
    }

    public void setUpAttributes(TypedArray typedArray) {
        if (typedArray != null) {
            rippleType = typedArray.getInt(R.styleable.RippleView_rv_rippleType, RIPPLE_TYPE_FILLED);
            rippleCount = typedArray.getInt(R.styleable.RippleView_rv_rippleCount, 3);
            rippleDuration = typedArray.getInt(R.styleable.RippleView_rv_rippleDuration, 2000);
            rippleIntervalDelay = typedArray.getInt(R.styleable.RippleView_rv_intervalDelay, 0);
            rippleColor = typedArray.getColor(R.styleable.RippleView_rv_rippleColor, 0xFFFFFFFF);
            rippleStrokeColor = typedArray.getColor(R.styleable.RippleView_rv_rippleStrokeColor, 0xFFFFFFFF);
            rippleColorStart = typedArray.getColor(R.styleable.RippleView_rv_rippleColorStart, 0xFFFFFFFF);
            rippleColorEnd = typedArray.getColor(R.styleable.RippleView_rv_rippleColorEnd, 0xFFFFFFFF);
            rippleMinOpacity = typedArray.getFloat(R.styleable.RippleView_rv_rippleMinOpacity, 0f);
            rippleMaxOpacity = typedArray.getFloat(R.styleable.RippleView_rv_rippleMaxOpacity, 1f);
            rippleClipRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleClipRadius, 0f);
            rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleView_rv_rippleStrokeWidth, DimensionUtility.convertDpToPixel(getContext(), 2.5f));
            rippleMinRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleMinRadius, 0);
            rippleMaxRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleMaxRadius, DimensionUtility.convertDpToPixel(getContext(),30));
            rippleRepeats = typedArray.getInt(R.styleable.RippleView_rv_rippleRepeatCount, ObjectAnimator.INFINITE);
            rippleRepeatMode = typedArray.getInt(R.styleable.RippleView_rv_rippleRepeatMode, 0);
            autoStartRipple = typedArray.getBoolean(R.styleable.RippleView_rv_autoStartAnimation, false);
            useColorInterpolation = typedArray.getBoolean(R.styleable.RippleView_rv_useColorInterpolation, false);
            showBorderStroke = typedArray.getBoolean(R.styleable.RippleView_rv_showBorderStroke, false);
            rippleShape = typedArray.getInt(R.styleable.RippleView_rv_rippleShapeType, RIPPLE_CIRCLE);
            rippleCornerRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleCornerRadius, 0f);
            rippleMinWidth = typedArray.getDimension(R.styleable.RippleView_rv_rippleMinWidth, 0f);
            rippleMinHeight = typedArray.getDimension(R.styleable.RippleView_rv_rippleMinHeight, 0f);
            rippleMaxWidth = typedArray.getDimension(R.styleable.RippleView_rv_rippleMaxWidth, DimensionUtility.convertDpToPixel(getContext(),60));
            rippleMaxHeight = typedArray.getDimension(R.styleable.RippleView_rv_rippleMaxHeight, DimensionUtility.convertDpToPixel(getContext(),30));
            initializeRipple();
        }
    }

    public void setUpWith(@NonNull ViewGroup parent){
        this.parent = parent;
        initializeRipple();
    }

    private void initializeRipple() {
        setAlpha(0);

        rippleDelay = rippleDuration/rippleCount;
        ripples = new ParticleRipple[rippleCount];

        for(Animator animator : animators) {
            animator.cancel();
            animator.end();
        }

        animators.clear();
        color.setColor(rippleColor);

        if(useColorInterpolation) {
            colorStart =  new ColorUtility.SoulColor();
            colorEnd =  new ColorUtility.SoulColor();
            colorStart.setColor(rippleColorStart);
            colorEnd.setColor(rippleColorEnd);
        }else{
            colorStart = null;
            colorEnd = null;
        }

        if(showBorderStroke) {
            strokeColor = new ColorUtility.SoulColor();
            strokeColor.setColor(rippleStrokeColor);
        }else{
            strokeColor = null;
        }

        for(int i = 0; i< ripples.length; i++){
            ParticleRipple ripple = new ParticleRipple();
            ripple.setPaint(new Paint());
            ripple.setShapeType(rippleShape);
            ripple.setCornerRadius(rippleCornerRadius);
            ripple.setMinRadius(rippleMinRadius);
            ripple.setMaxRadius(rippleMaxRadius);
            ripple.setMinOpacity(rippleMinOpacity);
            ripple.setMaxOpacity(rippleMaxOpacity);
            ripple.setClipRadius(rippleClipRadius);
            ripple.setStrokeWidth(rippleStrokeWidth);
            ripple.setMinWidth(rippleMinWidth);
            ripple.setMinHeight(rippleMinHeight);
            ripple.setMaxWidth(rippleMaxWidth);
            ripple.setMaxHeight(rippleMaxHeight);
            ripple.setColor(color);
            ripple.setStrokeColor(strokeColor);
            ripple.setColorStart(colorStart);
            ripple.setColorEnd(colorEnd);
            ripple.setType(rippleType);
            ripple.setX(rippleX + offsetX);
            ripple.setY(rippleY + offsetY);
            ripple.setCenterX(centerX + offsetX);
            ripple.setCenterY(centerY + offsetY);
            ripple.setVisible(true);
            ripple.setAlwaysAlive(true);
            ripple.init();

            ripples[i] = ripple;

            final int index = i;
            final ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);

            animator.setRepeatCount(rippleRepeats);
            animator.setRepeatMode(rippleRepeatMode);
            animator.setStartDelay((long)(index * rippleDelay));
            animator.setDuration(rippleDuration);
            animator.addUpdateListener(animation -> {
                if(ripples[index] == null)
                    return;

                ripples[index].setShapeType(rippleShape);
                ripples[index].setColor(color);
                ripples[index].setColorStart(colorStart);
                ripples[index].setColorEnd(colorEnd);
                ripples[index].setStrokeColor(strokeColor);
                ripples[index].setCornerRadius(rippleCornerRadius);
                ripples[index].setMinOpacity(rippleMinOpacity);
                ripples[index].setMaxOpacity(rippleMaxOpacity);
                ripples[index].setMinRadius(rippleMinRadius);
                ripples[index].setMaxRadius(rippleMaxRadius);
                ripples[index].setClipRadius(rippleClipRadius);
                ripples[index].setStrokeWidth(rippleStrokeWidth);
                ripples[index].setCenterX(centerX + offsetX);
                ripples[index].setCenterY(centerY + offsetY);
                ripples[index].setX(rippleX + offsetX);
                ripples[index].setY(rippleY + offsetY);
                ripples[index].setMinWidth(rippleMinWidth);
                ripples[index].setMinHeight(rippleMinHeight);
                ripples[index].setMaxWidth(rippleMaxWidth);
                ripples[index].setMaxHeight(rippleMaxHeight);
                ripples[index].update(rippleDuration,(float)animation.getAnimatedValue());

                if(!ripples[index].isAlive()){
                    ripples[index] = null;
                }
                invalidate();
            });

            animators.add(animator);
        }

        if(animators.get(0).getListeners() != null){
            animators.get(0).getListeners().clear();
        }
        if(animators.get(animators.size()-1).getListeners() != null){
            animators.get(animators.size()-1).getListeners().clear();
        }

        animators.get(0).addListener(startListener);
        animators.get(animators.size()-1).addListener(endListener);
    }

    public void startRippleAnimation() {
        startRippleAnimation(0);
    }

    public void startRippleAnimation(int rippleDelay) {
        startRippleAnimation(rippleDelay, new AccelerateDecelerateInterpolator());
    }

    public void startRippleAnimation(int rippleDelay, Interpolator interpolator) {
        handler.postDelayed(()-> {
            animatorSet.end();
            animatorSet.cancel();
            animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(interpolator);
            animatorSet.playTogether(animators);
            animatorSet.start();
            animationRunning=true;

        }, rippleDelay);
    }

    public void stopRippleAnimation() {
        stopRippleAnimation(concealDuration);
    }

    public void stopRippleAnimation(long duration) {
        if(animationRunning){
          dismiss(duration);
        }
    }

    private AnimatorListenerAdapter startListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            show(revealDuration);
        }
    };


    private AnimatorListenerAdapter endListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            dismiss(concealDuration);
        }
    };

    private void show(long duration){
        ValueAnimator showAnimator = ValueAnimator.ofFloat(0f, 1f);
        showAnimator.addUpdateListener(animation ->
            this.setAlpha((float) animation.getAnimatedValue())
        );
        showAnimator.setDuration(duration);
        showAnimator.setInterpolator(new DecelerateInterpolator());
        showAnimator.start();

        if(onStart != null){
            onStart.run();
        }
    }

    private void dismiss(long duration){
        ValueAnimator showAnimator = ValueAnimator.ofFloat(1f, 0f);
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.end();
                animatorSet.cancel();
                animationRunning=false;
                if(onEnd != null){
                    onEnd.run();
                }
            }
        });
        showAnimator.addUpdateListener(animation -> {
            this.setAlpha((float) animation.getAnimatedValue());
        });
        showAnimator.setDuration(duration);
        showAnimator.setInterpolator(new LinearInterpolator());
        showAnimator.start();
    }

    public void removeRipple(){
        stopRippleAnimation();
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

        if(centerX == Integer.MIN_VALUE){
            centerX = paddingLeft + (usableWidth / 2f);
        }

        if(centerY == Integer.MIN_VALUE){
            centerY = paddingTop + (usableHeight / 2f);
        }

        if(rippleX == Integer.MIN_VALUE){
            rippleX = paddingLeft + (usableWidth / 2f);
        }

        if(rippleY == Integer.MIN_VALUE){
            rippleY = paddingTop + (usableHeight / 2f);
        }

        bounds = new Bounds(0, 0, usableWidth, usableHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);

        if(bounds == null) {

            initializeValues();

            for (ParticleRipple ripple : ripples) {
                if(ripple == null)
                    return;

                ripple.setBounds(bounds);
                ripple.setColor(color);
                ripple.setStrokeColor(strokeColor);
                ripple.setColorStart(colorStart);
                ripple.setColorEnd(colorEnd);
                ripple.setCornerRadius(rippleCornerRadius);
                ripple.setMinOpacity(rippleMinOpacity);
                ripple.setMaxOpacity(rippleMaxOpacity);
                ripple.setMinRadius(rippleMinRadius);
                ripple.setMaxRadius(rippleMaxRadius);
                ripple.setClipRadius(rippleClipRadius);
                ripple.setStrokeWidth(rippleStrokeWidth);
                ripple.setX(rippleX + offsetX);
                ripple.setY(rippleY + offsetY);
                ripple.setCenterX(centerX + offsetX);
                ripple.setCenterY(centerY + offsetY);
                ripple.setMaxWidth(rippleMaxWidth);
                ripple.setMaxHeight(rippleMaxHeight);
                ripple.setMinWidth(rippleMinWidth);
                ripple.setMinHeight(rippleMinHeight);
                ripple.update();
                ripple.init();
            }

            if(listener != null){
                listener.onViewDraw(this);
            }

            if(autoStartRipple) {
                startRippleAnimation();
            }

            invalidate();
        }

        for (ParticleRipple ripple : ripples) {
            if (ripple != null) {
                ripple.draw(canvas);
            }
        }
    }

    public void setTarget(View view){
        setTarget(view,2);
    }

    public void setTarget(View view, float radiusRatio){
        setTarget(view,1.25f,1.5f, radiusRatio, 0.5f);
    }

    public void setTarget(View view, float radiusRatio, float clipRatio){
        setTarget(view,1.25f,1.5f, radiusRatio, clipRatio);
    }

    public void setTarget(View view, float widthRatio, float heightRatio, float radiusRatio, float clipRatio){
        if(target == view)
            return;

        if(runLater != null){
            runLater.run();
        }

        target = view;

        stopRippleAnimation();

        parent.removeView(this);
        parent.addView(this);

        if(getLayoutParams() instanceof ConstraintLayout.LayoutParams){
            setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        }else if (getLayoutParams() instanceof LinearLayout.LayoutParams){
            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }else if (getLayoutParams() instanceof FrameLayout.LayoutParams){
            setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }else if (getLayoutParams() instanceof RelativeLayout.LayoutParams){
            setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }else{
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        int[] locationView = new int[2];
        int[] locationParent = new int[2];

        view.getLocationOnScreen(locationView);
        parent.getLocationOnScreen(locationParent);

        locationView[0] = locationView[0] - getCalculatedOffsetX(parent);
        locationView[1] = locationView[1] - getCalculatedOffsetY(parent);

        centerX = (locationView[0] + view.getWidth()  / 2f);
        centerY = (locationView[1] + view.getHeight() / 2f) - DimensionUtility.convertDpToPixel(getContext(), 24) ;

        rippleX = (locationView[0] + view.getWidth()  / 2f);
        rippleY = (locationView[1] + view.getHeight() / 2f) - DimensionUtility.convertDpToPixel(getContext(), 24) ;

        rippleMinWidth = view.getWidth();
        rippleMinHeight = view.getHeight();

        rippleMaxWidth = rippleMinWidth * widthRatio;
        rippleMaxHeight = rippleMinHeight * heightRatio;

        rippleClipRadius = Math.max(rippleMinWidth, rippleMinHeight) * clipRatio;
        rippleMinRadius = rippleClipRadius;
        rippleMaxRadius = rippleMinRadius * radiusRatio;

        setElevation(view.getElevation()-1f);
        setTranslationZ(view.getTranslationZ()-1f);
    }

    private int getCalculatedOffsetY(ViewGroup parent) {
        Property<Integer> property = new Property<>(parent.getTop());

        //getCalculatedOffsetY(parent.getParent(), property);

        return property.getValue();
    }

    private int getCalculatedOffsetX(ViewGroup parent) {
        Property<Integer> property = new Property<>(parent.getLeft());

        //getCalculatedOffsetX(parent.getParent(), property);

        return property.getValue();
    }

    private void getCalculatedOffsetY(ViewParent parent, Property<Integer> offset) {
        if(parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;
            offset.setValue(offset.getValue()+group.getTop());
            if(group.getParent() != null){
                getCalculatedOffsetY(group.getParent(), offset);
            }
        }
    }

    private void getCalculatedOffsetX(ViewParent parent, Property<Integer> offset) {
        if(parent instanceof ViewGroup){
            ViewGroup group = (ViewGroup) parent;
            offset.setValue(offset.getValue()+group.getLeft());
            if(group.getParent() != null){
                getCalculatedOffsetX(group.getParent(), offset);
            }
        }
    }

    public void setOffsetX(float offsetX){
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY){
        this.offsetY = offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOnEnd(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public int getRippleStrokeColor() {
        return rippleStrokeColor;
    }

    public void setRippleStrokeColor(int rippleStrokeColor) {
        this.rippleStrokeColor = rippleStrokeColor;
    }

    public int getRippleColorStart() {
        return rippleColorStart;
    }

    public void setRippleColorStart(int rippleColorStart) {
        this.rippleColorStart = rippleColorStart;
    }

    public int getRippleColorEnd() {
        return rippleColorEnd;
    }

    public void setRippleColorEnd(int rippleColorEnd) {
        this.rippleColorEnd = rippleColorEnd;
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public boolean isShowBorderStroke() {
        return showBorderStroke;
    }

    public void setShowBorderStroke(boolean showBorderStroke) {
        this.showBorderStroke = showBorderStroke;
    }

    public int getRippleIntervalDelay() {
        return rippleIntervalDelay;
    }

    public void setRippleIntervalDelay(int rippleIntervalDelay) {
        this.rippleIntervalDelay = rippleIntervalDelay;
    }

    public void setTarget(int centerX, int centerY){
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

    public float getRippleMinWidth() {
        return rippleMinWidth;
    }

    public void setRippleMinWidth(float rippleMinWidth) {
        this.rippleMinWidth = rippleMinWidth;
    }

    public float getRippleMinHeight() {
        return rippleMinHeight;
    }

    public void setRippleMinHeight(float rippleMinHeight) {
        this.rippleMinHeight = rippleMinHeight;
    }

    public float getRippleX() {
        return rippleX;
    }

    public void setRippleX(float x) {
        this.rippleX = x;
    }

    public float getRippleY() {
        return rippleY;
    }

    public void setRippleY(float y) {
        this.rippleY = y;
    }

    public float getRippleMaxWidth() {
        return rippleMaxWidth;
    }

    public void setRippleMaxWidth(float rippleMaxWidth) {
        this.rippleMaxWidth = rippleMaxWidth;
    }

    public float getRippleMaxHeight() {
        return rippleMaxHeight;
    }

    public void setRippleMaxHeight(float rippleMaxHeight) {
        this.rippleMaxHeight = rippleMaxHeight;
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

    public Runnable getOnStart() {
        return onStart;
    }

    public boolean isCleanUpAfter() {
        return cleanUpAfter;
    }

    public void setCleanUpAfter(boolean cleanUpAfter) {
        this.cleanUpAfter = cleanUpAfter;
    }

    public void setRippleCornerRadius(float rippleCornerRadius) {
        this.rippleCornerRadius = rippleCornerRadius;
    }

    public void setRippleShape(int rippleShape) {
        this.rippleShape = rippleShape;
    }

    public int getRippleShape() {
        return rippleShape;
    }

    public float getRippleCornerRadius() {
        return rippleCornerRadius;
    }

    public void setRippleStrokeWidth(float rippleStrokeWidth) {
        this.rippleStrokeWidth = rippleStrokeWidth;
    }

    public float getRippleStrokeWidth() {
        return rippleStrokeWidth;
    }

    public int getRippleType() {
        return rippleType;
    }

    public void setRippleType(int rippleType) {
        this.rippleType = rippleType;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        this.color.setColor(rippleColor);
    }

    public int getRippleRepeats() {
        return rippleRepeats;
    }

    public void setRippleRepeats(int rippleRepeats) {
        this.rippleRepeats = rippleRepeats;
    }

    public int getRippleRepeatMode() {
        return rippleRepeatMode;
    }

    public void setRippleRepeatMode(int rippleRepeatMode) {
        this.rippleRepeatMode = rippleRepeatMode;
    }

    public int getRippleDuration() {
        return rippleDuration;
    }

    public float getRippleMinOpacity() {
        return rippleMinOpacity;
    }

    public void setRippleMinOpacity(float rippleMinOpacity) {
        this.rippleMinOpacity = rippleMinOpacity;
    }

    public float getRippleMaxOpacity() {
        return rippleMaxOpacity;
    }

    public void setRippleMaxOpacity(float rippleMaxOpacity) {
        this.rippleMaxOpacity = rippleMaxOpacity;
    }

    public float getRippleClipRadius() {
        return rippleClipRadius;
    }

    public void setRippleClipRadius(float rippleClipRadius) {
        this.rippleClipRadius = rippleClipRadius;
    }

    public float getRippleMaxRadius() {
        return rippleMaxRadius;
    }

    public void setRippleMaxRadius(float rippleMaxRadius) {
        this.rippleMaxRadius = rippleMaxRadius;
    }

    public float getRippleMinRadius() {
        return rippleMinRadius;
    }

    public void setRippleMinRadius(float rippleMinRadius) {
        this.rippleMinRadius = rippleMinRadius;
    }

    public int getRippleCount() {
        return rippleCount;
    }

    public void setRippleCount(int rippleCount) {
        if(rippleCount == this.rippleCount)
            return;

        this.rippleCount = rippleCount;
        this.initializeRipple();
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
}