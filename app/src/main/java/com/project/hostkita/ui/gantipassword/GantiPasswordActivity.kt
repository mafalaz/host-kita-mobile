package com.project.hostkita.ui.gantipassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityGantiPasswordBinding
import com.project.hostkita.databinding.ActivityUpdateRekeningBinding

class GantiPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGantiPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGantiPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.scan_menu_appbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}