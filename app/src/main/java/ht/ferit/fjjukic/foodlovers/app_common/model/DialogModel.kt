package ht.ferit.fjjukic.foodlovers.app_common.model

data class DialogModel(
    val title: Int,
    val message: Int? = null,
    val positiveTitleId: Int,
    val positiveAction: () -> Unit,
    val negativeTitleId: Int? = null,
    val negativeAction: (() -> Unit)? = null,
    val neutralTitleId: Int? = null,
    val neutralAction: (() -> Unit)? = null,
    val isCancellable: Boolean = true
): ScreenEvent()
