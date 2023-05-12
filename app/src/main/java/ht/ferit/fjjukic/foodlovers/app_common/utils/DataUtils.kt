package ht.ferit.fjjukic.foodlovers.app_common.utils

import android.content.res.Resources

inline fun <reified T> mergeLists(vararg arrays: List<T>): List<T> {
    val list: MutableList<T> = ArrayList()
    for (array in arrays) {
        list.addAll(array.map { i -> i })
    }
    return list.toMutableList()
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()