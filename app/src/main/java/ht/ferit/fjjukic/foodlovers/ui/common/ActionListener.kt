package ht.ferit.fjjukic.foodlovers.ui.common

interface ActionListener {
    fun show(id: String, userId: String, imageId: String)
    fun checkRecipe(id: String)
}