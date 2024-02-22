package com.example.pokefight.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.pokefight.BuildConfig
import com.example.pokefight.LoginActivity
import com.example.pokefight.R

class SplashScreen : AppCompatActivity() {
    val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val logo = this.findViewById<ImageView>(R.id.splash_logo)
        val buildModeText = findViewById<TextView>(R.id.build_mode)
        val background = findViewById<ConstraintLayout>(R.id.splash_background)
        if (BuildConfig.buildName == "dev") {
            buildModeText.text = "DEBUG"
            val colorRes = ContextCompat.getColor(this, R.color.md_theme_light_secondaryContainer)
            background.setBackgroundColor(colorRes)
        } else if (BuildConfig.buildName == "preproduction") {
            buildModeText.text = "PRE-PRODUCTION"
            val colorRes = ContextCompat.getColor(this, R.color.md_theme_light_tertiaryContainer)
            background.setBackgroundColor(colorRes)
        }
        applyReboundZoomAnimation(logo, 5)

        mainViewModel.checkNetworkConnection(this).observe(this) { isConnected ->
            if (isConnected) {
                mainViewModel.getPokemonList(1, 30).observe(this) {
                    val i = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            } else {
                val i = Intent(applicationContext, ErrorActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    fun applyReboundZoomAnimation(imageView: ImageView, repeatCount: Int) {
        val zoomFactor = 1.2f // Change this value according to your desired zoom level

        val zoomInAnimation = ScaleAnimation(
            1.0f, zoomFactor, // X-axis scale from 1.0 to zoomFactor
            1.0f, zoomFactor, // Y-axis scale from 1.0 to zoomFactor
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point (X-axis) - center
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point (Y-axis) - center
        )
        zoomInAnimation.duration = 500 // Duration of zoom-in animation

        val zoomOutAnimation = ScaleAnimation(
            zoomFactor, 1.0f, // X-axis scale from zoomFactor to 1.0
            zoomFactor, 1.0f, // Y-axis scale from zoomFactor to 1.0
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point (X-axis) - center
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point (Y-axis) - center
        )
        zoomOutAnimation.duration = 500 // Duration of zoom-out animation
        zoomOutAnimation.startOffset = 300 // Delay before starting zoom-out animation

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(zoomInAnimation)
        animationSet.addAnimation(zoomOutAnimation)

        animationSet.repeatCount = repeatCount // Set the number of times to repeat the animation
        imageView.startAnimation(animationSet)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Animation has ended, restart the animation
                imageView.startAnimation(animationSet)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}