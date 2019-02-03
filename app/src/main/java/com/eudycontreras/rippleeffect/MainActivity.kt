package com.eudycontreras.rippleeffect

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eudycontreras.rippleeffectlib.views.RippleView

class MainActivity : AppCompatActivity() {

    private lateinit var ctaView: RippleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ctaView = RippleView(this, findViewById<ViewGroup>(R.id.root))
        ctaView.rippleType = RippleView.RIPPLE_TYPE_INDICATOR
        ctaView.rippleColor = ContextCompat.getColor(this, R.color.colorAccent)
        ctaView.rippleCount = 5
        ctaView.rippleMinOpacity = 0f
        ctaView.rippleMaxOpacity = 1f
        ctaView.rippleRepeatMode = RippleView.REPEAT_RESTART_MODE
        ctaView.rippleRepeats = RippleView.INFINITE_REPEATS
        ctaView.rippleDuration = 4000
    }

    override fun onPostResume() {
        super.onPostResume()
        val view: View = findViewById(R.id.someElement)
        Handler().postDelayed({
            view.post {
                ctaView.setTarget(this, view, 1.4f, 0.5f)
                //ctaView.stopRippleAnimation()
                ctaView.startRippleAnimation()

                Handler().postDelayed({
                    ctaView.stopRippleAnimation(2000)
                },3000)
            }
        }, 1000)
    }
}
