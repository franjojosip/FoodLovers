package ht.ferit.fjjukic.foodlovers.app_common.listener

interface ResultListener {
    fun <T> onSuccess(value: T)
    fun onFailure(message: String)
}