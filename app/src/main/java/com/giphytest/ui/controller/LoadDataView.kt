package com.giphytest.ui.controller

import android.content.Context


interface LoadDataView {

    fun showLoading()

    fun hideLoading()

    fun showRetry()

    fun hideRetry()

    fun showError(message: String)

}
