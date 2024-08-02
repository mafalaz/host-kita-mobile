package com.project.hostkita.ui.detailpenjualan

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.project.hostkita.R
import com.project.hostkita.databinding.ActivityDetailPenjualanBinding
import com.project.hostkita.databinding.ActivityPenjualanBinding
import com.project.hostkita.detail.DetailOrderViewModel
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.models.PenjualanDetailItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DetailPenjualanActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailPenjualanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var barChart: BarChart
    private lateinit var barChartPendapatan: BarChart
    private lateinit var binding: ActivityDetailPenjualanBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.detail_penjualan)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        barChart = binding.barChart
        barChartPendapatan = binding.barChartPendapatan

        val orderId = intent.getStringExtra("orderId")
        val pref = UserPreference.getInstance(this.dataStore)
        val token = runBlocking { pref.getSession().first().token }
        if (orderId != null) {
            viewModel.loadPenjualanDetail(token, orderId)
        }

        viewModel.penjualanDetail.observe(this, Observer { penjualanList ->
            penjualanList?.let {
                setupBarChart(it)
                setupPendapatanChart(it)
                if (it.isNotEmpty()) {
                    // Ambil data terbaru berdasarkan tanggal
                    val latestPenjualan = it.maxByOrNull { penjualan -> parseDate(penjualan.tanggalUpdatePenjualan) }
                    // Perbarui tampilan dengan data terbaru dan gabungkan total pendapatan
                    updateTextViews(it)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupBarChart(penjualanList: List<PenjualanDetailItem>) {
        // Urutkan list berdasarkan tanggal
        val sortedList = penjualanList.sortedBy { parseDate(it.tanggalUpdatePenjualan) }

        val entriesSisaProduk = ArrayList<BarEntry>()
        val entriesTotalCheckout = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        sortedList.forEachIndexed { index, penjualan ->
            penjualan.sisaProduk?.let { entriesSisaProduk.add(BarEntry(index.toFloat(), it.toFloat())) }
            penjualan.totalCheckout?.let { entriesTotalCheckout.add(BarEntry(index.toFloat(), it.toFloat())) }
            labels.add(formatDate(penjualan.tanggalUpdatePenjualan))
        }

        val dataSetSisaProduk = BarDataSet(entriesSisaProduk, "Sisa Produk")
        dataSetSisaProduk.color = ColorTemplate.rgb("#6bf3ad") // Color for "Sisa Produk"

        val dataSetTotalCheckout = BarDataSet(entriesTotalCheckout, "Total Checkout")
        dataSetTotalCheckout.color = ColorTemplate.rgb("#a5e7ff") // Color for "Total Checkout"

        val data = BarData(dataSetSisaProduk, dataSetTotalCheckout)
        barChart.data = data
        dataSetSisaProduk.valueTextSize = 12f
        dataSetTotalCheckout.valueTextSize = 12f
        barChart.setDrawGridBackground(false)
        barChart.description.text = ""
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = true

        // Format tanggal untuk axis
        barChart.xAxis.apply {
            valueFormatter = object : IndexAxisValueFormatter(labels) {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < labels.size) labels[index] else ""
                }
            }
            granularity = 1f // Ensure there is only one label per axis tick
            setDrawLabels(true)
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.TOP // Position labels at the bottom
        }

        barChart.axisLeft.apply {
            // Hilangkan grid lines di sumbu kiri
            // Hilangkan grid lines di sumbu kiri

            setDrawAxisLine(true) // Tampilkan garis axis
        }

        barChart.invalidate() // Refresh chart
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPendapatanChart(penjualanList: List<PenjualanDetailItem>) {
        // Urutkan list berdasarkan tanggal
        val sortedList = penjualanList.sortedBy { parseDate(it.tanggalUpdatePenjualan) }

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        sortedList.forEachIndexed { index, penjualan ->
            penjualan.totalPendapatan?.let { entries.add(BarEntry(index.toFloat(), it.toFloat())) }
            labels.add(formatDate(penjualan.tanggalUpdatePenjualan))
        }

        val dataSet = BarDataSet(entries, "Total Pendapatan")
        dataSet.color = ColorTemplate.rgb("#f7f79c") // Color for "Total Pendapatan"

        val data = BarData(dataSet)
        dataSet.valueTextSize = 12f
        barChartPendapatan.data = data
        barChartPendapatan.setDrawGridBackground(false)
        barChartPendapatan.setDrawBarShadow(false)
        barChartPendapatan.description.text = ""
        barChartPendapatan.axisRight.isEnabled = false
        barChartPendapatan.setDrawValueAboveBar(true)
        barChartPendapatan.legend.isEnabled = true

        // Format tanggal untuk axis
        barChartPendapatan.xAxis.apply {
            valueFormatter = object : IndexAxisValueFormatter(labels) {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < labels.size) labels[index] else ""
                }
            }
            granularity = 1f // Ensure there is only one label per axis tick
            setDrawLabels(true)
            setDrawGridLines(false) // Hilangkan grid lines di sumbu X
            position = XAxis.XAxisPosition.TOP // Position labels at the bottom
        }

        barChartPendapatan.axisLeft.apply {
            // Hilangkan grid lines di sumbu kiri

            setDrawAxisLine(true) // Tampilkan garis axis
        }

        barChartPendapatan.invalidate() // Refresh chart
    }

    private fun parseDate(dateString: String?): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return dateString?.let { formatter.parse(it) } ?: Date()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTextViews(penjualanList: List<PenjualanDetailItem>) {
        val numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)

        // Hitung total pendapatan dari semua item
        val totalPendapatanSemua = penjualanList.sumOf { it.totalPendapatan ?: 0 }

        // Ambil informasi dari item terbaru berdasarkan tanggal
        val latestPenjualan = penjualanList.maxByOrNull { penjualan -> parseDate(penjualan.tanggalUpdatePenjualan) }

        latestPenjualan?.let { penjualan ->
            binding.tvNamaProduk.text = penjualan.namaProduk
            binding.tvHargaProduk.text = "Rp " + numberFormat.format(penjualan.hargaProduk)

            val originalDate = penjualan.tanggalUpdatePenjualan
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val parsedDate = LocalDateTime.parse(originalDate, formatter)
            val dateOnly = parsedDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            binding.tvTanggalUpdate.text = dateOnly
        }

        // Tampilkan total pendapatan yang digabungkan
        binding.tvTotalPendapatan.text = "Rp " + numberFormat.format(totalPendapatanSemua)
    }

    private fun formatDate(dateString: String?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = formatter.parse(dateString)
        return SimpleDateFormat("MM-dd", Locale.getDefault()).format(parsedDate)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}



