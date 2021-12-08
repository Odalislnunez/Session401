package es.usj.mastertsa.onunez.session401

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import java.util.*

class FifthSplashScreenActivity : AppCompatActivity() {
    companion object { private val SPLASH_SCREEN_DELAY: Long = 4000 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_fifth_splash_screen)
        val task = object : TimerTask() {
            override fun run() {
                val intent = Intent(this@FifthSplashScreenActivity,
                    FifthActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)
    }
}