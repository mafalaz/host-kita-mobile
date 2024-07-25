package com.project.hostkita.ui.dashboardumkm.ui.dashboard

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.hostkita.databinding.FragmentDashboardBinding
import com.project.hostkita.detail.DetailOrderActivity
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.ui.payment.PaymentActivity
import com.project.hostkita.utils.getImageUri
import com.project.hostkita.utils.reduceFileImage
import com.project.hostkita.utils.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val calendar = Calendar.getInstance()
    private var currentImageUri: Uri? = null

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        checkAndDisableButton()

        binding.tanggalButton.setOnClickListener {
            showDatePicker()
        }

//        binding.galleryButton.setOnClickListener{
//            showPopup()
//        }

        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)

        binding.buttonCekHarga.setOnClickListener {
            val cekJumlahProduk = binding.inputJumlahProduk.text.toString()
            val cekHargaSatuan = binding.inputHargaProduk.text.toString()

            if (cekJumlahProduk.isNotEmpty() && cekHargaSatuan.isNotEmpty()) {
                val jumlahProdukInt = cekJumlahProduk.toInt()

                if (jumlahProdukInt < 50) {
                    showToast("Minimum untuk jumlah produk adalah 50 pcs")
                } else {
                    val biayaPacking = jumlahProdukInt * 1000
                    val hargaSatuanInt = cekHargaSatuan.toInt()
                    val biayaHost = hargaSatuanInt * 0.05 * jumlahProdukInt
                    val biayaPlatform = 5000
                    val biayaTotal = biayaPacking + biayaHost + biayaPlatform

                    binding.tvBiayaPacking.text = "Rp " + numberFormat.format(biayaPacking)
                    binding.tvBiayaHost.text = "Rp " + numberFormat.format(biayaHost)
                    binding.tvBiayaPlatform.text = "Rp " + numberFormat.format(biayaPlatform)
                    binding.tvTotalBiaya.text = "Rp " + numberFormat.format(biayaTotal)
                }
            } else {
                binding.tvBiayaPacking.text = "Rp 0"
                binding.tvBiayaHost.text= "Rp 0"
                binding.tvBiayaPlatform.text = "Rp 0"
                binding.tvTotalBiaya.text = "Rp 0"
            }

            checkAndDisableButton()
        }


        binding.buttonSubmitOrder.setOnClickListener {
            showPopup2()
        }
    }

    private fun showPopup2() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah orderan Anda sudah sesuai?")

        // Set up the buttons
        builder.setPositiveButton("Ya") { _, _ ->
            val namaProduk = binding.inputNamaProduk.text.toString()
            val harga = binding.inputHargaProduk.text.toString()
            val berat = binding.inputBeratProduk.text.toString()
            val lebar = binding.inputLebarProduk.text.toString()
            val panjang = binding.inputPanjangProduk.text.toString()
            val tinggi = binding.inputTinggiProduk.text.toString()
            val jumlahProduk = binding.inputJumlahProduk.text.toString()
            val tanggalLive = binding.tvTanggal.text.toString()
            val biayaPacking = binding.tvBiayaPacking.text.toString()
            val biayaHost = binding.tvBiayaHost.text.toString()
            val biayaPlatform = binding.tvBiayaPlatform.text.toString()
            val totalPayment = binding.tvTotalBiaya.text.toString()
            val deskripsi = binding.inputDeskripsi.text.toString()

            val pref = UserPreference.getInstance(requireContext().dataStore)
            val token = runBlocking { pref.getSession().first().token }

            if (namaProduk.isEmpty() || harga.isEmpty() || berat.isEmpty() || lebar.isEmpty() || panjang.isEmpty() || tinggi.isEmpty() || jumlahProduk.isEmpty() || tanggalLive.isEmpty() || deskripsi.isEmpty()) {
                showToast("Form tidak boleh ada yang kosong")
            }  else {

//                    viewModel.uploadImage(namaProduk, harga, berat, lebar, panjang, tinggi, jumlahProduk, fotoProduk, tanggalLive, deskripsi, biayaPacking, biayaHost, biayaPlatform, totalPayment, token,
//                        onImageUploadComplete = {
//                            showLoading(false)
//                            showToast("Pembayaran Berhasil Dibuat")
//                            val intent = Intent(requireContext(), PaymentActivity::class.java).apply {
//                                putExtra("totalPayment", totalPayment)
//                            }
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                        }
//                    ) { errorMessage ->
//                        _error.value = errorMessage
//                    }

                val intent = Intent(requireContext(), PaymentActivity::class.java).apply {
                    putExtra("namaProduk", namaProduk)
                    putExtra("harga", harga)
                    putExtra("berat", berat)
                    putExtra("lebar", lebar)
                    putExtra("panjang", panjang)
                    putExtra("tinggi", tinggi)
                    putExtra("jumlahProduk", jumlahProduk)
                    putExtra("tanggalLive", tanggalLive)
                    putExtra("biayaPacking", biayaPacking)
                    putExtra("biayaHost", biayaHost)
                    putExtra("biayaPlatform", biayaPlatform)
                    putExtra("totalPayment", totalPayment)
                    putExtra("deskripsi", deskripsi)
                }
                startActivity(intent)
            }
        }

        builder.setNegativeButton("Cek Lagi") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }


    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.tvTanggal.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

//    private fun showImage() {
//        currentImageUri?.let {
//            Log.d("Image URI", "showImage: $it")
//            binding.previewImageView.setImageURI(it)
//        }
//    }

//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.TakePicture()
//    ) { isSuccess ->
//        if (isSuccess) {
//            showImage()
//        }
//    }
//
//    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
//            showImage()
//        } else {
//            Log.d("Photo Picker", "No media selected")
//        }
//    }

//    private fun startCamera() {
//        currentImageUri = getImageUri(requireContext())
//        launcherIntentCamera.launch(currentImageUri)
//    }
//
//    private fun startGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }

//    private fun showPopup() {
//        val builder = AlertDialog.Builder(requireContext())
//
//        builder.setTitle("Pilih Foto Produk")
//        builder.setMessage("Melalui galeri atau kamera")
//
//        // Set up the buttons
//        builder.setPositiveButton("Galeri") { _, _ ->
//            startGallery()
//        }
//
//        builder.setNegativeButton("Kamera") { _, _ ->
//            startCamera()
//        }
//
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }

    private fun checkAndDisableButton() {
        val biayaPackingText = binding.tvBiayaPacking.text.toString()
        val biayaHostText = binding.tvBiayaHost.text.toString()
        val totalBiayaText = binding.tvTotalBiaya.text.toString()
        val biayaPlatformText = binding.tvBiayaPlatform.text.toString()

        if (biayaPackingText == "Rp 0" ||
            biayaHostText == "Rp 0" ||
            totalBiayaText == "Rp 0" ||
            biayaPlatformText == "Rp 0" ||
            biayaPackingText.isEmpty() ||
            biayaHostText.isEmpty() ||
            totalBiayaText.isEmpty() ||
            biayaPlatformText.isEmpty()) {
            binding.buttonSubmitOrder.isEnabled = false
            binding.buttonSubmitOrder.isClickable = false
            binding.buttonSubmitOrder.setBackgroundColor(Color.GRAY)
        } else {
            binding.buttonSubmitOrder.isEnabled = true
            binding.buttonSubmitOrder.isClickable = true
            binding.buttonSubmitOrder.setBackgroundColor(Color.BLUE)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingBarOrder.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}