package com.project.hostkita.ui.loginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityHomeSliderBinding
import com.project.hostkita.databinding.ActivityLoginRegisterBinding
import com.project.hostkita.ui.login.Login
import com.project.hostkita.ui.register.Register

class LoginRegister : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDaftar.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.buttonMasuk.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}