package com.genius.testlib.base

/**
 * Created by geniuS on 9/4/2019.
 */
interface SuperModel {
    fun showLoading()
    fun cancelLoading()
    fun showToast(message: String?)
    fun showLongToast(message: String?)
    fun debugToast(message: String?)
    val isNetworkConnected: Boolean
    fun hideKeyboard()
    fun printLog(message: String?)
    fun showSnack(message: String?)
}