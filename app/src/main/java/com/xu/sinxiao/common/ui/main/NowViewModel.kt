package com.xu.sinxiao.common.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NowViewModel : ViewModel() {
    fun getTopic(): Unit {
        viewModelScope.launch {

        }
    }
}