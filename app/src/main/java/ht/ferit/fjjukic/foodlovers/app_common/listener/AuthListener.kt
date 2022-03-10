package ht.ferit.fjjukic.foodlovers.app_common.listener

interface AuthListener {
    fun onSuccess()
    fun onFailure(message: String)
}