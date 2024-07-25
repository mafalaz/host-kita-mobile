package com.project.hostkita.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityDetailOrderBinding
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding

    private val viewModel by viewModels<DetailOrderViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.details_menu_appbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val orderId = intent.getStringExtra("orderId")
        val pref = UserPreference.getInstance(this.dataStore)
        val token = runBlocking { pref.getSession().first().token }
        if (orderId != null) {
            viewModel.loadOrderDetail(token, orderId)
        }

        viewModel.orderDetail.observe(this, Observer { order ->
            Glide.with(this@DetailOrderActivity)
                .load(order?.fotoProduk)
                .into(binding.imageFotoProduk)

            val originalDate = order?.tanggalLive
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val parsedDate = LocalDateTime.parse(originalDate, formatter)
            val dateOnly = parsedDate.toLocalDate().toString()

            val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)

            binding.tvOrderId.text = order?.orderId.toString()
            binding.tvDetailNamaProduk.text = order?.namaProduk
            binding.tvDetailJumlahProduk.text = order?.jumlahProduk.toString()
            binding.tvDetailTanggalLive.text = dateOnly
            binding.tvHargaProduk.text = "Rp " + numberFormat.format(order?.hargaProduk)
            binding.tvDetailDeskripsi.text = order?.deskripsi
            binding.tvDetailStatusLive.text = order?.statusLive
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}