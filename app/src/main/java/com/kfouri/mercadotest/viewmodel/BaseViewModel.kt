package com.kfouri.mercadotest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private var showProgress = MutableLiveData<Boolean>()
    private var showToast = MutableLiveData<String>()

    protected fun showProgress(value: Boolean) {
        showProgress.value = value
    }

    protected fun showToast(error: String) {
        showToast.value = error
    }

    fun onShowProgress() = showProgress
    fun onShowToast() = showToast
}