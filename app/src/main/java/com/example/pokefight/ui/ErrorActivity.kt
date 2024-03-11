package com.example.pokefight.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pokefight.R

class ErrorActivity : AppCompatActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.connectionLost()
        val retryBtn = findViewById<Button>(R.id.error_btn_retry)
        retryBtn.setOnClickListener {
            val i = Intent(applicationContext, SplashScreen::class.java)
            startActivity(i)
            finish()
        }
    }
}