package ht.ferit.fjjukic.foodlovers.app_common.utils

inline fun<reified T> mergeLists(vararg arrays: List<T>): List<T> {
    val list: MutableList<T> = ArrayList()
    for (array in arrays) {
        list.addAll(array.map { i -> i })
    }
    return list.toMutableList()
}