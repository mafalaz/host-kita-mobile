package com.project.hostkita.middleware

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.hostkita.detail.DetailOrderViewModel
import com.project.hostkita.di.Injection
import com.project.hostkita.di.UserRepository
import com.project.hostkita.rekening.RekeningViewModel
import com.project.hostkita.ui.dashboardumkm.ui.dashboard.DashboardViewModel
import com.project.hostkita.ui.dashboardumkm.ui.home.HomeViewModel
import com.project.hostkita.ui.dashboardumkm.ui.profile.ProfileViewModel
import com.project.hostkita.ui.login.LoginViewModel
import com.project.hostkita.ui.register.RegisterViewModel
import com.project.hostkita.ui.splashscreen.AuthViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RekeningViewModel::class.java) -> {
                RekeningViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailOrderViewModel::class.java) -> {
                DetailOrderViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}