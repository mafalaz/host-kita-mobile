package com.project.hostkita.ui.payment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityPaymentSuccessBinding
import com.project.hostkita.ui.dashboardumkm.DashboardUmkm

class PaymentActivitySuccess : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memuat GIF dari drawable
        Glide.with(this)
            .asGif()
            .load(R.drawable.verified) // Ganti dengan nama resource GIF Anda
            .into(binding.imageViewGif)

        val namaProduk = intent.getStringExtra("namaProduk") ?: ""
        val jumlahProduk = intent.getStringExtra("jumlahProduk") ?: ""
        val tanggalLive = intent.getStringExtra("tanggalLive") ?: ""

        binding.tvNamaProduk.text = namaProduk
        binding.tvJumlahProduk.text = jumlahProduk + "Pcs"
        binding.tvTanggalLive.text = tanggalLive

        binding.buttonMaps.setOnClickListener {
            val uri = Uri.parse("https://maps.app.goo.gl/voaacACGg45AkhVYA?g_st=ac")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Jika aplikasi Google Maps tidak tersedia, arahkan ke Play Store
                val playStoreUri = Uri.parse("market://details?id=com.google.android.apps.maps")
                val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
                if (playStoreIntent.resolveActivity(packageManager) != null) {
                    startActivity(playStoreIntent)
                } else {
                    // Jika Play Store juga tidak tersedia, arahkan ke Google Maps di browser
                    val browserUri =
                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                    val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                    startActivity(browserIntent)
                }
            }
        }

        binding.buttonKeOrderPage.setOnClickListener {
            val intent = Intent(this, DashboardUmkm::class.java)
            startActivity(intent)
        }
    }
}

