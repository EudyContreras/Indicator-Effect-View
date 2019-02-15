package com.eudycontreras.indicatoreffect

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eudycontreras.indicatoreffectlib.utilities.DimensionUtility
import com.eudycontreras.indicatoreffectlib.views.IndicatorView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var indicator: IndicatorView
    private val interpolatorIn = DecelerateInterpolator()
    private val interpolatorOut = OvershootInterpolator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        indicator = IndicatorView(this, findViewById<ViewGroup>(R.id.root))
        indicator.indicatorType = IndicatorView.INDICATOR_TYPE_AROUND
        indicator.indicatorColor = ContextCompat.getColor(this, R.color.colorAccent)
        indicator.indicatorStrokeColor = ContextCompat.getColor(this, R.color.colorAccent)
        indicator.indicatorColorStart = ContextCompat.getColor(this, R.color.white)
        indicator.indicatorColorEnd = ContextCompat.getColor(this, R.color.colorAccent)
        indicator.indicatorCount = 3
        indicator.indicatorMinOpacity = 0f
        indicator.indicatorMaxOpacity = 1f
        indicator.indicatorRepeatMode = IndicatorView.REPEAT_MODE_RESTART
        indicator.indicatorRepeats = IndicatorView.INFINITE_REPEATS
        indicator.indicatorDuration = 7000
        indicator.indicatorStrokeWidth = 0f
        indicator.isShowBorderStroke = false
        indicator.revealDuration = 0
        indicator.isUseColorInterpolation = false
        indicator.offsetY = DimensionUtility.convertPixelsToDp(this, 10f)

    }

    override fun onResume() {
        super.onResume()
        val view: View = findViewById(R.id.someElement)

        someElementContainer.post {

            someElementContainer.pivotX = someElementContainer.measuredWidth / 2f
            someElementContainer.pivotY = someElementContainer.measuredHeight / 2f
            someElementContainer.scaleX = 0.75f
            someElementContainer.scaleY = 0.75f
            someElementContainer.translationZ = 0f
            Handler().postDelayed({
                someElementContainer.animate()
                    .setInterpolator(interpolatorOut)
                    .translationZ(DimensionUtility.convertDpToPixel(this, DimensionUtility.convertPixelsToDp(this,8f)))
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(500L)
                    .setListener(object:  Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) { }

                        override fun onAnimationEnd(p0: Animator?) {
                            if (!indicator.isAnimationRunning) {
                                indicator.setTarget(someElementContainer, 2.05f, 0.45f)
                                indicator.startIndicatorAnimation(0)

                            }
                        }

                        override fun onAnimationCancel(p0: Animator?) {}

                        override fun onAnimationStart(p0: Animator?) {}
                    })
                    .start()
            },1000)

        }

        view.setOnClickListener {
            Log.d("CLICKED", "CLICKED")
            indicator.startIndicatorAnimation()
        }

        view.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorIn)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,0f))
                        .scaleY(0.93f)
                        .scaleX(0.93f)
                        .setDuration(150L)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,8f))
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150L)
                        .start()
                    view.callOnClick()
                }
                MotionEvent.ACTION_MOVE -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,8f))
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150L)
                        .start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,8f))
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150L)
                        .start()
                }
                else -> {}
            }
            false
        }
    }
}
