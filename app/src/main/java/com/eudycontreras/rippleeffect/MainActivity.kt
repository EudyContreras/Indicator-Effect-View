package com.eudycontreras.rippleeffect

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
import com.eudycontreras.rippleeffectlib.utilities.DimensionUtility
import com.eudycontreras.rippleeffectlib.views.RippleView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var ripple: RippleView
    private val interpolatorIn = DecelerateInterpolator()
    private val interpolatorOut = OvershootInterpolator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ripple = RippleView(this, findViewById<ViewGroup>(R.id.root))
        ripple.rippleType = RippleView.RIPPLE_TYPE_INDICATOR
        ripple.rippleColor = ContextCompat.getColor(this, R.color.colorAccent)
        ripple.rippleCount = 1
        ripple.rippleMinOpacity = 0f
        ripple.rippleMaxOpacity = 1f
        ripple.rippleRepeatMode = RippleView.REPEAT_RESTART_MODE
        ripple.rippleRepeats = 20
        ripple.rippleDuration = 2000
        ripple.rippleStrokeWidth = 20f
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
                    .translationZ(DimensionUtility.convertDpToPixel(this,12f))
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(150L)
                    .setListener(object:  Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            ripple.setTarget(someElementContainer, 2.65f, 0.47f)
                            ripple.startRippleAnimation()
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationStart(p0: Animator?) {

                        }
                    })
                    .start()
            },1000)

        }

        view.setOnClickListener {
            Log.d("CLICKED", "CLICKED")
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
                    view.callOnClick()
                }
                MotionEvent.ACTION_UP -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,12f))
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150L)
                        .start()
                }
                MotionEvent.ACTION_MOVE -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .setListener(null)
                        .translationZ(DimensionUtility.convertDpToPixel(this,12f))
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150L)
                        .start()
                }
                MotionEvent.ACTION_CANCEL -> {
                    someElementContainer.animate()
                        .setInterpolator(interpolatorOut)
                        .translationZ(DimensionUtility.convertDpToPixel(this,12f))
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
