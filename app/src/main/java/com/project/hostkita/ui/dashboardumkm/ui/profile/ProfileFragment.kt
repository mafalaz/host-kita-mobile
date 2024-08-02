package com.project.hostkita.ui.dashboardumkm.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.project.hostkita.R
import com.project.hostkita.databinding.FragmentProfileBinding
import com.project.hostkita.di.UserPreference
import com.project.hostkita.di.dataStore
import com.project.hostkita.middleware.ViewModelFactory
import com.project.hostkita.rekening.UpdateRekeningActivity
import com.project.hostkita.ui.gantipassword.GantiPasswordActivity
import com.project.hostkita.ui.login.Login
import com.project.hostkita.ui.payment.PaymentActivity
import com.project.hostkita.ui.penjualan.PenjualanActivity
import com.project.hostkita.ui.splashscreen.AuthViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModelUmkm by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreference.getInstance(requireContext().dataStore)
        val token = runBlocking { pref.getSession().first().token }

        viewModelUmkm.loadGetUmkm(token)

        binding.buttonImageLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        viewModelUmkm.name.observe(viewLifecycleOwner) { name ->
            binding.tvName.text = name.toString()
        }

        binding.buttonImageUpdate.setOnClickListener{
            val intent = Intent(requireContext(), UpdateRekeningActivity::class.java)
            startActivity(intent)
        }

        binding.buttonImageGantiPassword.setOnClickListener{
            val intent = Intent(requireContext(), GantiPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.buttonPenjualan.setOnClickListener{
            val intent = Intent(requireContext(), PenjualanActivity::class.java)
            startActivity(intent)
        }

        viewModelUmkm.name.observe(viewLifecycleOwner) { name ->
            binding.buttonCS.setOnClickListener {
                val phoneNumber = "+6282210329085" // Ganti dengan nomor telepon CS yang valid
                val message = "Halo, saya $name. Saya butuh bantuan."
                val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp")

                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Install Whatsapp terlebih dahulu.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}