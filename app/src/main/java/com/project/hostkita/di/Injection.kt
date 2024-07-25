package com.project.hostkita.di

import android.content.Context
import com.project.hostkita.api.ApiConfig
import com.project.hostkita.api.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = provideApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    private fun provideApiService(token: String): ApiService {
        return ApiConfig.getApiService(token)
    }
}