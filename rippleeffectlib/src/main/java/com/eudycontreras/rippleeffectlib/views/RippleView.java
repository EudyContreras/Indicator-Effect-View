package com.eudycontreras.rippleeffectlib.views;

import android.animation.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.eudycontreras.rippleeffectlib.Bounds;
import com.eudycontreras.rippleeffectlib.particles.ParticleRipple;
import com.eudycontreras.rippleeffectlib.R;
import com.eudycontreras.rippleeffectlib.utilities.ColorUtility;
import com.eudycontreras.rippleeffectlib.utilities.DimensionUtility;

import java.util.ArrayList;
import java.util.Objects;

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
    private int rippleDelay;
    private int rippleRepeats;
    private int rippleRepeatMode;
    private int rippleDuration;

    private int usableWidth;
    private int usableHeight;

    private float rippleX = Integer.MIN_VALUE;
    private float rippleY = Integer.MIN_VALUE;

    private float centerX = Integer.MIN_VALUE;
    private float centerY = Integer.MIN_VALUE;

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

    private boolean animationRunning;
    private boolean autoStartRipple;
    private boolean cleanUpAfter;

    private ParticleRipple[] ripples;
    private ArrayList<Animator> animators;

    private Runnable onEnd;
    private Runnable onStart;
    private Runnable runLater;
    private ViewDrawListener listener;

    private AnimatorSet animatorSet;
    private ViewGroup parent;
    private Bounds bounds;
    private Paint paint;
    private ColorUtility.SoulColor color;
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
        color =  new ColorUtility.SoulColor();
        paint.setAntiAlias(true);
        animatorSet = new AnimatorSet();
        animators = new ArrayList<>();

    }

    public void setUpAttributes(TypedArray typedArray) {
        if (typedArray != null) {
            rippleType = typedArray.getInt(R.styleable.RippleView_rv_rippleType, RIPPLE_TYPE_FILLED);
            rippleCount = typedArray.getInt(R.styleable.RippleView_rv_rippleCount, 3);
            rippleDuration = typedArray.getInt(R.styleable.RippleView_rv_rippleDuration, 2000);
            rippleColor = typedArray.getColor(R.styleable.RippleView_rv_rippleColor, 0xFFFFFFFF);
            rippleMinOpacity = typedArray.getFloat(R.styleable.RippleView_rv_rippleMinOpacity, 0f);
            rippleMaxOpacity = typedArray.getFloat(R.styleable.RippleView_rv_rippleMaxOpacity, 1f);
            rippleClipRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleClipRadius, 0f);
            rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleView_rv_rippleStrokeWidth, DimensionUtility.convertDpToPixel(getContext(), 2.5f));
            rippleMinRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleMinRadius, 0);
            rippleMaxRadius = typedArray.getDimension(R.styleable.RippleView_rv_rippleMaxRadius, DimensionUtility.convertDpToPixel(getContext(),30));
            rippleRepeats = typedArray.getInt(R.styleable.RippleView_rv_rippleRepeatCount, ObjectAnimator.INFINITE);
            rippleRepeatMode = typedArray.getInt(R.styleable.RippleView_rv_rippleRepeatMode, 0);
            autoStartRipple = typedArray.getBoolean(R.styleable.RippleView_rv_autoStartAnimation, false);
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

        for(int i = 0; i< ripples.length; i++){
            ParticleRipple particle = new ParticleRipple();
            particle.setPaint(new Paint());
            particle.setShapeType(rippleShape);
            particle.setCornerRadius(rippleCornerRadius);
            particle.setMinRadius(rippleMinRadius);
            particle.setMaxRadius(rippleMaxRadius);
            particle.setMinOpacity(rippleMinOpacity);
            particle.setMaxOpacity(rippleMaxOpacity);
            particle.setClipRadius(rippleClipRadius);
            particle.setStrokeWidth(rippleStrokeWidth);
            particle.setMinWidth(rippleMinWidth);
            particle.setMinHeight(rippleMinHeight);
            particle.setMaxWidth(rippleMaxWidth);
            particle.setMaxHeight(rippleMaxHeight);
            particle.setColor(color);
            particle.setType(rippleType);
            particle.setCenterX(centerX);
            particle.setCenterY(centerY);
            particle.setVisible(true);
            particle.init();

            ripples[i] = particle;

            final int index = i;
            final ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);
            animator.setRepeatCount(rippleRepeats);
            animator.setRepeatMode(rippleRepeatMode);
            animator.setStartDelay((long)(i * rippleDelay));
            animator.setDuration(rippleDuration);
            animator.addUpdateListener(animation -> {
                if(ripples[index] == null)
                    return;

                ripples[index].setShapeType(rippleShape);
                ripples[index].setColor(color);
                ripples[index].setCornerRadius(rippleCornerRadius);
                ripples[index].setMinOpacity(rippleMinOpacity);
                ripples[index].setMaxOpacity(rippleMaxOpacity);
                ripples[index].setMinRadius(rippleMinRadius);
                ripples[index].setMaxRadius(rippleMaxRadius);
                ripples[index].setClipRadius(rippleClipRadius);
                ripples[index].setStrokeWidth(rippleStrokeWidth);
                ripples[index].setCenterX(centerX);
                ripples[index].setCenterY(centerY);
                ripples[index].setX(rippleX);
                ripples[index].setY(rippleY);
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
        animatorSet.end();
        animatorSet.cancel();
        if(!animationRunning){

            animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.playTogether(animators);
            animatorSet.start();
            animationRunning=true;
        }
    }

    public void stopRippleAnimation() {
        stopRippleAnimation(200);
    }

    public void stopRippleAnimation(int duration) {
        if(animationRunning){
            ValueAnimator showAnimator = ValueAnimator.ofFloat(1f, 0f);
            showAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorSet.end();
                    animatorSet.cancel();
                    animationRunning=false;
                }
            });
            showAnimator.addUpdateListener(animation -> {
                this.setAlpha((float) animation.getAnimatedValue());
            });
            showAnimator.setDuration(duration);
            showAnimator.setInterpolator(new LinearInterpolator());
            showAnimator.start();
        }
    }

    private AnimatorListenerAdapter startListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            show();
        }
    };


    private AnimatorListenerAdapter endListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            dismiss();
        }
    };

    private void show(){
        ValueAnimator showAnimator = ValueAnimator.ofFloat(0f, 1f);
        showAnimator.addUpdateListener(animation ->
            this.setAlpha((float) animation.getAnimatedValue())
        );
        showAnimator.setDuration(300);
        showAnimator.setInterpolator(new DecelerateInterpolator());
        showAnimator.start();

        if(onStart != null){
            onStart.run();
        }
    }

    private void dismiss(){
        animationRunning=false;
        if(onEnd != null){
            onEnd.run();
        }
    }

    public void removeRipple(){
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
                ripple.setBounds(bounds);
                ripple.setColor(color);
                ripple.setCornerRadius(rippleCornerRadius);
                ripple.setMinOpacity(rippleMinOpacity);
                ripple.setMaxOpacity(rippleMaxOpacity);
                ripple.setMinRadius(rippleMinRadius);
                ripple.setMaxRadius(rippleMaxRadius);
                ripple.setClipRadius(rippleClipRadius);
                ripple.setStrokeWidth(rippleStrokeWidth);
                ripple.setCenterX(centerX);
                ripple.setCenterY(centerY);
                ripple.setX(rippleX);
                ripple.setY(rippleY);
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
        }

        for (ParticleRipple ripple : ripples) {
            if (ripple != null) {
                ripple.draw(canvas);
            }
        }
    }

    public void setTarget(Context context, View view){
        setTarget(context,view,2);
    }

    public void setTarget(Context context, View view, float radiusRatio){
        setTarget(context, view,1.25f,1.5f, radiusRatio, 0.5f);
    }

    public void setTarget(Context context, View view, float radiusRatio, float clipRatio){
        setTarget(context, view,1.25f,1.5f, radiusRatio, clipRatio);
    }

    public void setTarget(Context context, View view, float widthRatio, float heightRatio, float radiusRatio, float clipRatio){
        if(target == view)
            return;

        if(runLater != null){
            runLater.run();
        }

        target = view;

        stopRippleAnimation();
        parent.removeView(this);
        parent.addView(this);
        setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        centerX = (location[0] + view.getWidth()  / 2f);
        centerY = (location[1] + view.getHeight() / 2f) - DimensionUtility.convertDpToPixel(context, 24) ;

        rippleX = (location[0] + view.getWidth()  / 2f);
        rippleY = (location[1] + view.getHeight() / 2f) - DimensionUtility.convertDpToPixel(context, 24) ;

        rippleMinWidth = view.getWidth();
        rippleMinHeight = view.getHeight();

        rippleMaxWidth = rippleMinWidth * widthRatio;
        rippleMaxHeight = rippleMinHeight * heightRatio;

        rippleClipRadius = Math.max(rippleMinWidth, rippleMinHeight) * clipRatio;
        rippleMinRadius = rippleClipRadius;
        rippleMaxRadius = rippleMinRadius * radiusRatio;

        setElevation(view.getElevation());
        setTranslationZ(view.getTranslationZ());
    }

    public void setOnEnd(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public void setTarget(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setTarget(Activity activity, int id){
        View view = activity.findViewById(id);
        setTarget(activity, view);
    }

    public void setTarget(AppCompatActivity activity, int id){
        View view = activity.findViewById(id);
        setTarget(activity, view);
    }

    public void setTarget(Fragment fragment, int id){
        View view = Objects.requireNonNull(fragment.getView()).findViewById(id);
        setTarget(fragment.getContext(),view);
    }

    public void setListener(ViewDrawListener listener) {
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