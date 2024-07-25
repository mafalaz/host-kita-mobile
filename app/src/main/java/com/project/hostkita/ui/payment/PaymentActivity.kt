package com.project.hostkita.ui.payment

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityDetailOrderBinding
import com.project.hostkita.databinding.ActivityPaymentBinding
import com.project.hostkita.detail.DetailOrderViewModel
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.ui.dashboardumkm.DashboardUmkm
import com.project.hostkita.ui.dashboardumkm.ui.dashboard.DashboardFragment
import com.project.hostkita.ui.dashboardumkm.ui.dashboard.DashboardViewModel
import com.project.hostkita.utils.getImageUri
import com.project.hostkita.utils.reduceFileImage
import com.project.hostkita.utils.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUriProduk: Uri? = null
    private var currentImageUriBukti: Uri? = null

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            PaymentActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(PaymentActivity.REQUIRED_PERMISSION)
        }

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.scan_menu_appbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.galleryButton.setOnClickListener{
            showPopup()
        }

        binding.galleryButtonBuktiPembayaran.setOnClickListener{
            showPopup2()
        }

        val namaProduk = intent.getStringExtra("namaProduk") ?: ""
        val harga = intent.getStringExtra("harga") ?: ""
        val berat = intent.getStringExtra("berat") ?: ""
        val lebar = intent.getStringExtra("lebar") ?: ""
        val panjang = intent.getStringExtra("panjang") ?: ""
        val tinggi = intent.getStringExtra("tinggi") ?: ""
        val jumlahProduk = intent.getStringExtra("jumlahProduk") ?: ""
        val tanggalLive = intent.getStringExtra("tanggalLive") ?: ""
        val biayaPacking = intent.getStringExtra("biayaPacking") ?: ""
        val biayaHost = intent.getStringExtra("biayaHost") ?: ""
        val biayaPlatform = intent.getStringExtra("biayaPlatform") ?: ""
        val totalPayment = intent.getStringExtra("totalPayment") ?: ""
        val deskripsi = intent.getStringExtra("deskripsi") ?: ""

        binding.tvNamaProduk.text = namaProduk
        binding.tvJumlahProduk.text = jumlahProduk
        binding.tvTanggalLive.text = tanggalLive
        binding.tvBiayaPacking.text = biayaPacking
        binding.tvBiayaHost.text = biayaHost
        binding.tvBiayaPlatform.text = biayaPlatform
        binding.tvTotalBiaya.text = totalPayment
        binding.tvBigPrice.text = totalPayment

        binding.buttonBayarSekarang.setOnClickListener {
            if (currentImageUriProduk == null) {
                showToast("Pilih foto produk terlebih dahulu")
            } else if (currentImageUriBukti == null) {
                showToast("Pilih bukti pembayaran terlebih dahulu")
            }
            else {
                val fotoProduk = uriToFile(currentImageUriProduk!!, this).reduceFileImage()
                val buktiTransfer = uriToFile(currentImageUriBukti!!, this).reduceFileImage()

                val pref = UserPreference.getInstance(this.dataStore)
                val token = runBlocking { pref.getSession().first().token }

                showLoading(true)

                viewModel.uploadImage(namaProduk, harga, berat, lebar, panjang, tinggi, jumlahProduk, fotoProduk, tanggalLive, deskripsi, biayaPacking, biayaHost, biayaPlatform, totalPayment, buktiTransfer, token,
                    onImageUploadComplete = {
                        showLoading(false)
                        showToast("Pembayaran Berhasil")
                        val intent = Intent(this, PaymentActivitySuccess::class.java).apply {
                            putExtra("namaProduk", namaProduk)
                            putExtra("jumlahProduk", jumlahProduk)
                            putExtra("tanggalLive", tanggalLive)
                        }
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                ) { errorMessage ->
                    _error.value = errorMessage
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingBarOrder.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun showImage() {
        currentImageUriProduk?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showImageBukti() {
        currentImageUriBukti?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageViewBuktiPembayaran.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUriProduk = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCameraBuktiPayment = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImageBukti()
        }
    }

    private val launcherGalleryBuktiPayment = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUriBukti = uri
            showImageBukti()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUriProduk = getImageUri(this)
        launcherIntentCamera.launch(currentImageUriProduk)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCameraBuktiPayment() {
        currentImageUriBukti = getImageUri(this)
        launcherIntentCameraBuktiPayment.launch(currentImageUriBukti)
    }

    private fun startGalleryBuktiPayment() {
        launcherGalleryBuktiPayment.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showPopup() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Pilih Foto Produk")
        builder.setMessage("Melalui galeri atau kamera")

        // Set up the buttons
        builder.setPositiveButton("Galeri") { _, _ ->
            startGallery()
        }

        builder.setNegativeButton("Kamera") { _, _ ->
            startCamera()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showPopup2() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Pilih Bukti Pembayaran")
        builder.setMessage("Melalui galeri atau kamera")

        // Set up the buttons
        builder.setPositiveButton("Galeri") { _, _ ->
            startGalleryBuktiPayment()
        }

        builder.setNegativeButton("Kamera") { _, _ ->
            startCameraBuktiPayment()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}