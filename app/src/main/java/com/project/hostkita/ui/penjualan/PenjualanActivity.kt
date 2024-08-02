package com.project.hostkita.ui.penjualan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hostkita.R
import com.project.hostkita.adapter.OrderAdapter
import com.project.hostkita.adapter.PenjualanAdapter
import com.project.hostkita.databinding.ActivityGantiPasswordBinding
import com.project.hostkita.databinding.ActivityPenjualanBinding
import com.project.hostkita.detail.DetailOrderActivity
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.ui.dashboardumkm.ui.home.HomeViewModel
import com.project.hostkita.ui.detailpenjualan.DetailPenjualanActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class PenjualanActivity : AppCompatActivity() {
    private lateinit var penjualanAdapter: PenjualanAdapter
    private lateinit var binding: ActivityPenjualanBinding

    private val viewModel by viewModels<PenjualanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.pejualan_appbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val pref = UserPreference.getInstance(this.dataStore)
        val token = runBlocking { pref.getSession().first().token }

        val recyclerView2 = binding.rvPenjualan
        recyclerView2.layoutManager = LinearLayoutManager(this)
        penjualanAdapter = PenjualanAdapter { view ->
            val position = recyclerView2.getChildAdapterPosition(view)
            val clickedUser = penjualanAdapter.getPenjualan(position)
            val intent = Intent(this, DetailPenjualanActivity::class.java)
            intent.putExtra("orderId", clickedUser.orderId.toString())
            startActivity(intent)
        }
        recyclerView2.adapter = penjualanAdapter

        // Observe orderList LiveData
        viewModel.orderList.observe(this) { penjualan ->
            if (penjualan.isNullOrEmpty()) {
                binding.tvNoDataPenjualan.visibility = View.VISIBLE
                recyclerView2.visibility = View.GONE
            } else {
                binding.tvNoDataPenjualan.visibility = View.GONE
                recyclerView2.visibility = View.VISIBLE
                penjualanAdapter.setPejualan(penjualan)
            }
        }

        // Load orders
        viewModel.loadPenjualanUser(token)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}