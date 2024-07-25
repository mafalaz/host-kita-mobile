package com.project.hostkita.ui.getstarted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityGetStartedBinding

class GetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStarted.setOnClickListener{
            val intent = Intent(this, HomeSlider::class.java)
            startActivity(intent)
        }
    }
}