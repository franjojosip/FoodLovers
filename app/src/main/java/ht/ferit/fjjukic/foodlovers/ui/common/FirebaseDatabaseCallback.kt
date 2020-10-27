package ht.ferit.fjjukic.foodlovers.ui.common

interface FirebaseDatabaseCallback {
    fun <T : Any> onCallback(value: T?)
}