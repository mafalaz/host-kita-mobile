package com.project.hostkita.rekening

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityUpdateRekeningBinding
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UpdateRekeningActivity : AppCompatActivity() {
    private val viewModel by viewModels<RekeningViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityUpdateRekeningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.scan_menu_appbar)
        setSupportActionBar(toolbar)

        val pref = UserPreference.getInstance(this.dataStore)
        val token = runBlocking { pref.getSession().first().token }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel.loadRekening(token)

        viewModel.namaBank.observe(this) { namaBank ->
            binding.tvNamaBank.text = namaBank
            checkAndDisableButton()
        }

        viewModel.noRekening.observe(this) { noRekening ->
            binding.tvNoRekening.text = noRekening
            checkAndDisableButton()
        }

        viewModel.atasNama.observe(this) { atasNama ->
            binding.tvAtasNama.text = atasNama
            checkAndDisableButton()
        }

        viewModel.error.observe(this) { _ ->
            Toast.makeText(this, "Silahkan Isi Data Rekening Anda", Toast.LENGTH_SHORT).show()
        }

        viewModel.rekeningId.observe(this) { rekeningId ->
            // Use rekeningId when updating the rekening
            binding.buttonUpdate.setOnClickListener {
                val namaBank = binding.inputNamaBank.text.toString()
                val noRekening = binding.inputnoRekening.text.toString()
                val atasNama = binding.inputAtasNamaRekening.text.toString()

                if (namaBank.isEmpty() || noRekening.isEmpty() || atasNama.isEmpty()) {
                    Toast.makeText(this, "Form tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val noRekeningInt = noRekening.toInt()

                    binding.tvNamaBank.text = namaBank
                    binding.tvNoRekening.text = noRekening
                    binding.tvAtasNama.text = atasNama

                    viewModel.updateRekening(token, rekeningId.toInt(), namaBank, noRekeningInt, atasNama)

                    viewModel.success.observe(this, Observer { isSuccess ->
                        if (isSuccess) {
                            Toast.makeText(this, "Rekening Anda Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                            binding.inputNamaBank.text?.clear()
                            binding.inputnoRekening.text?.clear()
                            binding.inputAtasNamaRekening.text?.clear()
                        }
                    })

                    viewModel.error.observe(this, Observer { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }


        binding.buttonSimpan.setOnClickListener {
            val namaBank = binding.inputNamaBank.text.toString()
            val noRekening = binding.inputnoRekening.text.toString()
            val atasNama = binding.inputAtasNamaRekening.text.toString()

            if (namaBank.isEmpty() || noRekening.isEmpty() || atasNama.isEmpty()) {
                Toast.makeText(this, "Form tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            } else {
                val noRekeningInt = noRekening.toInt()

                binding.tvNamaBank.text = namaBank
                binding.tvNoRekening.text = noRekening
                binding.tvAtasNama.text = atasNama

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.addRekeningUser(token, namaBank, noRekeningInt, atasNama)
                    }

                    withContext(Dispatchers.Main) {
                        binding.inputNamaBank.text?.clear()
                        binding.inputnoRekening.text?.clear()
                        binding.inputAtasNamaRekening.text?.clear()

                        Toast.makeText(
                            this@UpdateRekeningActivity,
                            "Data Rekening Anda Berhasil Disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                checkAndDisableButton()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkAndDisableButton() {
        if (!binding.tvNamaBank.text.isNullOrEmpty() &&
            !binding.tvNoRekening.text.isNullOrEmpty() &&
            !binding.tvAtasNama.text.isNullOrEmpty()
        ) {
            binding.buttonSimpan.isEnabled = false
            binding.buttonSimpan.isClickable = false
            binding.buttonSimpan.setBackgroundColor(Color.GRAY)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
