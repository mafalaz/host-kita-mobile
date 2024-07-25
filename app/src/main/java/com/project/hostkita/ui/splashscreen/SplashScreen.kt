package com.project.hostkita.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.project.hostkita.R
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.ui.dashboardumkm.DashboardUmkm
import com.project.hostkita.ui.getstarted.GetStarted
import com.project.hostkita.ui.payment.PaymentActivity
import com.project.hostkita.ui.payment.PaymentActivitySuccess

class SplashScreen : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashDelay = 2000

        val imageView = findViewById<ImageView>(R.id.logoView)
        imageView.setBackgroundResource(R.drawable.logo_host_kita)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                Handler().postDelayed({
                    val intent = Intent(this, DashboardUmkm::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, splashDelay.toLong())
            } else {
                Handler().postDelayed({
                    val intent = Intent(this, GetStarted::class.java)
                    startActivity(intent)
                }, splashDelay.toLong())
            }
        }
    }
}