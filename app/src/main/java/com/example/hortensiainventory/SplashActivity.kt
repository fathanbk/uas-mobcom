package com.example.hortensiainventory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Create an Intent for the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Use a Handler to delay the start of MainActivity
        Handler().postDelayed({
            // Start the MainActivity after 5 seconds
            startActivity(intent)
            finish() // Optional: Finish the current activity if needed
        }, 5000) // 5000 milliseconds (5 seconds)
    }
}