package ht.ferit.fjjukic.foodlovers.app_common.model


sealed class ScreenEvent

class LoadingBar(val isVisible: Boolean) : ScreenEvent()

data class MessageModel(
    val message: String? = null,
    val messageId: Int? = null,
    val isShortLength: Boolean = true
) : ScreenEvent()

data class SnackbarModel(
    val message: String? = null,
    val messageId: Int? = null,
    val isShortLength: Boolean = true
) : ScreenEvent()

data class RefreshFiltersScreenEvent(val value: FilterModel) : ScreenEvent()