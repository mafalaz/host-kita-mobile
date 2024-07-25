package com.project.hostkita.ui.dashboardumkm.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hostkita.adapter.OrderAdapter
import com.project.hostkita.databinding.FragmentHomeBinding
import com.project.hostkita.detail.DetailOrderActivity
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {
    private lateinit var orderAdapter: OrderAdapter
    private val orderViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreference.getInstance(requireContext().dataStore)
        val token = runBlocking { pref.getSession().first().token }

        val recyclerView2 = binding.rvOrder
        recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter { view ->
            val position = recyclerView2.getChildAdapterPosition(view)
            val clickedUser = orderAdapter.getOrder(position)
            val intent = Intent(requireContext(), DetailOrderActivity::class.java)
            intent.putExtra("orderId", clickedUser.orderId.toString())
            startActivity(intent)
        }
        recyclerView2.adapter = orderAdapter

        // Observe orderList LiveData
        orderViewModel.orderList.observe(viewLifecycleOwner, Observer { rekomendasiArticles ->
            if (rekomendasiArticles.isNullOrEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
                recyclerView2.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                recyclerView2.visibility = View.VISIBLE
                orderAdapter.setOrder(rekomendasiArticles)
            }
        })

        // Load orders
        orderViewModel.loadOrderUser(token)

        // Observe error LiveData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
