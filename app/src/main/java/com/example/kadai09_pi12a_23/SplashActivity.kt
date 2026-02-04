package com.example.kadai09_pi12a_23

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * 启动页：显示 splash_zatsugaku_king.png，约 2 秒后跳转至主界面
 */
class SplashActivity : AppCompatActivity() {

    private val splashDelayMillis = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleManager.applySavedLocale(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashDelayMillis)
    }
}
