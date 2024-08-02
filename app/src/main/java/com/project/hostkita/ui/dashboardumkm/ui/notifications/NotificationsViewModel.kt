package com.project.hostkita.ui.dashboardumkm.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Belum ada payment"
    }
    val text: LiveData<String> = _text
}