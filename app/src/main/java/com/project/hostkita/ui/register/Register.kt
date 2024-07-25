package com.project.hostkita.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityLoginRegisterBinding
import com.project.hostkita.databinding.ActivityRegisterBinding
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.models.ErrorResponse
import com.project.hostkita.ui.dashboardumkm.DashboardUmkm
import com.project.hostkita.ui.login.Login

class Register : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val name = binding.edRegisterName.text.toString()
            val password = binding.passwordEditText.text.toString()
            showLoading(true)

            viewModel.register(name, email, password) { success, errorMessage ->
                showLoading(false)

                if (success) {
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    try {
                        val errorBody = Gson().fromJson(errorMessage, ErrorResponse::class.java)
                        val detailedErrorMessage = errorBody.message
                        Toast.makeText(this, detailedErrorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonLogin.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}