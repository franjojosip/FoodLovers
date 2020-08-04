package ht.ferit.fjjukic.foodlovers.ui.common

interface AuthListener {
    fun onSuccess()
    fun onFailure(message: String)
}