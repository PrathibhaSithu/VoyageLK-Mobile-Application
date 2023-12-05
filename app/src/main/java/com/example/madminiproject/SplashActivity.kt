package com.example.madminiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display the splash screen layout
        setContentView(R.layout.activity_splash)

        // Launch the main activity after a delay
        val splashTimer = object : Thread() {
            override fun run() {
                try {
                    sleep(2000) // 2 seconds delay
                    val intent = Intent(baseContext, SignInActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        splashTimer.start()
    }

    override fun onPause() {
        super.onPause()
        finish() // Finish the splash screen activity to prevent going back to it
    }
}